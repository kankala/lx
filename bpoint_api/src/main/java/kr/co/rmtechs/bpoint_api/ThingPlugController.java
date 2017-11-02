package kr.co.rmtechs.bpoint_api;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import kr.co.rmtechs.bpoint_api.service.ThingPlugServiceImpl;


@Controller
public class ThingPlugController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired 
	ThingPlugServiceImpl thingPlugService;
	
	@PostConstruct
	public void startPushMessageContoller()
	{
		logger.info(" - startThingPlug ----------------- ");
		thingPlugService.startThingPlug();
	}
	
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info(" - endThingPlug Container is destroy! clean up ----------------- ");
		thingPlugService.setEndFlag();
	}
}
