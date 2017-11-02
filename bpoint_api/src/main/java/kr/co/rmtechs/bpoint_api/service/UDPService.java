package kr.co.rmtechs.bpoint_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import kr.co.rmtechs.bpoint_api.config.BpointProperties;
import kr.co.rmtechs.bpoint_api.service.net.LGUPlusServer;
import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.commons.Utils;

@Configuration
@Component
public class UDPService {
	public static UDPService service;
	public static UDPService getInstance() {
		return service;
	}
	
	@Autowired private BpointDomainMapper appMapper;
	@Autowired private BpointProperties	bpointProperties;	
	
	@Bean(name="UpdInit")
	public boolean initLGUPlusServer() {
		if(UDPService.service == null)
			UDPService.service = this;
		
		try {
			String str_port = bpointProperties.getUdp_port();
			int port = LGUPlusServer.DEFAULT_PORT;
			if(!StringUtils.isEmpty(str_port)) {
				port = Utils.parseInt(str_port);
			}
			LGUPlusServer server = LGUPlusServer.getInstance(port);
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}

	public BpointDomainMapper getAppMapper() {
		return appMapper;
	}

	public void setAppMapper(BpointDomainMapper appMapper) {
		this.appMapper = appMapper;
	}
	
	
}