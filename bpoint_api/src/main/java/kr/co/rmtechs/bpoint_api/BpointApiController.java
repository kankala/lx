package kr.co.rmtechs.bpoint_api;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.rmtechs.bpoint_api.config.BpointProperties;
import kr.co.rmtechs.bpoint_api.service.AppBpointGroupMap;
import kr.co.rmtechs.bpoint_api.service.AppBpointHist;
import kr.co.rmtechs.bpoint_api.service.AppBpointReg;
import kr.co.rmtechs.bpoint_api.service.AppEventHist;
import kr.co.rmtechs.bpoint_api.service.AppGroupList;
import kr.co.rmtechs.bpoint_api.service.AppLogin;
import kr.co.rmtechs.bpoint_api.service.AppSetting;
import kr.co.rmtechs.bpoint_api.service.AppVersion;
import kr.co.rmtechs.bpoint_api.service.BpointDecorder;
import kr.co.rmtechs.bpoint_api.service.MakeResponse;
import kr.co.rmtechs.bpoint_api.service.ThingPlugServiceImpl;
import kr.co.rmtechs.bpoint_domain.BPOINT_RESOURCE;
import kr.co.rmtechs.bpoint_domain.eTHINGPLUG_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_SETTING;
import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.BpointContentVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointVO;
import kr.co.rmtechs.bpoint_domain.vo.SettingVO;
import kr.co.rmtechs.commons.DateTimeUtil;

@PermitAll
@RestController
public class BpointApiController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MakeResponse makeResponse;
	
	@Autowired
	AppVersion appVersion;
	
	@Autowired
	AppLogin appLogin;
	
	@Autowired
	AppGroupList appGroupList;
	
	@Autowired
	AppEventHist appEventHist;
	
	@Autowired
	AppSetting appSetting;
	
	@Autowired
	AppBpointGroupMap appBpointGroupMap;
	
	@Autowired
	AppBpointHist appBpointHist;
	
	@Autowired
	AppBpointReg appBpointReg;
	
	@Autowired
	ThingPlugServiceImpl thingplugService;
	
	@Autowired
	BpointDomainMapper appMapper;
	
	@Autowired
	BpointProperties bpointProperties;
	
	@PostConstruct
	public void startBpointApiController()
	{
		if(bpointProperties.getProperties_name()!=null){
			logger.info(" * startBpointApiController -----------------["
					+ bpointProperties.getApp_eui() +","+bpointProperties.toString() +"]");
			System.out.println(" * startBpointApiController -----------------["
					+ bpointProperties.getApp_eui() +","+bpointProperties.toString() +"]");
		}
		else
			logger.info(" * startBpointApiController -----------------");
	}
	
	private int ConvertHexaToInt(int high, int low){
        int ret_value = 0;
          
        high = high&0xFF;
        low = low&0xFF;
        if (high != 0) {
        	ret_value += (high * 256);
        }
        if (low != 0) {
        	ret_value += high;
        }
       
        return ret_value;
	}
	
	
	@RequestMapping(value="/download/{os}", method = RequestMethod.GET)
	public ModelAndView downloadFile(@PathVariable("os")String os){
		
		if(os==null) return null;
		
		String fullPath = bpointProperties.getDownload_path() + os;
		File donwloadFile = new File(fullPath);
		
		logger.debug("download filename : " + fullPath);
		ModelAndView mav = new ModelAndView();
		mav.addObject("downloadFile", donwloadFile);
		mav.addObject("downloadFileName", os);
		mav.setViewName("downloadFileView");
		return mav;
	}
	
	@RequestMapping(value="/thingplug/{id}", produces="application/xml" )
	@ResponseBody
	public String thingPlug(@PathVariable("id")String ltid, @RequestBody String strBody, @RequestHeader Map<String,String> header ){// @RequestHeader(value="X-M2M-RI") String ltid){

		try {
			
			logger.debug("recv push ltid : " + ltid + ",header:" + header +",body:"+strBody);

			if(ltid==null){
				logger.warn("don't have X-M2M-RI header : " + ltid);
				return "";
			}else if(ltid.length()!=BPOINT_RESOURCE.LTID_SIZE){
				logger.warn("invalid X-M2M-RI header : " + ltid);
				return "";
			}

			
			String dev_eui = ltid.substring(BPOINT_RESOURCE.APP_EUI_BOTTOM_SIZE);
			
			
			BpointVO vo = appMapper.getLtid(dev_eui);
			if(vo==null){
				logger.warn("can't search X-M2M-RI header ltid : " + ltid + ",dev_eui: " + dev_eui+ ", body :"+strBody);
				return "";
			}
			

			SettingVO setVo = appMapper.getSetting(eTYPE_SETTING.ALARM.getValue());
			double settingX, settingY, settingZ;
			
			if(strBody!=null){
				logger.debug("/thingplug ["+strBody+"]");
				settingX = Double.parseDouble(setVo.getValue1());
				settingY = Double.parseDouble(setVo.getValue2());
				settingZ = Double.parseDouble(setVo.getValue3());
			}
			else {
				logger.warn("don't have subscription-push body");
				settingX = settingY = settingZ = 0.0;
			}
			
			// 4. parshing..
			Map<String, String> map = thingplugService.parserXmlToStr(strBody);
			logger.debug(map.toString());
			
			String content = (String)map.get("con");			
			String lt = (String)map.get("lt");					
			int size = Integer.parseInt((String)map.get("cs"));	
			String property = (String)map.get("ppt");			
			String statetag = (String)map.get("st");				

			if( (content==null)||(lt==null)||(statetag==null) || 
				(size!=BpointContentVO.getMaxContentSize()))
			{
				logger.warn("don't have body parameter : " + ltid + ",dev_eui: " + dev_eui+ ", body :"+strBody);
				return "";
			}
			
			logger.debug("subscript push content ltid : " + dev_eui  
				+ ",size : " + size + ", content:"+content+",lt:"+lt);
			
			BpointContentVO contentVO = new BpointContentVO(dev_eui, content, lt, property, statetag, true);
			String title = "dev_eui["+dev_eui+"]를 변경하였습니다.";
			String detail = "";
			eTYPE_EVENT_STATUS event_status = eTYPE_EVENT_STATUS.STATUS_MODIFY;
			
logger.debug("base_time : " + vo.getBpoint_base_event_time() 
			+ ", recv : " + contentVO.getBpoint_base_x() +"," + contentVO.getBpoint_base_y() +"," + contentVO.getBpoint_base_z()
			+ ", orig : " + vo.getBpoint_x() +","+ vo.getBpoint_y() +","+ vo.getBpoint_z() 
			+ ", base : " + vo.getBpoint_base_x() +","+ vo.getBpoint_base_y() +","+ vo.getBpoint_base_z()
			+ ", sett : " + settingX + ","+ settingY + ","+ settingZ );

			eTHINGPLUG_STATUS thingplugStatus = eTHINGPLUG_STATUS.SUBSCRIPTION_PUSH;
			if(vo.getBpoint_base_event_time() != null){
				
				boolean fail_x, fail_y, fail_z;
				fail_x = fail_y = fail_z = false; 
			
				if( vo.getBpoint_base_x() > contentVO.getBpoint_base_x() ){
					if( (vo.getBpoint_base_x() - contentVO.getBpoint_base_x()) > settingX) {
						fail_x = true;
						detail = "가속도 X 변경 org: "+ vo.getBpoint_base_x() +",new : " + contentVO.getBpoint_base_x();
					}
				}else if( vo.getBpoint_base_x() < contentVO.getBpoint_base_x() ){
						if( (contentVO.getBpoint_base_x() - vo.getBpoint_base_x()) > settingX){
							fail_x = true;
							detail = "가속도 X 변경 org: "+ vo.getBpoint_base_x() +",new : " + contentVO.getBpoint_base_x();
						}
				}

				if( vo.getBpoint_base_y() > contentVO.getBpoint_base_y() ){
					if( (vo.getBpoint_base_y() - contentVO.getBpoint_base_y()) > settingY){
						fail_y = true;
						if(detail.length()>0) detail +=","; 
						detail += " 가속도 Y 변경 org: "+ vo.getBpoint_base_y() +",new : " + contentVO.getBpoint_base_y();
					}
				}else if( vo.getBpoint_base_y() < contentVO.getBpoint_base_y() ){
						if( (contentVO.getBpoint_base_y() - vo.getBpoint_base_y()) > settingY){
							fail_y = true;
							if(detail.length()>0) detail +=","; 
							detail += " 가속도 Y 변경 org: "+ vo.getBpoint_base_y() +",new : " + contentVO.getBpoint_base_y();
						}
				}
				
				if( vo.getBpoint_base_z() > contentVO.getBpoint_base_z() ){
					if( (vo.getBpoint_base_z() - contentVO.getBpoint_base_z()) > settingZ){
						
						fail_z = true;

						if(detail.length()>0) detail +=",";
						detail += " 가속도 Z 변경 org: "+ vo.getBpoint_base_z() +",new : " + contentVO.getBpoint_base_z();
					}				
				}else if( vo.getBpoint_base_z() < contentVO.getBpoint_base_z() ){
			
						if( (contentVO.getBpoint_base_z() - vo.getBpoint_base_z()) > settingZ){
							fail_z = true;
							if(detail.length()>0) detail +=","; 
							detail += " 가속도 Z 변경 org: "+ vo.getBpoint_base_z() +",new : " + contentVO.getBpoint_base_z();
						}
				}
			
logger.debug("fail/fail back check : " +fail_x +","+fail_y+","+fail_z+",status:"+vo.getBpoint_fail_status());				

				if(fail_x || fail_y || fail_z){	
					if(vo.getBpoint_fail_status()==BPOINT_RESOURCE.STATUS_NORMAL &&
							(contentVO.getBpoint_base_x()!=0 && contentVO.getBpoint_base_y()!=0 && contentVO.getBpoint_base_z()!=0)){
						vo.setBpoint_fail_status(BPOINT_RESOURCE.STATUS_FAIL);
						detail = "recv push[가속도 장애], dev_eui "+dev_eui+", " + detail;
						logger.info(detail);
						event_status = eTYPE_EVENT_STATUS.STATUS_FAIL;
						title = "recv push dev_eui["+dev_eui+"]가 장애가 발생하였습니다.";
						thingplugStatus = eTHINGPLUG_STATUS.SUBSCRIPTION_PUSH_FAIL;
					}
					
				}else{	
					if(vo.getBpoint_fail_status()==BPOINT_RESOURCE.STATUS_FAIL){
						vo.setBpoint_fail_status(BPOINT_RESOURCE.STATUS_NORMAL);
						detail = "recv push[가속도 장애 해제], dev_eui "+dev_eui+", " + detail;
						logger.info(detail);
						event_status = eTYPE_EVENT_STATUS.STATUS_FAILBACK;
						title = "recv push dev_eui["+dev_eui+"]가 장애가 해지되었습니다.";
						thingplugStatus = eTHINGPLUG_STATUS.SUBSCRIPTION_PUSH_FAILBACK;
					}
				}
			
				if(detail.length()==0) detail = "recv push , dev_eui:" + dev_eui + ", 가속도 x,y,z" 
						+ contentVO.getBpoint_base_x()+","+contentVO.getBpoint_base_y() +"," + contentVO.getBpoint_base_z();

				detail += "[수신 data:" + content + "]";
logger.debug("detail msg : " + detail);		

				vo.setBpoint_x(contentVO.getBpoint_base_x());
				vo.setBpoint_y(contentVO.getBpoint_base_y());
				vo.setBpoint_z(contentVO.getBpoint_base_z());
				vo.setBpoint_degrees(contentVO.getBpoint_degrees());
				vo.setBpoint_humidity(contentVO.getBpoint_humidity());
				vo.setBpoint_event_time(contentVO.getBpoint_base_event_time());
				vo.setBpoint_signal(contentVO.getBpoint_base_signal());
				vo.setBpoint_battery(contentVO.getBpoint_base_battery());


				if(appMapper.updateBpointFailStatus(vo)== 0){
					logger.warn("recv push , dev_eui:" + dev_eui + "에 대한 정보를 저장하지 못하였습니다.");
					return "";
				}
			}else{
				contentVO.setBpoint_fail_clear(true);
				if(appMapper.setBpointBaseContent(contentVO)==0){
					logger.warn("recv push , dev_eui:" + dev_eui + "에 대한 정보를 저장하지 못하였습니다.");
					return "";
				}
				detail = "recv push , dev_eui:" + dev_eui + ", 가속도 x,y,z" 
				+ contentVO.getBpoint_base_x()+","+contentVO.getBpoint_base_y() +"," + contentVO.getBpoint_base_z();
			}


			String strTmp = MakeResponse.InsertEventAndHistory(appMapper, eTYPE_EVENT.TYPE_EVENT_LTID,	
					dev_eui, event_status, title, detail, "system",
					eTHINGPLUG_STATUS.SUBSCRIPTION_PUSH, detail,
					contentVO.getBpoint_degrees(), contentVO.getBpoint_humidity(),
					contentVO.getBpoint_base_x(), contentVO.getBpoint_base_y(),
					contentVO.getBpoint_base_z(),
					contentVO.getBpoint_base_battery(), contentVO.getBpoint_base_signal(), 
					contentVO.getBpoint_base_statetag(),
					contentVO.getBpoint_base_event_time());
			if(strTmp!=null) logger.warn("InsertEventAndHistory fail : " + strTmp);
			
		}catch(Exception e){
			logger.error("recv thingplug : " + e.toString());
		}
		
		return "";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public String home(@RequestBody String strBody){
		
		logger.info("/ : " + strBody);
				
		int nErrorCode = 0;
		String returnJson="";
		String strMsg="";
		String strPayload=null;

		try {
			 
			ObjectMapper mapperBody = new ObjectMapper();
			// read JSON
			Map<String, Object> map = mapperBody.readValue(strBody, 
					new TypeReference<Map<String, Object>>(){});
						
			logger.debug(" - ver : " + map.get("ver"));
			logger.debug(" - if_id  : " + map.get("if_id"));
						
			String if_name = (String) map.get("if_id");
									
			if(if_name==null)
			{
				logger.info("if_id null");
				nErrorCode = MakeResponse.ERROR_NOT_DEFINE_IF;
			}
			else if(if_name.equals("get_version")){		
				logger.debug("* start GetLastVersion ------------------------- ");

				return appVersion.GetLastVersion((String)map.get("os"));
			}
			else if(if_name.equals("login")){
				
				logger.debug("* start login ------------------------- ");
				logger.debug(" - id : "   + map.get("id"));
				logger.debug(" - pwd : "  + map.get("pwd"));
				logger.debug(" - uuid : " + map.get("uuid")); 
				
				return appLogin.Login((String)map.get("id"), (String)map.get("pwd"), (String)map.get("uuid"));

			}
			else if(if_name.equals("group_list")){
				
				logger.debug("* start group_list --------------------- ");
				
				return appGroupList.GetGroupList();
			}
			else if(if_name.equals("bpoint_list") || 
					if_name.equals("bpoint_base_list")){
				
				if(if_name.equals("bpoint_list"))
						logger.debug("* start bpoint_list --------------------- ");
				else	logger.debug("* start bpoint_base_list --------------------- ");
				
				return appBpointReg.GetBpointList((String)map.get("event_date"), (String)map.get("order"), 
						(String)map.get("type"), (String)map.get("latitude"), (String)map.get("longitude"), 
						(String)map.get("value"), (String)map.get("active"), (String)map.get("fail_status"),
						(String)map.get("group_no"), (String)map.get("page"), (String)map.get("count"),
						(String)map.get("id"), (String)map.get("if_id"));
			}
			else if(if_name.equals("bpoint_detail")){
				
				logger.debug("* start bpoint_detail ---------------------------------- ");
				logger.debug(" - id 	 : " + map.get("id"));
				logger.debug(" - dev_eui : " + map.get("dev_eui"));
				
				return appBpointReg.GetBpointDetail((String)map.get("dev_eui"), (String)map.get("id"));
			}
			else if(if_name.equals("bpoint_status")){
				
				logger.debug("* start bpoint_status ---------------------------------- ");
				logger.debug(" - dev_eui   : " + map.get("dev_eui"));				
				logger.debug(" - status    : " + map.get("status"));
				logger.debug(" - id        : " + map.get("id"));
				
				return appBpointReg.SetBpointStatus((String)map.get("status"),
						(String)map.get("dev_eui"), (String)map.get("id"));
			}
			else if(if_name.equals("bpoint_reg")){
				
				logger.debug("* start bpoint_reg ---------------------------------- ");		
				logger.debug(" - action        : " + map.get("action"));
				logger.debug(" - dev_eui       : " + map.get("dev_eui"));
				logger.debug(" - name          : " + map.get("name"));
				logger.debug(" - latitude      : " + map.get("latitude"));
				logger.debug(" - longitude     : " + map.get("longitude"));
				logger.debug(" - zipcode       : " + map.get("zipcode"));
				logger.debug(" - zipcode_state : " + map.get("zipcode_state"));
				logger.debug(" - zipcode_city  : " + map.get("zipcode_city"));
				logger.debug(" - zipcode_etc   : " + map.get("zipcode_etc"));
				logger.debug(" - status        : " + map.get("status"));
				logger.debug(" - id            : " + map.get("id"));			
				
				return appBpointReg.SetBpointReg((String)map.get("action"), 
						(String)map.get("dev_eui"), ((Integer)map.get("commpart")).toString(), 
						(String)map.get("name"), 
						(String)map.get("latitude"), (String)map.get("longitude"),
						(String)map.get("zipcode"), (String)map.get("zipcode_state"),
						(String)map.get("zipcode_city"), (String)map.get("zipcode_etc"),
						(String)map.get("status"),	(String)map.get("id"));
			}
			else if(if_name.equals("bpoint_history")){
				
				logger.debug("* start bpoint_history ------------------------- ");
				logger.debug(" - dev_eui : "  + map.get("dev_eui"));
				logger.debug(" - page    : "  + map.get("page"));
				logger.debug(" - count   : "  + map.get("count"));
			
				return appBpointHist.GetBpointHist((String)map.get("dev_eui"), (String)map.get("page"), (String)map.get("count"));
			}
			else if(if_name.equals("bpoint_group_modify")){
				
				logger.debug("* start bpoint_group_modify ------------------------- ");
				logger.debug(" - id : " + map.get("id"));
				
				if( (map.get("sub_list") == null) || (map.get("sub_list")=="")){ 
						nErrorCode = MakeResponse.ERROR_NOT_EXIST_IF;
						strMsg = "sub_list";
				} else {
					ArrayList<Object> list = (ArrayList<Object>) map.get("sub_list");
					
					if(list.size()==0){ 
						nErrorCode = MakeResponse.ERROR_INVALID_VALUE;
						strMsg = "sub_list";
					}
					else {
						return appBpointGroupMap.SetBpointGroupMap((String)map.get("id"), list);
					}
				}
			}
			else if(if_name.equals("bpoint_content_reload")){
				logger.debug("* start bpoint_content_reload ------------------------- ");
				logger.debug(" - id      : " + map.get("id"));
				logger.debug(" - deb_eui : " + map.get("deb_eui"));
				
				return appBpointReg.GetBpointContentReload((String)map.get("dev_eui"), (String)map.get("id"));
			}
			else if(if_name.equals("bpoint_thingplug")){
				logger.debug("* start bpoint_thingplug ------------------------- ");
				logger.debug(" - type    : " + map.get("type"));
				logger.debug(" - dev_eui : " + map.get("dev_eui"));
				
				return appBpointReg.GetBpointThingplugInfo((String)map.get("dev_eui"), (String)map.get("type"));
			}
			else if(if_name.equals("event_list")){
				
				logger.debug("* start event_list ------------------------- ");
				logger.debug(" - type    : "  + map.get("type"));
				logger.debug(" - dev_eui : "  + map.get("dev_eui"));
				logger.debug(" - page    : "  + map.get("page"));
				logger.debug(" - count   : "  + map.get("count"));
				
				return appEventHist.GetEventList((String)map.get("type"), 
						(String)map.get("dev_eui"), (String)map.get("page"), (String)map.get("count"));
			}
			else if(if_name.equals("alarm_set")){
				
				logger.debug("* start alarm_set ------------------------- ");
				logger.debug(" - x : "  + map.get("x"));
				logger.debug(" - y : "  + map.get("y"));
				logger.debug(" - z : "  + map.get("z"));
				
				return appSetting.SetAlarm((String)map.get("x"), 
						(String)map.get("y"),(String)map.get("z"), (String)map.get("id"));
			}
			else if(if_name.equals("alarm")) {
				
				logger.debug("* start alarm ------------------------- ");
				
				return appSetting.GetAlarm();
			}
			else if(if_name.equals("change_pwd")){

				logger.debug("* start change_pwd ------------------------- ");
				logger.debug(" - id : "   + map.get("id"));
				logger.debug(" - pwd : "  + map.get("pwd"));
				logger.debug(" - change_pwd : "  + map.get("change_pwd"));
				
				return appLogin.ChangePwd((String)map.get("id"), (String)map.get("pwd"), (String)map.get("change_pwd"));

			}
			else{
				nErrorCode = MakeResponse.ERROR_NOT_EXIST_IF;
				strMsg = if_name;
			}
		}catch(JsonParseException e) {
			logger.error("json parser Exception");
			e.printStackTrace();
			nErrorCode = MakeResponse.ERROR_JSON_PARSING;
		}catch(Exception e){
			logger.error("Exception");
			e.printStackTrace();
			nErrorCode = MakeResponse.ERROR_EXCEPTION;
		}
		
		if(nErrorCode > 0){
			strPayload = "" + nErrorCode;
			
			logger.info("return error code : " + nErrorCode);
			return makeResponse.MakeFailResponse(nErrorCode, strMsg, strPayload);
		}
		return returnJson;
	}
}
