package kr.co.rmtechs.bpoint_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_SETTING;
import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.SettingVO;

@Service
public class AppSetting extends MakeResponse {
	@Autowired
	BpointDomainMapper appMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String GetAlarm(){
		String returnJson = DEFAULT_ERROR_STRING;
		
		try{
			SettingVO vo = appMapper.getSetting(eTYPE_SETTING.ALARM.getValue());
			
			if(vo != null){
				logger.debug(vo.toString());
				returnJson = MakeSuccessResponse(RET_SUCCESS, null, vo);
			}
			else returnJson=MakeSuccessResponse(ERROR_QUERY_NO_DATA,MSG_QUERY_NO_DATA,null);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAlarm : " + e.toString());
		}
		
		return returnJson;
	}
	
	public String CheckArg( String value1, String value2, String value3, String id){
		if( value1==null || value1=="" ||
			value2==null || value2=="" ||
			value3==null || value3=="" ||
			id==null     || id==""   ){
			String strMsg=null;
			if(value1==null || value1=="") strMsg = "x,";
			if(value2==null || value2=="") strMsg += "y,";
			if(value3==null || value3=="") strMsg += "z,";
			if(id==null     || id=="")     strMsg += "id,";
							
			 return MakeFailResponse(ERROR_NOT_EXIST_IF, strMsg, null);
		}
					 
		return null;
	}
	
	public String SetAlarm(String value1, String value2, String value3, String id){
	
		try{
			String checkArg = CheckArg(value1, value2, value3, id);
			if(checkArg!=null) return checkArg;
		
			//권한 체크
			if(isAdminRank(appMapper,id)==false)
				return MakeFailResponse(ERROR_NOT_EXIST_PRIVILEGE, "id : "+id, null);
				
			int nCount = appMapper.appUpdateSetting(value1, value2, value3, id, eTYPE_SETTING.ALARM.getValue());
				
			logger.debug("appUpdateSetting count : " + nCount);
			if(nCount>0){				
				String strMsg = InsertEventHist(appMapper,  eTYPE_EVENT.TYPE_EVENT_ALARM, "alarm", 
						eTYPE_EVENT_STATUS.STATUS_MODIFY,	"알람 설정 값을 변경하였습니다.", 
						"x:"+value1+", y:"+value2+", z:"+value3+"으로 변경하였습니다.",
						id);
				if(strMsg!=null) logger.warn("InsertEventHist : " + strMsg);
				
				return MakeSuccessResponse(RET_SUCCESS, null, null);
			}
			else return MakeFailResponse(ERROR_UPDATE_FAIL,"alarm",null);
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAlarm : " + e.toString());
		}
		
		return DEFAULT_ERROR_STRING;
	}

}
