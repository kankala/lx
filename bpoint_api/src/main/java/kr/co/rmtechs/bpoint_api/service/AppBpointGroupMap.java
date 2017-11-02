package kr.co.rmtechs.bpoint_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT_STATUS;
import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.BpointGroupMapVO;
import kr.co.rmtechs.bpoint_domain.vo.GroupVO;

@Service
public class AppBpointGroupMap extends MakeResponse {
	@Autowired
	BpointDomainMapper appMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private char EVENT_GROUP_ADD	= '1';
	private char EVENT_GROUP_DELETE	= '2';
	private int  ERROR_BPOINT_GROUP_MAP = ERROR_BPOINT_GROUP_BASE+1;
	
	public String CheckArg(String action, String dev_eui, String group_no, List<GroupVO> groupVo) {
		String strMsg = "";
		
		if( action==null || action=="" || 
				dev_eui == null || dev_eui== "" ||
			group_no == null || group_no =="" ){
			if(action==null || action=="") strMsg = "action,";
			if(dev_eui==null || dev_eui=="") strMsg += "dev_eui,";
			if(group_no==null || group_no=="") strMsg += "group_no";					
			 return MakeFailResponse(ERROR_NOT_EXIST_IF, strMsg, null);
		} 
		
		if( (action.equals(""+EVENT_GROUP_ADD) != true) && 
			(action.equals(""+EVENT_GROUP_DELETE) != true) )
			return MakeFailResponse(ERROR_INVALID_VALUE, "action", null);

		if( dev_eui.length() != DB_SIZE_LTID) 
			return MakeFailResponse(ERROR_STRING_SIZE_WRONG, "dev_eui", null);
		
		// 등록인 경우만, 해당 그룹이 존재하는지 확인하며, 
		// 존재하지 않는 그룹이면 에러를 던진다.
		if(action.equals(""+EVENT_GROUP_ADD) == true){
			for(int i = 0; i<groupVo.size(); i++){
				GroupVO vo = groupVo.get(i);
				if(group_no.equals(""+vo.getGroup_no()) == true)
					return null;
			}
			return MakeFailResponse(ERROR_INVALID_VALUE, "group_no", null);
		}
		
		// 삭제이면 그룹을 체크하지 않는다.
		return null;
	}
	
	
	public String SetBpointGroupMap(String id, ArrayList<Object> list){
		
		int nPos = 0;
		
		try{
			if((id==null)||(id=="")) return MakeFailResponse(ERROR_NOT_EXIST_IF, "id", null);
			if(id.length()>DB_SIZE_ID) return MakeFailResponse(ERROR_STRING_SIZE_WRONG, "id", null);
			
			List<GroupVO> groupVo = appMapper.getGroupList();
	
			if( (groupVo == null) || (groupVo.size()==0)){
				logger.debug("vo is null : " + id);
				return MakeFailResponse(ERROR_QUERY_NO_DATA, "DB:bpoint_group_map", null);
			}
		
			nPos = 1;
			List<BpointGroupMapVO> addVo = new ArrayList<BpointGroupMapVO>();
			List<BpointGroupMapVO> deleteVo = new ArrayList<BpointGroupMapVO>();
			
			String strAddDetail = "";
			String strDeleteDetail = "";
			String strAddDebEui = "";
			String strDelDebEui = "";
			String strMsg=null;
			
			// add 와 delete 를 구분하기 위함.
			for(int i=0; i<list.size();i++){
				logger.debug("index : " + i);
				Map<String, Object> subMap = (Map<String, Object>)list.get(i);
				
				String action = (String)subMap.get("action");
				String ltid = (String)subMap.get("dev_eui");
				String group_no = (String) subMap.get("group_no");
				
				logger.debug("action   : " + action);
				logger.debug("dev_eui  : " + ltid);
				logger.debug("group_no : " + group_no);

				String checkArg = CheckArg(action, ltid, group_no, groupVo);
				if(checkArg!=null) return checkArg;
				
				BpointGroupMapVO vo = new BpointGroupMapVO();
				vo.setBpoint_ltid(ltid);
				vo.setGroup_no(Integer.parseInt(group_no));
				
				if(action.equals(""+EVENT_GROUP_ADD)){
					vo.setId_insert(id);
					addVo.add(vo);
					strAddDetail += "[" + vo.getBpoint_ltid() + "," + vo.getGroup_no() + "],";
					
					if(strAddDebEui.length()==0)	strAddDebEui = vo.getBpoint_ltid();
					else {
						if( strAddDebEui.indexOf(vo.getBpoint_ltid()) == -1) 
							strAddDebEui = strAddDebEui +","+vo.getBpoint_ltid(); 
					}
				}
				else {
					vo.setId_update(id);
					deleteVo.add(vo);
					
					strDeleteDetail += "[" + vo.getBpoint_ltid() + "," + vo.getGroup_no() + "],";
					
					if(strDelDebEui.length()==0) strDelDebEui = vo.getBpoint_ltid();
					else{
						if( strDelDebEui.indexOf(vo.getBpoint_ltid()) == -1) 
							strDelDebEui = strDelDebEui +","+vo.getBpoint_ltid();
					}
				}
			}

			// vo check ------------------------------------------
			int nCount=0;
			
			if(addVo.size()>0)	{	// group_no를 체크하여 존재하는게 있을 경우 에러
				nCount = appMapper.getLtidGroupMapCount(addVo);
				logger.debug("insertVo group count : " + addVo.size() +", query count : " + nCount);
				if(nCount>0) return MakeFailResponse(ERROR_INSERT_FAIL, "저장 요청하신 group_no가 이미 존재합니다.", null);
			}
			nPos = 2;
			if(deleteVo.size()>0){	//group_no 수와 수신되어진 group_no 수가 틀린경우 에러
				nCount = appMapper.getLtidGroupMapCount(deleteVo);
				logger.debug("deleteVo group count : " + deleteVo.size()+", query count : "+ nCount);
				if(nCount!=deleteVo.size()) return MakeFailResponse(ERROR_DELETE_FAIL, "삭제 요청하신 group_no가 존재하지 않습니다.", null);		
			}
			nPos = 3;
			
			if(addVo.size()>0){		// 저장
				nCount = appMapper.insertGroupMap(addVo);	
				if(nCount==0) return MakeFailResponse(ERROR_INSERT_FAIL, "저장에 실패하였습니다.", null);
				
				strMsg = InsertEventHist(appMapper,  eTYPE_EVENT.TYPE_EVENT_GROUP, strAddDebEui,  
						eTYPE_EVENT_STATUS.STATUS_LTID_GROUP_ADD,
							"dev_eui-group_no를 추가하였습니다.", strAddDetail, id);
				
				if(strMsg!=null) logger.warn("InsertEventHist : " + strMsg);
			}
			nPos = 4;
			if(deleteVo.size()>0){	// 삭제
				nCount = appMapper.deleteGroupMap(deleteVo);
				if(nCount==0) return MakeFailResponse(ERROR_DELETE_FAIL, "삭제에 실패하였습니다.", null);
				
				strMsg = InsertEventHist(appMapper,  eTYPE_EVENT.TYPE_EVENT_GROUP, strDelDebEui, 
						eTYPE_EVENT_STATUS.STATUS_LTID_GROUP_DELETE,
						"dev_eui-group_no를 삭제하였습니다.", strDeleteDetail, id);
				if(strMsg!=null) logger.warn("SetEventHist : " + strMsg);
			}	
						
		}catch(Exception e){
			
			logger.debug("exception setBpointGroupMap : " + e.toString());
			if(nPos==0)
				 return MakeFailResponse(ERROR_BPOINT_GROUP_MAP, "search group_no fail", null);
			else if(nPos==1)
				 return MakeFailResponse(ERROR_BPOINT_GROUP_MAP, "search insert count fail", null);
			else if(nPos==2)
				 return MakeFailResponse(ERROR_BPOINT_GROUP_MAP, "search delete count fail", null);
			else if(nPos==3)
				 return MakeFailResponse(ERROR_BPOINT_GROUP_MAP, "insert GroupMap fail", null);
			else return MakeFailResponse(ERROR_BPOINT_GROUP_MAP, "delete GroupMap fail", null);
		}
			
		return MakeSuccessResponse(RET_SUCCESS, null, null);
	}

}
