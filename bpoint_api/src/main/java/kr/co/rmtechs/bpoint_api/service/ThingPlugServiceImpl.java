package kr.co.rmtechs.bpoint_api.service;

import java.io.StringReader;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import kr.co.rmtechs.bpoint_api.config.BpointProperties;
import kr.co.rmtechs.bpoint_domain.eTHINGPLUG_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT_STATUS_CHAR;
import kr.co.rmtechs.bpoint_domain.eTYPE_SUBSCRIPTION_EVENT_TYPE;
import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.BpointContentVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointVO;


@Service
public class ThingPlugServiceImpl {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final int THINGPLUG_MAX_STEP = 5; 
	
	private static final String THINGPLUG_BASE_URL = "thingplugpf.sktiot.com:9000/";
	
	private static final String THINGPLUG_VER	= "v1_0";
	private static final String THINGPLUG_NODE_URI = "node";
	private static final String THINGPLUG_REMOTECSE_URI = "remoteCSE";
	private static final String THINGPLUG_CONTAINER_URI = "container-LoRa";
	private static final String THINGPLUG_CONTAINER_LATEST_URI = "latest";
	private static final String THINGPLUG_SUBSCRIPTION_URI = "subscription";
	private static final String THINGPLUG_LOCALE = "en";
	private static final String THINGPLUG_CONTENT_TYPE = "application/vnd.onem2m-res+xml;ty=23";

	private static final int DB_RESPONSE_SIZE = 2000;  
	
	@Autowired 
	BpointDomainMapper appMapper;
	
	@Autowired
	BpointProperties	bpointProperties;
		
	public boolean bEndFlag = false; 
	
	public void setEndFlag() {
		bEndFlag = true;
	}
	
	@Async
	public void startThingPlug(){
		logger.info("* Thinplug i/f Service run ---------------- ");
	
		try{
			Thread.sleep(1000);
			logger.info("* start thingplug interface implimentation");
			
			int nCount = 0;
			int nSubLoop = 1;
			
			while(true){		

				nCount = GetLtidForThingPlug();
							
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
	
	public int  GetLtidForThingPlug(){
		int remote_count = 0;
		int node_count = 0;
		int container_count=0;
		int subscription_count=0;
		
		try{
			
			List<String> vo = appMapper.getLtidForThingPlugNode(bpointProperties);
			logger.info("+ Thingplug request Request Node Count : " + vo.size());
			
			
			for(String deb_eui : vo){	
				logger.info("thingplug need node dev_eui : " + deb_eui);
				
				appMapper.setThingplugResponse(deb_eui, eTHINGPLUG_STATUS.READ_NODE.getValue(), 
						eTHINGPLUG_STATUS.READ_NODE.getValue(),	eTHINGPLUG_STATUS.NOT_REGISTED.getValue(), null);
				
				SendThingPlugFlow(deb_eui, eTHINGPLUG_STATUS.NODE);
				node_count++;
			}
			
			vo = appMapper.getLtidForThingPlugRemoteCES(bpointProperties);
			logger.info("+ Thingplug RemoteCES Count : " + vo.size());
			for(String deb_eui : vo){	
				logger.info("thingplug need remoteces dev_eui : " + deb_eui);
				appMapper.setThingplugResponse(deb_eui, eTHINGPLUG_STATUS.READ_REMOTE_CSE.getValue(), 
						eTHINGPLUG_STATUS.READ_REMOTE_CSE.getValue(),
						eTHINGPLUG_STATUS.NOT_REGISTED.getValue(), null);
				
				SendThingPlugFlow(deb_eui, eTHINGPLUG_STATUS.REMOTE_CSE);
				remote_count++;
			}
			
			vo = appMapper.getLtidForThingPlugContainer(bpointProperties);
			logger.info("+ Thingplug Container Count : " + vo.size());
			for(String deb_eui : vo){	
				logger.info("thingplug need container latest dev_eui : " + deb_eui);
				appMapper.setThingplugResponse(deb_eui, eTHINGPLUG_STATUS.READ_CONTAINER_LORA.getValue(), 
						eTHINGPLUG_STATUS.READ_CONTAINER_LORA.getValue(),
						eTHINGPLUG_STATUS.NOT_REGISTED.getValue(), null);
				
				SendThingPlugFlow(deb_eui, eTHINGPLUG_STATUS.CONTAINER_LORA);
				container_count++;
			}

			
			vo = appMapper.getLtidForThingPlugSubscription(bpointProperties);
			logger.info("+ Thingplug Subscription Count : " + vo.size());
			for(String deb_eui : vo){	
				logger.info("thingplug need subscrption dev_eui : " + deb_eui);
				appMapper.setThingplugResponse(deb_eui, eTHINGPLUG_STATUS.READ_SUBSCRIPTION.getValue(), 
						eTHINGPLUG_STATUS.READ_SUBSCRIPTION.getValue(),
						eTHINGPLUG_STATUS.NOT_REGISTED.getValue(), null);
				
				SendThingPlugFlow(deb_eui, eTHINGPLUG_STATUS.SUBSCRIPTION_ACTIVE);
				subscription_count ++;
			}


		}catch(Exception e){
			logger.error(e.toString());
		}
		
		return (node_count + remote_count + container_count + subscription_count);
	}
	
	private URI getThingPlugUri(eTHINGPLUG_STATUS status, String ltid){
		URI url = null;
		
		try{
		switch(status){
		case NODE:
			url = URI.create("http://" + THINGPLUG_BASE_URL + bpointProperties.getApp_eui() + "/" + THINGPLUG_VER + "/" 
					+ THINGPLUG_NODE_URI + "-" + bpointProperties.ConvertDeveuiToLtid(ltid));		
			break;
		case REMOTE_CSE:
			url = URI.create("http://" + THINGPLUG_BASE_URL + bpointProperties.getApp_eui() + "/" + THINGPLUG_VER + "/" 
					+ THINGPLUG_REMOTECSE_URI + "-" + bpointProperties.ConvertDeveuiToLtid(ltid));
			break;
		case CONTAINER_LORA:
			url = URI.create("http://" + THINGPLUG_BASE_URL + bpointProperties.getApp_eui() + "/" + THINGPLUG_VER + "/" 
					+ THINGPLUG_REMOTECSE_URI + "-" +  bpointProperties.ConvertDeveuiToLtid(ltid)
					+ "/" + THINGPLUG_CONTAINER_URI	+ "/" + THINGPLUG_CONTAINER_LATEST_URI );	
			break;
		case SUBSCRIPTION_RETRIEVE:
			url = URI.create("http://" + THINGPLUG_BASE_URL + bpointProperties.getApp_eui() + "/" + THINGPLUG_VER + "/" 
					+ THINGPLUG_REMOTECSE_URI + "-" +  bpointProperties.ConvertDeveuiToLtid(ltid) 
					+ "/" + THINGPLUG_CONTAINER_URI	+ "/" + THINGPLUG_SUBSCRIPTION_URI + "-"  + bpointProperties.getSubscription_name());
			break;
		case SUBSCRIPTION_ACTIVE:
			url = URI.create("http://" + THINGPLUG_BASE_URL + bpointProperties.getApp_eui() + "/" + THINGPLUG_VER + "/" 
					+ THINGPLUG_REMOTECSE_URI + "-" +  bpointProperties.ConvertDeveuiToLtid(ltid) 
					+ "/" + THINGPLUG_CONTAINER_URI);
			break;
		case SUBSCRIPTION_UPDATE:
			url = URI.create("http://" + THINGPLUG_BASE_URL + bpointProperties.getApp_eui() + "/" + THINGPLUG_VER + "/" 
					+ THINGPLUG_REMOTECSE_URI + "-" +  bpointProperties.ConvertDeveuiToLtid(ltid) 
					+ "/" + THINGPLUG_CONTAINER_URI	+ "/" + THINGPLUG_SUBSCRIPTION_URI + "-"  + bpointProperties.getSubscription_name());			
			break;			
		case SUBSCRIPTION_DEACTIVE:
			url = URI.create("http://" + THINGPLUG_BASE_URL + bpointProperties.getApp_eui() + "/" + THINGPLUG_VER + "/" 
					+ THINGPLUG_REMOTECSE_URI + "-" +  bpointProperties.ConvertDeveuiToLtid(ltid) 
					+ "/" + THINGPLUG_CONTAINER_URI	+ "/" + THINGPLUG_SUBSCRIPTION_URI + "-"  + bpointProperties.getSubscription_name());
			break;
			
		default :
		}
		}catch(Exception e){
			logger.debug("getThingPlugUri error : " +e.toString());		
		}

		return url;
	}

	@Async
	private void SendThingPlugFlow(String dev_eui, eTHINGPLUG_STATUS status){
		
		boolean bResult = true;
		eTHINGPLUG_STATUS tmp_status = status;
		eTHINGPLUG_STATUS tmp_db_status = status;
		
		for(int i=0; i<THINGPLUG_MAX_STEP; i++) {
	
			Map<String, Object> retMap = SendThingPlug_Server(dev_eui, tmp_status, tmp_db_status, "response", bResult);
			bResult = (boolean)retMap.get("success");
			
			switch(tmp_status){
			case NODE:
				if(bResult == false){ 
					logger.debug(" - stop node deb_eui : " + dev_eui);
					return;
				}
				tmp_db_status = tmp_status = eTHINGPLUG_STATUS.REMOTE_CSE;
				break;
			case REMOTE_CSE:
				if(bResult == false) {
					logger.debug(" - stop remote_cse deb_eui : " + dev_eui);
					return;
				}
				tmp_db_status = tmp_status = eTHINGPLUG_STATUS.CONTAINER_LORA;
				break;
			case CONTAINER_LORA:	
				if(bResult == false){
					logger.debug(" - stop container deb_eui : " + dev_eui);
				}
				tmp_status = tmp_db_status = eTHINGPLUG_STATUS.SUBSCRIPTION_RETRIEVE;
				break;
			case SUBSCRIPTION_RETRIEVE:
				if(bResult == true){
					logger.debug(" - stop subscription_retrieve deb_eui : " + dev_eui);
					tmp_status = eTHINGPLUG_STATUS.SUBSCRIPTION_UPDATE;
					tmp_db_status = eTHINGPLUG_STATUS.STATUS_FINISHED;					
				}
				else
				{
					tmp_status = eTHINGPLUG_STATUS.SUBSCRIPTION_ACTIVE;
					tmp_db_status = eTHINGPLUG_STATUS.STATUS_FINISHED;
				}
				break;
				
			case SUBSCRIPTION_UPDATE:
				if(bResult == false) logger.debug(" - stop subscription_update deb_eui : " + dev_eui);
				return;				
			case SUBSCRIPTION_ACTIVE:
				if(bResult == false) logger.debug(" - stop subscription active deb_eui : " + dev_eui);
				return;
			default:
				logger.info(" not support thingplug status : " + tmp_status.getValue());
				return;
			}
		}
		
	}
	
	private String GetSubscriptionBody(char notiType, String ltid, String url){
		
		String strTmp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<m2m:sub\n"
				+ "xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n"
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
				+ "<enc>\n"
				+ "  <rss>"+notiType +"</rss>\n"
				+ "</enc>\n"
				+ "<nu>HTTP|"+url+ltid+"</nu>"
				+ "<nct>2</nct>\n"
				+ "</m2m:sub>";
		
		return strTmp;
	}
	
	
	private boolean SetContentsLastInfo(eTHINGPLUG_STATUS thingplug_status, String dev_eui, Map<String, String> map, 
			String msg, boolean master_insert, String event_tag, boolean fail_clear){
		try{
			
			String content = (String)map.get("con");			
			String lt = (String)map.get("lt");					
			int size = Integer.parseInt((String)map.get("cs"));	
			String property = (String)map.get("ppt");			
			String statetag = (String)map.get("st");			
		
			if( (size==BpointContentVO.MAX_CONTENT_SIZE) && (content != null) 
					&& (content.length()==BpointContentVO.MAX_CONTENT_SIZE)) {
				
				BpointVO vo = appMapper.getLtid(dev_eui);
				BpointContentVO contentVO = new BpointContentVO(dev_eui, content, lt, property, statetag, fail_clear);
				
				logger.info("response(content) : " + thingplug_status.getName()+",dev_eui : " + dev_eui 
				+ ",size : " + size + ", content:"+content+",lt:"+lt+",vo:"+contentVO.toString());
				
				if((master_insert==true) || (vo.getBpoint_base_event_time() == null)){
					int nCount = appMapper.setBpointBaseContent(contentVO);	

					if(nCount>0){
						String title = event_tag + ", dev_eui :" +dev_eui + "를 수신하였습니다.";
						String detail = event_tag + ", dev_eui : " +dev_eui + ", 가속도 x:"+ contentVO.getBpoint_base_x()
							+ ", y:"+contentVO.getBpoint_base_y() + ", z:" + contentVO.getBpoint_base_z();

						String strMsg = MakeResponse.InsertEventAndHistory(appMapper, eTYPE_EVENT.TYPE_EVENT_LTID,
								dev_eui, eTYPE_EVENT_STATUS.STATUS_MODIFY, title, detail, "system",
								thingplug_status, event_tag + "," + msg + "를 정상으로 수신하였습니다.",
								contentVO.getBpoint_degrees(), contentVO.getBpoint_humidity(),
								contentVO.getBpoint_base_x(), contentVO.getBpoint_base_y(),
								contentVO.getBpoint_base_z(),
								contentVO.getBpoint_base_battery(), contentVO.getBpoint_base_signal(),
								contentVO.getBpoint_base_statetag(),
								contentVO.getBpoint_base_event_time());
						
						if(strMsg!=null) logger.warn("SetBpointHistory insert fail : " + strMsg);
					}
					else{
						String strMsg = MakeResponse.InsertBpointHistory(appMapper, dev_eui, thingplug_status,
								event_tag + "," + msg + "를 정상으로 수신하였으나, 가속도의 기본정보를 저장하지 못하였습니다.",
								contentVO.getBpoint_degrees(), contentVO.getBpoint_humidity(),
								contentVO.getBpoint_base_x(), contentVO.getBpoint_base_y(),
								contentVO.getBpoint_base_z(),
								contentVO.getBpoint_base_battery(), contentVO.getBpoint_base_signal(),
								contentVO.getBpoint_base_statetag(),
								contentVO.getBpoint_base_event_time());
						if(strMsg!=null) logger.warn("SetBpointHistory insert fail : " + strMsg);
					}
				}else{ 
					String strMsg = MakeResponse.InsertBpointHistory(appMapper, dev_eui, thingplug_status,
							event_tag + "," + msg + "를 정상으로 수신하였으나, 기본 정보가 존재하여 수신데이터를 저장하지 않았습니다.",
							contentVO.getBpoint_degrees(), contentVO.getBpoint_humidity(),
							contentVO.getBpoint_base_x(), contentVO.getBpoint_base_y(),
							contentVO.getBpoint_base_z(),
							contentVO.getBpoint_base_battery(), contentVO.getBpoint_base_signal(),
							contentVO.getBpoint_base_statetag(),
							contentVO.getBpoint_base_event_time());
					if(strMsg!=null) logger.warn("SetBpointHistory insert fail : " + strMsg);			
				}
			} else{ 
				logger.warn("response(content, 컨텐츠 비정상) : " + thingplug_status.getName()+",deb_eui : " + bpointProperties.ConvertDeveuiToLtid(dev_eui) 
				+ ",size : " + size + ", content:"+content+",lt:"+lt+ "," + event_tag );
				
				String strMsg = MakeResponse.InsertBpointHistory(appMapper, dev_eui,	thingplug_status,
						event_tag + "," + msg + " 수신 데이터가 비정상입니다.");
				if(strMsg!=null) logger.warn("SetBpointHistory insert fail : " + strMsg);
			}
			return true;
		}catch(Exception e){
			logger.error("response(content) : " + thingplug_status.getName()+",deb_eui : " 
						+ bpointProperties.ConvertDeveuiToLtid(dev_eui) + ","+e.toString());  
		}
		return false;
	}
	
	Map<String, Object> SendThingPlug_App(String dev_eui, eTHINGPLUG_STATUS thingplug_status, eTHINGPLUG_STATUS db_status, 
			String event_tag, String id, boolean fail_clear){
		return SendThingPlug(dev_eui, thingplug_status, db_status, 
				false, false, true, false, 
				event_tag, id, fail_clear);
	}
	
	Map<String, Object> SendThingPlug_Server(String dev_eui, eTHINGPLUG_STATUS thingplug_status, eTHINGPLUG_STATUS db_status, 
			String event_tag, boolean before_status){
		return SendThingPlug(dev_eui, thingplug_status, db_status, 
				before_status, true, false, true, 
				event_tag, "system", false);
	}

	Map<String, Object> SendThingPlug(String dev_eui, eTHINGPLUG_STATUS thingplug_status, eTHINGPLUG_STATUS db_status, 
			boolean before_status, boolean thingplug_db_update, boolean master_insert, boolean active_flag, 
			String event_tag, String id, boolean fail_clear){
		ResponseEntity<String> responseEntity = null;
		int response_code = 0;
		String response_body = null;
		String strMsg = null ;
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		try{
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", "application/xml; charset=UTF-8");
			headers.add("X-M2M-RI", bpointProperties.ConvertDeveuiToLtid(dev_eui));
			headers.add("X-M2M-Origin", bpointProperties.ConvertDeveuiToLtid(dev_eui));
			headers.add("uKey", bpointProperties.getUkey().trim());
			
			URI url = getThingPlugUri(thingplug_status, dev_eui);
			
			if(url == null){
				

				logger.debug("url fail not support status " + thingplug_status.getName() + "-------------------------------");
			}
			
			retMap.put("url", url.toString());
			retMap.put("header", headers.toString());

			if( (thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_ACTIVE) || 
				(thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_UPDATE)){
				headers.add("X-M2M-NM", bpointProperties.getSubscription_name());
				headers.add("locale", THINGPLUG_LOCALE);
				headers.add("Content-Type", THINGPLUG_CONTENT_TYPE);
				String body = GetSubscriptionBody(eTYPE_SUBSCRIPTION_EVENT_TYPE.CHILD_CREATED.getValue(), 
						bpointProperties.ConvertDeveuiToLtid(dev_eui), bpointProperties.getPush_url());
				
				retMap.put("body", body);
				HttpEntity<?> request = new HttpEntity<>(body, headers);	 
				
logger.debug("url : " + url.toString());
logger.debug("headers [" + headers.toString() +"]");			
logger.debug("body : " + body);

				if(thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_ACTIVE)
					responseEntity = restTemplate.exchange(url.toString(), 
		 			 		HttpMethod.POST, request, String.class);
				else
					responseEntity = restTemplate.exchange(url.toString(), 
		 			 		HttpMethod.PUT, request, String.class);
			}else{
				
				RequestEntity<Void> request = null;
			
logger.debug("url : " + url.toString());
logger.debug("headers [" + headers.toString() +"]");			
			
				if(thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_DEACTIVE){	
					request = new RequestEntity<Void>(headers, HttpMethod.DELETE, url);
				}else{
					request = new RequestEntity<Void>(headers, HttpMethod.GET, url);
				}				
				 
				logger.info("request : " + thingplug_status.getName()+",deb_eui : " +  dev_eui 
						+ ",X-M2M-RI:"+bpointProperties.ConvertDeveuiToLtid(dev_eui) + ",url:" + url.toString());
				
				responseEntity = restTemplate.exchange(request, String.class);				
			}
		
			logger.debug("status code " + responseEntity.getStatusCode());
			logger.debug("status code " + responseEntity.getHeaders().toString());
			logger.debug(responseEntity.toString());
			
			Map<String, String> map = null;
			
			if(responseEntity!=null)	{
				retMap.put("request_body", responseEntity.getBody());
				retMap.put("code", "" + responseEntity.getStatusCode().toString());
			}
			
			if(	(thingplug_status == eTHINGPLUG_STATUS.CONTAINER_LORA) 
				&& (responseEntity!=null)){
				
				map = parserXmlToStr(responseEntity.getBody());
				if(map.size()>0)	logger.debug(map.toString());
			}
			
			response_code = responseEntity.getStatusCodeValue();
			

			if( (responseEntity.getStatusCode() == HttpStatus.OK) ||
				(responseEntity.getStatusCode() == HttpStatus.CREATED)){
					
				response_body = responseEntity.toString().replaceAll("\"", "\'");
				retMap.put("body", response_body);
				
				if(response_body.length()>DB_RESPONSE_SIZE)
					response_body = response_body.substring(0, DB_RESPONSE_SIZE);
											

				if(thingplug_db_update == true){ 
					if(appMapper.setThingplugResponse(dev_eui, thingplug_status.getValue(),
						db_status.getValue(),	responseEntity.getStatusCodeValue(), response_body) == 0){
						logger.error("response(rm_bpoint_thingplug_map update fail) : " + thingplug_status.getName()+",deb_eui : " 
						+ dev_eui + ",response_code : " + response_code);
					}
					else {
						logger.info("response(rm_bpoint_thingplug_map update success) : " + thingplug_status.getName()+",deb_eui : " 
							+ dev_eui + ",response_code : " + response_code);
					}	
				}
				
				if(thingplug_status == eTHINGPLUG_STATUS.CONTAINER_LORA ){
					SetContentsLastInfo(thingplug_status, dev_eui, map, "contaner-Lor/latest", master_insert, event_tag, fail_clear);
				}
				else{
					

					strMsg = MakeResponse.InsertBpointHistory(appMapper, dev_eui,	thingplug_status,
						"response[" + thingplug_status.getName() + "], deb_eui : " + dev_eui
						+ "를 정상 수신하였습니다.");
				
					if(strMsg!=null) logger.warn("SetBpointHistory insert fail : " + strMsg);
					
					if( (active_flag == true) &&
						((thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_UPDATE) ||
						 (thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_ACTIVE) ||
						 (thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_DEACTIVE ))){
						
						BpointVO vo = appMapper.getLtid(dev_eui);
						if(vo!=null){
							String title = null;
							String detail = null;

							if( (thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_ACTIVE ||
								 thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_UPDATE ) &&
								(vo.getBpoint_status() == eTYPE_EVENT_STATUS_CHAR.STATUS_ADD_CHAR.getValue() ||
								 vo.getBpoint_status() == eTYPE_EVENT_STATUS_CHAR.STATUS_ACTIVATE_CHAR.getValue()) ){
								vo.setBpoint_status(eTYPE_EVENT_STATUS_CHAR.STATUS_ACTIVATE_CHAR.getValue());
								if( appMapper.appUpdateBpointStatus(vo) == 0)
									if(thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_ACTIVE )
										logger.warn("response subscription_active 저장실패 dev_eui : " + dev_eui +
												",status: "+ eTYPE_EVENT_STATUS_CHAR.STATUS_ACTIVATE_CHAR.getName());
									else
										logger.warn("response subscription_update 저장실패 dev_eui : " + dev_eui +
												",status: "+ eTYPE_EVENT_STATUS_CHAR.STATUS_ACTIVATE_CHAR.getName());
								else{
									title  = "response[" + thingplug_status.getName() + "], deb_eui : " + dev_eui + ", 활성화";
									detail = "response[" + thingplug_status.getName() + "], deb_eui : " + dev_eui
											+ "를 정상 수신 후 상태를 활성화 하였습니다.";
									
									strMsg=MakeResponse.InsertEventHist(appMapper, eTYPE_EVENT.TYPE_EVENT_LTID, dev_eui,
											eTYPE_EVENT_STATUS.STATUS_ACTIVATE, title, detail, "system");
									if(strMsg!=null) logger.warn(detail + ","+strMsg);
								}
							}
							else if( (thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_DEACTIVE) &&
									(vo.getBpoint_status() ==eTYPE_EVENT_STATUS_CHAR.STATUS_ACTIVATE_CHAR.getValue())){
								vo.setBpoint_status(eTYPE_EVENT_STATUS_CHAR.STATUS_DEACTIVATE_CHAR.getValue());
								if( appMapper.appUpdateBpointStatus(vo) == 0)
									logger.warn("response subscription_deactive 저장실패 dev_eui : " + dev_eui +
											",status: "+ eTYPE_EVENT_STATUS_CHAR.STATUS_DEACTIVATE_CHAR.getName());
								else{
									title  = "response[" + thingplug_status.getName() + "], deb_eui : " + dev_eui + " 비활성화";
									detail = "response[" + thingplug_status.getName() + "], deb_eui : " + dev_eui
											+ "를 정상 수신 후 상태를 비활성화를 하였습니다.";
									strMsg=MakeResponse.InsertEventHist(appMapper, eTYPE_EVENT.TYPE_EVENT_LTID, dev_eui, 
											eTYPE_EVENT_STATUS.STATUS_DEACTIVATE, title, detail, "system");
									if(strMsg!=null) logger.warn(detail + ","+strMsg);
								}
							}
						}
					}
				}
				
				retMap.put("success", true);
				return retMap;

			}else{  
				logger.info("response(fail) : " + thingplug_status.getName()+",deb_eui : " + dev_eui 
						+ ",response_code : " + response_code);
				
				strMsg = MakeResponse.InsertBpointHistory(appMapper, dev_eui,	thingplug_status,
						"response[" + thingplug_status.getName() + "], deb_eui : " + dev_eui
						+ "를 비정상 [response_code : " + response_code+ "] 수신하였습니다.");
				if(strMsg!=null){
					logger.warn("SetBpointHistory insert fail : " + strMsg);
					retMap.put("sub_msg", strMsg);
				}
			}
			 
		}catch(HttpClientErrorException e){
			HttpStatus httpStatus = e.getStatusCode();
			response_code = httpStatus.value();

			retMap.put("code", "" + e.getStatusCode().toString());
			retMap.put("err_msg", e.toString());
			
			String strTmp = "response(http fail)[";
			
			if( (thingplug_status == eTHINGPLUG_STATUS.SUBSCRIPTION_RETRIEVE) &&
				(httpStatus == HttpStatus.NOT_FOUND)){
					logger.warn("response(don't have subscription) : " + thingplug_status.getName()+",deb_eui : " + dev_eui 
							+ ",response_code : " + response_code + ",HttpClientErrorException : "+ e.toString());
					strTmp = "response(don't have subscription)[";
			}else{
				logger.error("response(fail) : " + thingplug_status.getName()+",deb_eui : " + dev_eui 
						+ ",response_code : " + response_code + ",HttpClientErrorException : "+ e.toString());
			}
			
			strMsg = MakeResponse.InsertBpointHistory(appMapper, dev_eui,	thingplug_status,
					strTmp + thingplug_status.getName() + "], deb_eui : " + dev_eui
					+ "를 비정상 [response_code : " + response_code+ "] 수신하였습니다.");

			if(strMsg!=null) {
				logger.warn("SetBpointHistory insert fail : " + strMsg);
				retMap.put("sub_msg", strMsg);
			}

		}catch(Exception e){
			logger.error("response(fail) : " + thingplug_status.getName()+",deb_eui : " + dev_eui
			+ ",response_code : " + response_code + ",Exception : "+ e.toString());
			
			retMap.put("err_msg", e.toString());
			strMsg = MakeResponse.InsertBpointHistory(appMapper, dev_eui,	thingplug_status,
				"response(system error)[" + thingplug_status.getName() + "], deb_eui : " + dev_eui
				+ " history table에 저장을 실패하였습니다.");
			if(strMsg!=null){
				logger.warn("SetBpointHistory insert fail : " + strMsg);
				retMap.put("sub_msg", e.toString());
			}
		}

		if(thingplug_db_update == true){ 
			SetThingplugResponse(dev_eui, thingplug_status.getValue(), thingplug_status.getValue(), response_code, response_body);
		}
		
		retMap.put("success", false);
		return retMap;
	}
	
	private void SetThingplugResponse(String ltid, int thingplug_status, int db_status, int response_code, String body){
		try{
			if( appMapper.setThingplugResponse(ltid, thingplug_status, db_status, response_code, body) == 0)
				logger.error("setThingPlugBindTime errorCode update fail ltid: " + ltid +", response_code : " + response_code );
			else logger.debug("setThingPlugBindTime errorCode update success ltid: " + ltid +", response_code : " + response_code );
		}catch(Exception e){
			logger.error("BindThingPlug db insert exception ltid : "  + ltid + "," + e.toString());
		}
	}
	
	public  Map<String, String> parserXmlToStr(String xml){
		
		StringReader sr = new StringReader(xml);
		InputSource is = new InputSource(sr);
		
		NodeList nodeList = null;
		Map<String,String> map = new HashMap<>();
		
		logger.debug("xml : " + xml);
		
		try{
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			Node node = doc.getDocumentElement();
			
			nodeList = node.getChildNodes();
			
			for(int i=0; i<nodeList.getLength(); i++){
				Node tmpNode = nodeList.item(i);
				logger.debug("xml node name : " + tmpNode.getNodeName() + ", data : " + tmpNode.getTextContent());
				map.put(tmpNode.getNodeName(), tmpNode.getTextContent());
			}
			
		}catch(Exception e){
			logger.error("parserXmlToStr : " + e.toString());
		}

		if(map.size()==0) return null;
		
		logger.debug("parserXmlToStr : " + map.toString());
		return map;
	}
}
