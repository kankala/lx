package kr.co.rmtechs.bpoint_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.VersionVO;

@Service
public class AppVersion extends MakeResponse {
	
	@Autowired
	BpointDomainMapper appMapper;
	
	public final static String OS_ANDROID="1";
	public final static String OS_IOS="2";
	
	public boolean CheckOsType(String os){	
		if(os.equals(OS_ANDROID) || os.equals(OS_IOS)) return true;
		return false;
	}
	
	public String GetLastVersion(String os){
		
		if(CheckOsType(os)==true)
		{
			try{
			
				VersionVO vo = appMapper.getLastVersion(os);
				
				if(vo != null){
					return MakeSuccessResponse(RET_SUCCESS, null, vo);
				}
				return MakeSuccessResponse(ERROR_QUERY_NO_DATA,MSG_QUERY_NO_DATA,null);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		else return MakeFailResponse(ERROR_NOT_EXIST_IF, "os", null);
		
		return DEFAULT_ERROR_STRING;
	}

}
