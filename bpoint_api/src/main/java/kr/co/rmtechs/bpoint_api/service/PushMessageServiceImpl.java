package kr.co.rmtechs.bpoint_api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.rmtechs.bpoint_api.config.BpointProperties;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_PUSH;
import kr.co.rmtechs.bpoint_domain.eTYPE_PUSH_SEND;
import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.EventHistVO;
import kr.co.rmtechs.bpoint_domain.vo.PushMessageVO;

@Service
public class PushMessageServiceImpl {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired 
	BpointDomainMapper appMapper;
	@Autowired
	BpointProperties bpointProperties;
	
	public static final String DB_EVENT_PUSH_NONE = "5";
	public static final String DB_EVENT_PUSH_READ = "6";
	
	public static final char DB_PUSH_RESPONSE_SUCCESS = '1';
	public static final char DB_PUSH_RESPONSE_FAIL = '2';

	
	public static final int DB_SLEEP_TIME = 5000; // 5 sec 
	public static final int DB_SLEEP_WAKEUP = 10; // 0.01 sec
	public static final int DB_PAGE_COUNT = 100;
	
	public static final String FCM_URL="https://fcm.googleapis.com/fcm/send";
	//public static final String FirebaseCloudMessageToken="key=AAAAJrPHEkY:APA91bFTZHtM7Hmc5vL9dPOsESOPw-1t824Q5ksAlu57X6J3Qd1GWdBJhywspxechZblOCbBH2ohsIceGCApC6EbE6qpzueO4agTdPNO356J0FndpA0eiZmOT3RRMrcCspzwe3xhBypr";

	
	public boolean bEndFlag = false; 
	
	public void setEndFlag() {
		bEndFlag = true;
	}
	
	@Async
	public void startPushMessage(){
		logger.info("* Push Service run ---------------- ");
	
		try{
			Thread.sleep(1000);
			logger.info("* start push message implimentation");
			
			boolean bFirst = true;
			int nCount = 0;
			int nSubLoop = 1;
			
			while(true){				
				if(bFirst){
					nCount = getEventAndSendMessage(DB_EVENT_PUSH_READ);
					bFirst = false;
				}
				else nCount = getEventAndSendMessage(DB_EVENT_PUSH_NONE);
							
				for(int i=0; i<1000; i++){
					if(nCount>0) nSubLoop = 1;	// 1초마다	
					else		 nSubLoop = 15;	// 15초마다
					
					for(int j=0; j<nSubLoop;j++){
						if(bEndFlag){
							logger.info(" - end recv thread");
							return;
						}
						Thread.sleep(1);
					}
				}
						
			}
	
		}catch(Exception e) {
			logger.error( e.toString());				
		}
	}
	
	public int getEventAndSendMessage(String type){
		int retValue=0;
		
		try{
			String send_type = null;
			String pk_code = null;
			String tmp_sub_code = null;
			String strJson = null;
		
			List<EventHistVO> vo = appMapper.getEventList(type, null, 0, DB_PAGE_COUNT);
			for(EventHistVO subVo : vo){
				
				retValue ++;
				PushMessageVO pvo = new PushMessageVO();

				logger.debug(" - read event : " + subVo.toString());
				
				send_type = "";
				pk_code = "" + subVo.getEvent_seq();
				tmp_sub_code = subVo.getEvent_ltid();
				
				String [] splite_sub_code = tmp_sub_code.split(",");
				List<String> sub_code = new ArrayList<String>();
				
				for(int i=0; i<splite_sub_code.length ; i++) sub_code.add(splite_sub_code[i].toString().trim());
				
				if(subVo.getEvent_type() == eTYPE_EVENT.TYPE_EVENT_LTID.getValue()){
					logger.debug("event type : LTID, status : "+subVo.getEvent_status());
					
					if(subVo.getEvent_status() == eTYPE_EVENT_STATUS.STATUS_ADD.getValue())
						send_type = eTYPE_PUSH_SEND.LTID_ADD.getValue();
					else if(subVo.getEvent_status() == eTYPE_EVENT_STATUS.STATUS_ACTIVATE.getValue())
						send_type = eTYPE_PUSH_SEND.LTID_ATIVATE.getValue();
					else if(subVo.getEvent_status() == eTYPE_EVENT_STATUS.STATUS_DEACTIVATE.getValue())
						send_type = eTYPE_PUSH_SEND.LTID_DEACTIVATE.getValue();
					else if(subVo.getEvent_status() == eTYPE_EVENT_STATUS.STATUS_DELETE.getValue())
						send_type = eTYPE_PUSH_SEND.LTID_DEL.getValue();
					else if(subVo.getEvent_status() == eTYPE_EVENT_STATUS.STATUS_MODIFY.getValue())
						send_type = eTYPE_PUSH_SEND.LTID_MODIFY.getValue();
					else if(subVo.getEvent_status() == eTYPE_EVENT_STATUS.STATUS_FAIL.getValue())
						send_type = eTYPE_PUSH_SEND.LTID_FAIL.getValue();
					else if(subVo.getEvent_status() == eTYPE_EVENT_STATUS.STATUS_FAILBACK.getValue())
						send_type = eTYPE_PUSH_SEND.LTID_FAILBACK.getValue();
					else {
						logger.info("not support ltid status : seq["+subVo.getEvent_seq()+"],status : "+subVo.getEvent_status());
						appMapper.setEventList(eTYPE_PUSH.READ_STATUS_FAIL.getValue(), subVo.getEvent_seq());
						continue;
					}
						
				}
				else if(subVo.getEvent_type() == eTYPE_EVENT.TYPE_EVENT_GROUP.getValue()){
					logger.debug("event type : GROUP, status : "+subVo.getEvent_status());
					if(subVo.getEvent_status() == eTYPE_EVENT_STATUS.STATUS_LTID_GROUP_ADD.getValue())
						send_type = eTYPE_PUSH_SEND.GROUP_ADD.getValue();
					else if(subVo.getEvent_status() == eTYPE_EVENT_STATUS.STATUS_LTID_GROUP_DELETE.getValue())
						send_type = eTYPE_PUSH_SEND.GROUP_DEL.getValue();
					else {
						logger.info("not support group status : seq["+subVo.getEvent_seq()+"],status : "+subVo.getEvent_status());
						appMapper.setEventList(eTYPE_PUSH.READ_STATUS_FAIL.getValue(), subVo.getEvent_seq());
						continue;
					}
				}
				else if(subVo.getEvent_type() == eTYPE_EVENT.TYPE_EVENT_ALARM.getValue()){
					logger.debug("event type : ALARM, status : "+subVo.getEvent_status());
					pk_code = ""+subVo.getEvent_seq();
					send_type = eTYPE_PUSH_SEND.SET_ALARM.getValue();
				}
				else{
					logger.info("not support type : seq["+subVo.getEvent_seq()+"],type : "+subVo.getEvent_type());
					appMapper.setEventList(eTYPE_PUSH.READ_STATUS_FAIL.getValue(), subVo.getEvent_seq());
					continue;
				}
				
				pvo.setPushMessageVo(send_type, pk_code, sub_code, 
						subVo.getEvent_title(), subVo.getEvent_detail(), subVo.getEvent_time());
				
				List<String> memberList = appMapper.getMemberToken();
				
				if(memberList.size()==0){
					logger.info("don't have usefull member's token");
					appMapper.setEventList(eTYPE_PUSH.READ_STATUS_FAIL.getValue(), subVo.getEvent_seq());
					continue;
				}
				
				logger.info(pvo.toString());
				if((strJson = getObjectToJsonStr(memberList, pvo)) == null){
					logger.error("getObjectToJsonStr fail.. \n");
					appMapper.setEventList(eTYPE_PUSH.READ_STATUS_FAIL.getValue(), subVo.getEvent_seq());
					continue;
				}
				
				// 해당 push 메시지를 읽었는지 표시한다.
				appMapper.setEventList(eTYPE_PUSH.READ_STATUS_READ.getValue(), subVo.getEvent_seq());
				sendPushMessage(subVo.getEvent_seq(), subVo.getEvent_ltid(), strJson);
			}
		}catch(Exception e){
			logger.error( e.toString());
			return -1;
		}
		
		return retValue;
	}
	
	
	public String getObjectToJsonStr(List<String> token, PushMessageVO data){
		
		try {	
			ObjectMapper mapperResponse =  new ObjectMapper();
			Map<String, Object> mapResponse = new HashMap<String, Object>();
			
			mapResponse.put("registration_ids", token);
			mapResponse.put("data", data);
			
			return mapperResponse.writerWithDefaultPrettyPrinter().writeValueAsString(mapResponse);
		
		}catch(JsonGenerationException e) {
			logger.error( e.toString());
		}catch(JsonMappingException e) {
			logger.error( e.toString());
		}catch(IOException e){
			logger.error( e.toString());
		}
		return null;
	}
	
	@Async 
	public void sendPushMessage(int seq, String pk_code, String strData){
		
		try{
			RestTemplate restTemplate = new RestTemplate();
		 	 
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=UTF-8");
			headers.add("Authorization", "key="+bpointProperties.getPush_token().trim());
		 
			HttpEntity<?> request = new HttpEntity<>(strData, headers);
		 
			logger.debug("send url  : " + FCM_URL);
			logger.debug("header    : " + headers.toString().trim());
			logger.debug("json body : " + strData);
		 
			ResponseEntity<String> responseEntity = restTemplate.exchange(FCM_URL, 
	 			 		HttpMethod.POST, request, String.class);

			logger.info(" - seq : " + seq + " , pk_code : " + 
					pk_code + ", response : " + responseEntity.getBody());
			
			ObjectMapper mapperBody = new ObjectMapper();
			Map<String, Object> map = mapperBody.readValue(responseEntity.getBody(), 
				new TypeReference<Map<String, Object>>(){});
	 	
			int nSuccess = 0;
			int nFail = 0;
			int nResult = 0;
	
			if( map.get("success") != null )
				nSuccess = (int) map.get("success");
			if( map.get("failure") != null )
				nFail = (int)map.get("failure");
			
			
			String strTmp = (String)responseEntity.getBody().replaceAll("\"", "\'");

			if(nSuccess==0)
				nResult = appMapper.setPushResult( DB_PUSH_RESPONSE_FAIL, 
						strTmp, nSuccess, nFail, seq);
			else
				nResult = appMapper.setPushResult( DB_PUSH_RESPONSE_SUCCESS, 
						strTmp,	nSuccess, nFail, seq);
			
			logger.debug("db result : " + nResult + ", response succ : " + nSuccess + ", fail : " + nFail);
			
		}catch(Exception e){
			logger.error("sendPushMessage send exception : " + e.toString());
		}
	}
}
