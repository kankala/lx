package kr.co.rmtechs.bpoint_api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.GroupVO;

@Service
public class AppGroupList extends MakeResponse {
	@Autowired
	BpointDomainMapper appMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String GetGroupList(){ 
			
		try {
			List<GroupVO> vo = appMapper.getGroupList();
			
			if((vo != null) && (vo.size()>0)){
				logger.debug("group_list is not null : " + vo.size());
				logger.debug(vo.toString());
				
				return MakeSuccessResponse(RET_SUCCESS, null, vo);
			}
			
			logger.debug("group_list is null ");
			return MakeSuccessResponse(ERROR_QUERY_NO_DATA, MSG_QUERY_NO_DATA, null);
		}catch(Exception e){
			e.printStackTrace();
		}
				
		return DEFAULT_ERROR_STRING;
	}
}
