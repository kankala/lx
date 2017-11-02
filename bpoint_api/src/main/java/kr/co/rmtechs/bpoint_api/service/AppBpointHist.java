package kr.co.rmtechs.bpoint_api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.BpointHistVO;
import kr.co.rmtechs.commons.DateTimeUtil;

@Service
public class AppBpointHist extends MakeResponse {
	@Autowired
	BpointDomainMapper appMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String CheckArg(String ltid, String page, String count) {
		
		if(	page==null || page == "" ||
			count==null || count == "" ){
				String strMsg=null;
				if(page==null || page=="") strMsg = "page,";
				if(count==null || count=="") strMsg += "count,";			
						
			 return MakeFailResponse(ERROR_NOT_EXIST_IF, strMsg, null);
		} 
		return null;
	}
	
	public String GetBpointHist(String ltid, String argPage, String argCount){
		String checkArg = CheckArg(ltid, argPage, argCount);
		if(checkArg!=null) return checkArg;
		
		argPage.trim();
		argCount.trim();
		if((ltid!=null) && (ltid!="")) ltid.trim();
		
		int count = Integer.parseInt(argCount);
		int start = (Integer.parseInt(argPage)-1)*count;
		
		try{
			String tableName = "rm_bpoint_hist_" + DateTimeUtil.getDayOfWeekToDB();
			List<BpointHistVO> vo = appMapper.getBpointHist(ltid, tableName, start, count);
			if((vo==null) || (vo.size()==0)) {
				logger.debug(vo.toString());
				MakeSuccessResponse(ERROR_QUERY_NO_DATA, MSG_QUERY_NO_DATA, null);
			}
			return MakeSuccessResponse(RET_SUCCESS, null, vo);
			
		}catch(Exception e){
			logger.error("getBpointHist exception : " + e.toString());
			e.printStackTrace();
		}
		
		return DEFAULT_ERROR_STRING; 
	}
	
}
