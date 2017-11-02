package kr.co.rmtechs.bpoint_api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT;
import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.EventHistVO;

@Service
public class AppEventHist extends MakeResponse {
	
	@Autowired
	BpointDomainMapper appMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String CheckArg(String type, String dev_eui, String page, String argCount) {
		
		String strMsg = "";
		
		if( type==null || type == "" ||
			page==null || page == "" ||
			argCount==null || argCount=="" ){
				
			if(type==null || type=="") strMsg = "type,";
			if(page==null || page=="") strMsg += "page,";
			if(argCount==null || argCount=="") strMsg += "count";				
						
			return MakeFailResponse(ERROR_NOT_EXIST_IF, strMsg, null);
		}
		
		if( type.equals(""+ eTYPE_EVENT.TYPE_EVENT_LTID.getValue()) != true && 
			type.equals(""+ eTYPE_EVENT.TYPE_EVENT_ALARM.getValue())!= true &&
			type.equals(""+ eTYPE_EVENT.TYPE_EVENT_GROUP.getValue())!= true &&
			type.equals(""+ eTYPE_EVENT.TYPE_EVENT_ALL.getValue())	!= true)
			 return MakeFailResponse(ERROR_INVALID_VALUE, "type : " + type, null);
				
		if(type.equals( eTYPE_EVENT.TYPE_EVENT_LTID.getValue()) && 
				(dev_eui==null || dev_eui==""))
			return MakeFailResponse(ERROR_NOT_EXIST_IF, "dev_eui", null);

		if(Integer.parseInt(page)<=0)
				return MakeFailResponse(ERROR_INVALID_VALUE, "page : " + page, null);
		if(Integer.parseInt(argCount)<=0)
				return MakeFailResponse(ERROR_INVALID_VALUE, "count : " + argCount, null);
			
		return null;
	}
	
	public String GetEventList(String type, String dev_eui, String argPage, String argCount){
		
		String checkArg = CheckArg(type, dev_eui, argPage, argCount);
		if(checkArg!=null) return checkArg;
		
		type.trim();  
		argPage.trim();
		argCount.trim();
		if((dev_eui!=null)&&(dev_eui!="")) dev_eui.trim();
			
		try{
			int count = Integer.parseInt(argCount);
			int	total = (Integer.parseInt(argPage)-1) * Integer.parseInt(argCount);
						
			List<EventHistVO> vo = appMapper.getEventList(type, dev_eui, total, count);
									
			if((vo != null) && (vo.size()>0)){
				logger.debug("vo is not null : " + vo.size());
				logger.debug(vo.toString());
				return MakeSuccessResponse(RET_SUCCESS, null, vo);
			}
			return MakeSuccessResponse(ERROR_QUERY_NO_DATA, MSG_QUERY_NO_DATA, null);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("GetEventList : " + e.toString());
		}
		return DEFAULT_ERROR_STRING;
	}
}
