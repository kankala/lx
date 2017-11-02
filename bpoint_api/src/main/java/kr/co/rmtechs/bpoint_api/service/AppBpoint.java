package kr.co.rmtechs.bpoint_api.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.BpointBaseVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointGroupMapVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointVO;

public class AppBpoint extends MakeResponse {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String getLtidAndGroupMap(BpointDomainMapper appMapper, String dev_eui, boolean bInsert){
		
		try{
			// 	데이터 재조회
			BpointVO vo = appMapper.getLtid(dev_eui);
			// null 처리
			if(vo.getBpoint_event_time()==null) vo.setBpoint_event_time("");
			if(vo.getBpoint_addr_city()==null) vo.setBpoint_addr_city("");
			if(vo.getBpoint_addr_etc()==null) vo.setBpoint_addr_etc("");
			if(vo.getBpoint_zipcode()==null) vo.setBpoint_zipcode("");
			
			List<Integer> arrayGroupMap = new ArrayList<Integer>();
		
			// INSERT 루틴.
			if(bInsert) {
				vo.setBpoint_group_array(arrayGroupMap);	// GROUP_LIST NULL 처리를 위함.
				logger.debug(vo.toString());
				return MakeSuccessResponse(RET_SUCCESS, "dev_eui : "+dev_eui+" 저장을 성공하였습니다.", vo);
			}

			// UPDATE 루틴
			// 	group check
			try{
				List<BpointGroupMapVO> bpointGroupMapVo = appMapper.getBpointGroupMap(dev_eui);
				logger.debug("dev_eui group map count : " + bpointGroupMapVo.size());
				if(bpointGroupMapVo.size()>0){
					for(BpointGroupMapVO tmpVo : bpointGroupMapVo){
						logger.debug("group no : " + tmpVo.getGroup_no());
						arrayGroupMap.add(tmpVo.getGroup_no());
					}
					vo.setBpoint_group_array(arrayGroupMap);
				}
			}catch(Exception e){
					logger.error("getBpointGroupMap exception : " + e.toString());
					vo.setBpoint_group_array(arrayGroupMap);
					e.printStackTrace();
			}
				
			return MakeSuccessResponse(RET_SUCCESS, "dev_eui:"+dev_eui + " 변경을 성공하였습니다.", vo);
		
		}catch(Exception e){
			logger.error("getLtidAndGroupMap getLtid exception: " + e.toString());
			e.printStackTrace();
		}
		//insert 시 getltid 조회 실패
		if(bInsert)	return MakeSuccessResponse(ERROR_SELECT_FAIL, "dev_eui : "+dev_eui+" 저장을 성공하였습니다.", null);
		return MakeSuccessResponse(ERROR_SELECT_FAIL, "dev_eui:"+dev_eui + " 변경을 성공하였습니다.", null);
	}

	public BpointBaseVO getLtidAndGroupMap(BpointDomainMapper appMapper, BpointBaseVO vo){
	
		List<Integer> arrayGroupMap = new ArrayList<Integer>();
		try{
			List<BpointGroupMapVO> bpointGroupMapVo = appMapper.getBpointGroupMap(vo.getBpoint_ltid());
			
			logger.debug("dev_eui group map count : " + bpointGroupMapVo.size());
			logger.debug(bpointGroupMapVo.toString());
			
			if(bpointGroupMapVo.size()>0){
				for(BpointGroupMapVO tmpVo : bpointGroupMapVo){
					logger.debug("group no : " + tmpVo.getGroup_no());
					arrayGroupMap.add(tmpVo.getGroup_no());
				}
			}
			vo.setBpoint_group_array(arrayGroupMap);
		}catch(Exception e){
			logger.error("getBpointGroupMap exception: " + e.toString());
			vo.setBpoint_group_array(arrayGroupMap);
			e.printStackTrace();
		}
		return vo;
	}
}
