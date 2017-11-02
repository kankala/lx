package kr.co.rmtechs.bpoint_api.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.rmtechs.bpoint_domain.eTHINGPLUG_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT_STATUS_CHAR;
import kr.co.rmtechs.bpoint_domain.eTYPE_USER_RANK;
import kr.co.rmtechs.bpoint_domain.eZIP_CITY_TYPE;
import kr.co.rmtechs.bpoint_domain.eZIP_STATE_TYPE;
import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.BpointBaseVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointHistVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointVO;
import kr.co.rmtechs.bpoint_domain.vo.EventHistVO;
import kr.co.rmtechs.bpoint_domain.vo.MemberVO;

@Service
public class MakeResponse {
		private Logger logger = LoggerFactory.getLogger(this.getClass());
		
		public static final int RET_SUCCESS				= 0;
		public static final int RET_FAIL				= -1;
		public static final int RET_VALUE				= 1;
		
		public static final int RET_BPOINT_BASE			= 1;
		public static final int RET_BPOINT_DETAIL		= 2;
		public static final int RET_BPOINT_ALL		 	= 3;
	// 
		public static final int USER_DEFAULT_GROUP_NO	= 0;
		public static final int USER_DEFAULT_NON_GROUP_NO = 99999;
		
		
	// 1000 시스템 에러
		public static final int ERROR_EXCEPTION			= 1000;
		public static final int ERROR_JSON_PARSING		= 1001;
		public static final int ERROR_JSON_GENERATION	= 1002;
		public static final int ERROR_JSON_MAPPING		= 1003;
		public static final int ERROR_IO_EXCEPTION		= 1004;
		
		public static final int ERROR_QUERY_NO_DATA		= 1100;
		public static final String MSG_QUERY_NO_DATA="code : "+ERROR_QUERY_NO_DATA+",조회되어진 데이터가 존재하지 않습니다.";
		public static final int ERROR_INSERT_FAIL		= 1101;
		public static final int ERROR_UPDATE_FAIL		= 1102;
		public static final int ERROR_DELETE_FAIL		= 1103;
		public static final int ERROR_ALREADY_EXIST		= 1104;
		public static final int ERROR_SELECT_FAIL		= 1300; // insert/update 후 조회 실패시 
		public static final int ERROR_NOT_EXIST_PRIVILEGE = 1400;
		public static final int ERROR_NOT_SUPPORT_DEV_EUI = 1500;
		public static final int ERROR_THINGPLUG_FAIL	= 1900;
		
		//2000 json paramet error
		public static final int ERROR_NOT_DEFINE_IF		= 2001;
		public static final int ERROR_NOT_EXIST_IF		= 2002;
		public static final int ERROR_NO_DATA			= 2003;
		public static final int ERROR_INVALID_VALUE		= 2004; // paramter의 값이 틀린경우
		public static final int ERROR_INVALID_STATUS		= 2005; // paramter의 값과 DB의 상태가 틀린경우.
		public static final int ERROR_STRING_SIZE_WRONG	= 2006;
		public static final int ERROR_STRING_SIZE_OVER	= 2007;
		public static final int ERROR_NOT_EXIST_ID		= 2008;
		public static final int ERROR_DONT_HAVE_GROUP	= 2100;
		public static final int ERROR_DONT_HAVE_GROUP_LTID = 2101;
		public static final int ERROR_DONT_HAVE_ZIPCODE_STATE= 2200;
		
		//2100 app version 
		public static final int ERROR_VERSION_BASE		= 2100;		//get_version
		public static final int ERROR_LOGIN_BASE			= 2200; 	//login, change_pwd 
		public static final int ERROR_GROUP_LIST_BASE	= 2300; 	//group_list
		public static final int ERROR_BPOINT_LIST_BASE	= 2400;		//bpoint_list
		public static final int ERROR_BPOINT_HISTORY_BASE = 2500;	//bpoint_history
		public static final int ERROR_BPOINT_GROUP_BASE = 2600; 	//bpoint_group_modify
		public static final int ERROR_EVENT_LIST_BASE 	= 2700;		//event_list
		public static final int ERROR_ALARM_BASE		= 2800;		//alarm_set, alarm
		
		public static final int DB_SIZE_LTID			= 16;
		public static final int DB_SIZE_LG_LTID			= 15;
		public static final int DB_SIZE_LTID_NAME		= 30;
		public static final int DB_SIZE_ZIPCODE			= 5;
		public static final int DB_SIZE_ZIPCODE_STATE	= 2;
		public static final int DB_SIZE_ZIPCODE_CITY	= 80;
		public static final int DB_SIZE_ZIPCODE_ETC		= 50;
		public static final int DB_SIZE_ID				= 50;
		public static final int DB_SIZE_PWD				= 50;
		public static final int DB_SIZE_NAME			= 20;
		
		public static final int DB_SIZE_EVENT_TITLE		= 50;
		public static final int DB_SIZE_EVENT_DETAIL	= 200;
		
		public static final String DEFAULT_ERROR_STRING = "{\"success\":false, \"message\":\"system error\", \"payload\":\"\"}";
		
		private ObjectMapper mapperResponse = null;
		private Map<String, Object> mapResponse = null;
		
		public MakeResponse(){
			mapperResponse = new ObjectMapper();
			mapResponse = new HashMap<String, Object>();
		}
		
		
		public String MakeFailResponse(int nError, String strMsg, Object payload){
			String strReturn = DEFAULT_ERROR_STRING;
			try {
				mapResponse.put("success", false);

				if(strMsg!=null) strMsg = "[" + strMsg + "] ";
				
				switch(nError)
				{
				case ERROR_EXCEPTION:
					strMsg += "json error";
					break;
				case ERROR_JSON_PARSING:
					strMsg += "json parshing error";
					break;
				case ERROR_JSON_GENERATION:
					strMsg += "json generation error";
					break;
				case ERROR_JSON_MAPPING:
					strMsg += "json mapping error";
					break;
				case ERROR_IO_EXCEPTION:
					strMsg += "io exception error";
					break;
				case ERROR_NOT_DEFINE_IF:
					strMsg += "if parameter가 존재하지 않습니다.";
					break;
				case ERROR_NOT_EXIST_IF:
					strMsg += "존재하지 않는 parameter가 있습니다.";
					break;
				case ERROR_INVALID_VALUE:
					strMsg += "존재하지 않는 parameter의 값이 있습니다.";
					break;
				case ERROR_INVALID_STATUS:
					strMsg += "parameter의 값과 DB의 상태가 상이합니다.";
					break;
				case ERROR_NO_DATA:
					strMsg += "json parameter에 Data가 존재하지 않습니다.";
					break;
				case ERROR_INSERT_FAIL:
					strMsg += "저장에 실패 하였습니다."; 
					break;
				case ERROR_UPDATE_FAIL:
					strMsg += "변경에 실패 하였습니다."; 
					break;
				case ERROR_DELETE_FAIL:
					strMsg += "삭제에 실패 하였습니다."; 
					break;
				case ERROR_STRING_SIZE_OVER:
					strMsg += "문자열의 크기가 DB SIZE 보다 큽니다."; 
					break;
				case ERROR_ALREADY_EXIST:
					strMsg += "이미 데이터가 존재합니다."; 
					break;
				case ERROR_NOT_EXIST_PRIVILEGE:
					strMsg += "권한이 존재하지 않습니다."; 
					break;
				case ERROR_DONT_HAVE_GROUP:
					strMsg += "사용자의 group이 설정되어 있지 않습니다.";
					break;
				case ERROR_DONT_HAVE_GROUP_LTID:
					strMsg += "요청한 DEV_EUI에 대하여 권한이 존재하지 않습니다.";
					break;
				case ERROR_NOT_EXIST_ID:
					strMsg += "존재하지 않는 ID입니다.";
					break; 
				case ERROR_DONT_HAVE_ZIPCODE_STATE:
					strMsg += "존재하지 않는 시/도 코드입니다.";
					break;
				case ERROR_QUERY_NO_DATA:
					strMsg += "데이터가 존재하지 않습니다.";
					break;
				case ERROR_THINGPLUG_FAIL:
					strMsg += "경계점으로 부터 정보를 가져올 수 없습니다.";
					break;
				case ERROR_NOT_SUPPORT_DEV_EUI:
					strMsg += "지원하지 않는 dev_eui 입니다.";
					break;
				default:
					strMsg += " ";
					break;
				}
					
				mapResponse.put("message", strMsg);
				
				if(payload!=null)	mapResponse.put("payload", payload);
				else				mapResponse.put("payload", ""+nError);

				strReturn=mapperResponse.writerWithDefaultPrettyPrinter().writeValueAsString(mapResponse);
				
			}catch(JsonGenerationException e) {
				logger.error(e.toString());
			}catch(JsonMappingException e) {
				logger.error(e.toString());
			}catch(IOException e){
				logger.error(e.toString());
			}
			
			logger.info(strReturn);
			return strReturn;
		}
			
		public String MakeSuccessResponse(int nError, String strMsg, Object payload){
			String strReturn=DEFAULT_ERROR_STRING;
			
			try {	
				mapResponse.put("payload", payload);							
				mapResponse.put("success", true);
				if(strMsg == null)	mapResponse.put("message", "ok");
				else				mapResponse.put("message", strMsg);
				
				strReturn= mapperResponse.writerWithDefaultPrettyPrinter().writeValueAsString(mapResponse);
			
			}catch(JsonGenerationException e) {
				logger.error(e.toString());
			}catch(JsonMappingException e) {
				logger.error(e.toString());
			}catch(IOException e){
				logger.error(e.toString());
			}
			
			logger.info(strReturn);
			
			return strReturn;
		}
		
		public boolean checkZipcodeState(String state){
			logger.debug("checkZipCode : " + state);
			if(state==null || state=="") return false;
			int nState = Integer.parseInt(state);
			
			if( ((nState>=eZIP_CITY_TYPE.SEOUL.getValue()) && 
				 (nState<=eZIP_CITY_TYPE.INCHEON.getValue())) ||
				((nState>=eZIP_STATE_TYPE.GANWON.getValue()) && 
				 (nState<=eZIP_STATE_TYPE.JEJU.getValue())) )
				return true;
			return false;
		}
		
		public void setStatus(BpointBaseVO vo, String status){
			if(status.equals(""+eTYPE_EVENT_STATUS.STATUS_ADD.getValue())==true)
				vo.setBpoint_status(eTYPE_EVENT_STATUS_CHAR.STATUS_ADD_CHAR.getValue());
			else if(status.equals(""+eTYPE_EVENT_STATUS.STATUS_ACTIVATE.getValue())==true)
				vo.setBpoint_status(eTYPE_EVENT_STATUS_CHAR.STATUS_ACTIVATE_CHAR.getValue());
			else if(status.equals(""+eTYPE_EVENT_STATUS.STATUS_DEACTIVATE.getValue())==true)
				vo.setBpoint_status(eTYPE_EVENT_STATUS_CHAR.STATUS_DEACTIVATE_CHAR.getValue());
		}
		
		public boolean isEqualString(String arg1, String arg2){
			if(arg1!=null){
				if(arg2==null) return true;
				else if(arg1.equals(arg2)==false) return true;
			} else {
				if(arg2!=null) return true;
			}
			return false;
		}
		
		//group_no 조회시 .default 값이면 조회가 되지 않기 위해서 임의의 높은값을 return 한다.
		
		public int getGroupNo(BpointDomainMapper appMapper, String id){
			try{
				MemberVO memberVo = appMapper.getUserRank(id);
				if(memberVo!=null){
					logger.debug("rank : "+memberVo.getMember_rank());
				
					if(memberVo.getMember_rank()==eTYPE_USER_RANK.USER_RANK_PART_OP_CHAR.getValue()){
						logger.debug("group : " + memberVo.getMember_group());
						int nMemberGroupNo = memberVo.getMember_group();
						
						if(nMemberGroupNo==USER_DEFAULT_GROUP_NO) nMemberGroupNo=USER_DEFAULT_NON_GROUP_NO;
						
						return nMemberGroupNo;
					}
					return RET_SUCCESS;
				}
			}catch(Exception e){
				logger.error(e.toString());				
			}
			
			return RET_FAIL;
		}
		

		
		public boolean isRankCheck(BpointDomainMapper appMapper, String id, char cRankLevel){		
			try{
				MemberVO memberVo = appMapper.getUserRank(id);
				if(memberVo!=null){
					logger.debug("rank : "+memberVo.getMember_rank());
				
					if(memberVo.getMember_rank()==cRankLevel) return true;
				}
			}catch(Exception e){
				logger.error(e.toString());
			}
			
			return false;
		}
		
		public boolean isAdminRank(BpointDomainMapper appMapper, String id){
			return isRankCheck(appMapper, id, eTYPE_USER_RANK.USER_RANK_ADMIN_CHAR.getValue());
		}
		
		public boolean isPartOpRank(BpointDomainMapper appMapper, String id){	
			return isRankCheck(appMapper, id, eTYPE_USER_RANK.USER_RANK_PART_OP_CHAR.getValue());
		}
		
		public String MakeDevEuiJson(String dev_eui){
			
		try {		
				mapResponse.put("dev_eui", dev_eui);
				return mapperResponse.writerWithDefaultPrettyPrinter().writeValueAsString(mapResponse);
			
			}catch(JsonGenerationException e) {
				logger.error(e.toString());
			}catch(JsonMappingException e) {
				logger.error(e.toString());
			}catch(IOException e){
				logger.error(e.toString());
			}
			
			return null;
		}
		
		
		
		/* 1. thingplug 등록 절차
		 *   - 성공  - subscription : 활성화, event insert, history insert
		 *         - contents/lastes : event insert, history insert
		 *         - other : history insert
		 *   - 실패  - history insert
		 *         - 상태체크 후 비활성화, 비활성화일때만 event insert
		 * 
		 * 2. push : thingplug -> rmtech ==> value check 후
		 *   - 장애, event insert, history insert
		 *   - 장애복구, event insert, history insert
		 *   - equal data, event insert, history
		 *  
		 * 3. reload 
		 *   - 성공 : 데이터 reload, event insert, history insert 
		 *   - 실패 : ack
		 *   
		 * 4. 활성화/비활성화
		 *   - 활성화 : 비활성화->활성화 
		 *     - subscription 조회 후 데이터 존재시 subscription 등록
		 *     - 활성화 등록, event insert, history insert
		 *   - 비활성화 : 활성화->비활성화
		 *     - subscription 조회 후 데이터 존재시 subscription 삭제
		 *     - 비활성화 등록, event insert, history insert
		 */
		
		public static final String UpdateBpointStatus_Active(BpointDomainMapper appMapper, String dev_eui, 
				eTYPE_EVENT_STATUS_CHAR active_status, eTYPE_EVENT_STATUS event_status, boolean insert_event){
			return UpdateBpointStatus(appMapper, dev_eui, eTYPE_EVENT_STATUS_CHAR.STATUS_ACTIVATE_CHAR);
		}
		public static final String UpdateBpointStatus_Deactive(BpointDomainMapper appMapper, String dev_eui, 
				eTYPE_EVENT_STATUS_CHAR active_status, eTYPE_EVENT_STATUS event_status, boolean insert_event){
			return UpdateBpointStatus(appMapper, dev_eui, eTYPE_EVENT_STATUS_CHAR.STATUS_DEACTIVATE_CHAR);
		}
			
		public static final String UpdateBpointStatus(BpointDomainMapper appMapper, String dev_eui, 
				eTYPE_EVENT_STATUS_CHAR active_status){
			String strReturn = "";
			try{
				BpointVO vo = new BpointVO();
				
				vo.setBpoint_ltid(dev_eui);
				vo.setId_update("system");
				vo.setBpoint_status(active_status.getValue());
				
				int result = appMapper.appUpdateBpointStatus(vo);
				
				if(result > 0) return null;			
				
				strReturn = "dev_eui["+ dev_eui + "],상태:" + active_status.getName() +" 을 저장하지 못하였습니다. query error ";
				
			}catch(Exception e){
				strReturn = "dev_eui["+ dev_eui + "],상태:" + active_status.getName() +" 을 저장하지 못하였습니다. error : " + e.toString(); 
			}
			return strReturn;
		}
		
		// rm_event_hist, rm_bpoint_hist 에 저장
		public static final String InsertEventAndHistory(BpointDomainMapper appMapper, eTYPE_EVENT event_type,	
				String dev_eui, eTYPE_EVENT_STATUS event_status, String title, String detail, String id,
				eTHINGPLUG_STATUS thingplug_status,	String msg){
				return InsertEventAndHistory(appMapper, event_type, dev_eui, event_status, title, detail, id,
						thingplug_status, msg, 0.0, (float)0.0, 0.0, 0.0, 0.0, 0, 0, 0, null);
		}

		public static final String InsertEventAndHistory(BpointDomainMapper appMapper, eTYPE_EVENT event_type,	
				String dev_eui, eTYPE_EVENT_STATUS event_status, String title, String detail, String id,
				eTHINGPLUG_STATUS thingplug_status, 
				String msg, double degrees, float humidity, double x, double y, double z, 
				int battery, int signal, 
				long statetag, String last_modified_time) {
			
			String event_msg=null;
			String history_msg=null;
			
			event_msg = InsertEventHist(appMapper, event_type,	dev_eui, event_status, title, detail, id);
			history_msg = InsertBpointHistory(appMapper, dev_eui, thingplug_status, msg, degrees, 
							humidity, x, y, z, battery, signal, statetag, last_modified_time);
			
			if((event_msg==null) && (history_msg==null)) return null;
			if((event_msg!=null) && (history_msg!=null)) return event_msg+","+history_msg;
			if(event_msg!=null) return event_msg;
			return history_msg;
		}
		
		// rm_event_hist 에 저장
		public static String InsertEventHist(BpointDomainMapper appMapper, eTYPE_EVENT event_type,	
				String dev_eui, eTYPE_EVENT_STATUS event_status, String title, String detail, String id){
		
			String strReturn=null;
					
			try{
				EventHistVO vo = new EventHistVO();
			
				if(dev_eui!=null)	vo.setEvent_ltid(dev_eui);
				vo.setEvent_type(event_type.getValue());
				vo.setEvent_status(event_status.getValue());
				
				if(title!=null){
					if(title.length()>DB_SIZE_EVENT_TITLE)
						vo.setEvent_title(title.substring(0, DB_SIZE_EVENT_TITLE));
					else vo.setEvent_title(title);
				}
				if(detail!=null){
					if(detail.length()>DB_SIZE_EVENT_DETAIL)
						vo.setEvent_detail(detail.substring(0, DB_SIZE_EVENT_DETAIL));
					else	vo.setEvent_detail(detail);
				}
				if(id!=null){
					if(id.length()>DB_SIZE_ID)
						vo.setId_insert(id.substring(0, DB_SIZE_ID));
					else vo.setId_insert(id);
				}
				
				int nResult = appMapper.insertEvent(vo);
				
				if(nResult>0) return null;
				else strReturn = "dev_eui[" + dev_eui+"],이벤트타입:"+event_type.getName()+
							",이벤트상태:"+event_status.getName()+"을 이벤트 table에 저장하지 못하였습니다." ;				
			}catch(Exception e){
				strReturn = "dev_eui[" + dev_eui+"],이벤트타입:"+event_type.getName()+
						",이벤트상태:"+event_status.getName()+"을 이벤트 table에 저장하지 못하였습니다. error : " + e.toString();
			}
			
			return strReturn; 
		}
		
		
		
		// rm_bpoint_hist table 저장
		public static final String InsertBpointHistory(BpointDomainMapper appMapper, String ltid, eTHINGPLUG_STATUS status, 
				String msg){
			return InsertBpointHistory(appMapper, ltid, status, msg, 0.0, (float)0.0, 0.0, 0.0, 0.0, 0, 0, 0, null);
		}
		
		public static final String InsertBpointHistory(BpointDomainMapper appMapper, String ltid, 
				eTHINGPLUG_STATUS status, String msg, double degrees, float humidity, 
				double x, double y, double z, int battery, int signal, long statetag, String last_modified_time ){
			int nCount = 0;
			
			try{
				BpointHistVO vo = new BpointHistVO();
			
				vo.setBpoint_ltid(ltid);
				vo.setBpoint_status(status.getCodeValue());
				vo.setBpoint_degrees(degrees);
				vo.setBpoint_humidity(humidity);
				vo.setBpoint_x(x);
				vo.setBpoint_y(y);
				vo.setBpoint_z(z);
				vo.setBpoint_msg(msg);
				vo.setBpoint_battery(battery);
				vo.setBpoint_signal(signal);
				vo.setBpoint_statetag(statetag);
				vo.setBpoint_last_modified_time(last_modified_time);
			
				nCount = appMapper.insertBpointHist(vo); 
				
				if( nCount > 0) return null;
				
				return "dev_eui["+ltid+"]의 status["+status.getName()+"]를 경계점 History에 정보를 저장하지 못하였습니다.";
				
			}catch(Exception e){
					
				return "dev_eui["+ltid+"]의 status["+status.getName()+"]를 경계점 History에 정보를 저장하지 못하였습니다. error:" + e.toString();
			}
		}
}
