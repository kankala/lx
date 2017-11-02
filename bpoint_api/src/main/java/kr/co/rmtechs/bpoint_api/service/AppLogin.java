package kr.co.rmtechs.bpoint_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.MemberVO;
/*
"if":"login",
"ver": "1.0",
"id": "admin",      
"pwd":"1",
"uuid":"admin_uuid",
*/
@Service
public class AppLogin extends MakeResponse{
	@Autowired
	BpointDomainMapper appMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final int ERROR_LOGIN_FAIL = ERROR_LOGIN_BASE+1;
	
	public String CheckArg(String id, String pwd, String uuid) {
	
		if(id==null || pwd == null || uuid==null ||
				id=="" || pwd == "" || uuid =="" ){
				String strMsg=null;
				if(id==null || id=="") strMsg = "id,";
				if(pwd==null || pwd=="") strMsg += "pwd,";
				if(uuid==null || uuid=="") strMsg += "uuid";				
						
			 return MakeFailResponse(ERROR_NOT_EXIST_IF, strMsg, null);
		} 
		return null;
	}
	
	public String Login(String id, String pwd, String uuid) {
		
		String checkArg = CheckArg(id,pwd,uuid);
		if(checkArg!=null) return checkArg;
		
		id.trim(); pwd.trim(); uuid.trim();
			
		try{
			MemberVO vo = new MemberVO();
			vo.setMember_id(id);
			vo.setMember_pwd(pwd);
			vo.setMember_uuid(uuid);
				
			int nResult = appMapper.checkLogin(vo);
			if(nResult == 0) return MakeFailResponse(ERROR_LOGIN_FAIL, id+", pwd가 상이합니다.", null);
			
			nResult = appMapper.updateLogin(vo);
				
			if(nResult==0) 
				logger.error("update 가 실패 하였습니다. id : " +vo.getMember_id() +" , uuid : "+vo.getMember_uuid());
			
			vo = appMapper.getUser(id);
			
			if(vo!=null)
				return MakeSuccessResponse(RET_SUCCESS, id+"조회가 정상적으로 확인되었습니다.", vo);
			
		}catch(Exception e){
			logger.error(e.toString());
		}
		
		return DEFAULT_ERROR_STRING;
	}
	
	
	public String ChangePwd(String id, String pwd, String change_pwd) {
				
		if(	id==null  || id=="" ||
			pwd==null || pwd=="" ||
			change_pwd==null || change_pwd=="") {
				String strMsg=null;
				if(id==null || id=="") strMsg = "id,";
				if(pwd==null || pwd=="") strMsg += "pwd,";		
				if(change_pwd==null || change_pwd=="") strMsg += "chage_pwd";
						
			 return MakeFailResponse(ERROR_NOT_EXIST_IF, strMsg, null);
		}
		
		id.trim(); pwd.trim(); 
			
		try{
			MemberVO vo = new MemberVO();
			vo.setMember_id(id);
			vo.setMember_pwd(pwd);
				
			int nResult = appMapper.checkLogin(vo);
			if(nResult == 0 ) return MakeFailResponse(ERROR_LOGIN_FAIL, id+", pwd가 상이합니다.", null);
			
			vo.setMember_pwd(change_pwd);
			nResult = appMapper.changePwd(vo);
			
			if(nResult==0) {
				logger.warn("update 가 실패 하였습니다. id : " +vo.getMember_id() );
				return MakeFailResponse(ERROR_UPDATE_FAIL, "id : " + id, null);
			}

			return MakeSuccessResponse(RET_SUCCESS, id+"의 비밀번호가 정상적으로 변경되었습니다.", null);
			
		}catch(Exception e){
			logger.debug(e.toString());
		}
		
		return DEFAULT_ERROR_STRING;
	}

}