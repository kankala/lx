package kr.co.rmtechs.bpoint_api;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import kr.co.rmtechs.bpoint_api.service.PushMessageServiceImpl;

@Controller
public class PushMessageController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired 
	PushMessageServiceImpl pushMessageService;
	
	@PostConstruct
	public void startPushMessageContoller()
	{
		logger.info(" - startPushMessage ----------------- ");
		pushMessageService.startPushMessage();
	}
	
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info(" - endPushMessage Container is destroy! clean up ----------------- ");
		pushMessageService.setEndFlag();
	}
}
