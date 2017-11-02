package kr.co.rmtechs.bpoint_api.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.rmtechs.bpoint_api.config.BpointProperties;
import kr.co.rmtechs.bpoint_domain.eTHINGPLUG_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_DB_EVENT;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT_STATUS_CHAR;
import kr.co.rmtechs.bpoint_domain.mapper.BpointDomainMapper;
import kr.co.rmtechs.bpoint_domain.vo.BpointBaseVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointDevEuiVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointThingplugStepVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointVO;
import kr.co.rmtechs.bpoint_domain.vo.ThingPlugVO;

@Service
public class AppBpointReg extends AppBpoint {
	@Autowired
	BpointDomainMapper appMapper;
	
	@Autowired
	ThingPlugServiceImpl thingplugService;
	
	@Autowired
	BpointProperties bpointProperties;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private int BPOINT_LIST_DATE_RECV	= 1;
	private int BPOINT_LIST_DATE_REG	= 2;
	
	private int	BPOINT_LIST_ORDER_ASC	= 1;
	private int BPOINT_LIST_ORDER_DESC	= 2;

	private int BPOINT_LIST_TYPE_NONE	= 1;
	private int BPOINT_LIST_TYPE_GPS	= 2;
	private int BPOINT_LIST_TYPE_CITY	= 3;
	
	private int	BPOINT_LIST_ACTIVE_ALL	= 1;
	private int BPOINT_LIST_ACTIVE		= 2;
	private int BPOINT_LIST_ACTIVE_DE	= 3;
	
	private int BPOINT_LIST_STATUS_ALL	= 1;
	private int BPOINT_LIST_STATUS_LIVE	= 2;
	private int BPOINT_LIST_STATUS_FAIL	= 3;
	
	
	public String CheckArg(String action, String dev_eui, String name,
			String latitude, String longitude, String zipcode, 
			String zipcode_state, String zipcode_city,
			String zipcode_etc, String status, String id){	
		if(	action==null 		|| action=="" 	||
			dev_eui==null 			|| dev_eui=="" 	||
			name==null 			|| name =="" 	||
			latitude==null 		|| latitude==""	|| 
			longitude==null 	|| longitude==""||
			zipcode_state==null || zipcode_state==""||
			zipcode_city==null	|| zipcode_city=="" ||
			status==null 		|| status==""	||
			id==null			|| id==""	) {
			
				String strMsg="";
				
				if(action==null		|| action=="" 	) strMsg = "action,";
				if(dev_eui==null 		|| dev_eui=="" 	) strMsg += "dev_eui,";
				if(name==null 		|| name =="" 	) strMsg += "name,";
				if(latitude==null 	|| latitude==""	) strMsg += "latitude,"; 
				if(longitude==null 	|| longitude=="") strMsg += "longitude,";
				if(zipcode_state==null	|| zipcode_state=="" ) strMsg += "zipcode_state,";
				if(zipcode_city==null	|| zipcode_city==""	) strMsg += "zipcode_city";
				if(status==null 	|| status==""	) strMsg += "status,";
				if(id==null			|| id==""		) strMsg += "id,";
							
				return MakeFailResponse(ERROR_NOT_EXIST_IF, strMsg, null);
		}
		
		if((action.equals(""+eTYPE_DB_EVENT.EVENT_INSERT.getValue())!=true) 
				&& (action.equals(""+eTYPE_DB_EVENT.EVENT_UPDATE.getValue())!=true)){
			return MakeFailResponse(ERROR_INVALID_VALUE, "action", null);
		}
		else if((status.equals(""+eTYPE_EVENT_STATUS.STATUS_ADD.getValue())==false) && 
				(status.equals(""+eTYPE_EVENT_STATUS.STATUS_ACTIVATE.getValue())==false) &&
				(status.equals(""+eTYPE_EVENT_STATUS.STATUS_DEACTIVATE.getValue())==false)){
			return MakeFailResponse(ERROR_INVALID_VALUE, "active", null);
		}else if((action.equals(""+eTYPE_DB_EVENT.EVENT_UPDATE.getValue())==true) && 
				 (status.equals(""+eTYPE_EVENT_STATUS.STATUS_MODIFY.getValue())==true))
			return MakeFailResponse(ERROR_INVALID_STATUS, "active,action", null);
		
		// 2017-07-19 LG 15bytes 이슈 추가
		if(dev_eui.length() != DB_SIZE_LTID && dev_eui.length() != DB_SIZE_LG_LTID) return MakeFailResponse(ERROR_STRING_SIZE_WRONG, "dev_eui size over[" +DB_SIZE_LTID+"<"+ dev_eui.length()+"]", null);
		if(name.length() > DB_SIZE_LTID_NAME) return MakeFailResponse(ERROR_STRING_SIZE_OVER, "name size over["+DB_SIZE_LTID_NAME+"<"+name.length()+"]" , null);
//		if(zipcode.length()!= DB_SIZE_ZIPCODE)return MakeFailResponse(ERROR_STRING_SIZE_WRONG, "zipcode", null);
		if(zipcode_state.length() > DB_SIZE_ZIPCODE_STATE)return MakeFailResponse(ERROR_STRING_SIZE_OVER, "zipcode_state size over["+DB_SIZE_ZIPCODE_STATE+"<"+zipcode_state.length()+"]", null);
		if(zipcode_city.length() > DB_SIZE_ZIPCODE_CITY) return MakeFailResponse(ERROR_STRING_SIZE_OVER, "zipcode_city size over["+DB_SIZE_ZIPCODE_CITY+"<" + zipcode_city.length() +"]", null);
		if(id.length() > DB_SIZE_ID)return MakeFailResponse(ERROR_STRING_SIZE_OVER, "id["+DB_SIZE_ID+"<"+id.length() +"]" , null);

		if ( checkZipcodeState(zipcode_state) == false)
			return MakeFailResponse(ERROR_DONT_HAVE_ZIPCODE_STATE, "zipcode_state : " + zipcode_state, null);

		return null;
	}
	
	public String IsUpdateArg(BpointVO vo, String name,  String latitude, String longitude, 
			String zipcode, String zipcode_state, String zipcode_city, 
			String zipcode_etc, String status){

		String strMsg = new String(); 
	
		if(vo.getBpoint_name().equals(name)==false)					strMsg = "name:"+name+",";
		if(vo.getBpoint_latitude() != Double.parseDouble(latitude)) strMsg += "위도:"+latitude+",";
		if(vo.getBpoint_longitude() != Double.parseDouble(longitude)) strMsg += "경도:"+longitude+",";
		if(isEqualString(vo.getBpoint_zipcode(), zipcode))			strMsg += "우편번호:"+zipcode+",";
		if(isEqualString(vo.getBpoint_addr_state(), zipcode_state)) strMsg += "시/도 : " +zipcode_state+",";
		if(isEqualString(vo.getBpoint_addr_city(), zipcode_city)) 	strMsg += "주소 : " +zipcode_city+",";
		if(isEqualString(vo.getBpoint_addr_etc(), zipcode_etc))		strMsg += "나머지주소:"+zipcode_etc+",";			
		if( status.equals(""+vo.getBpoint_status()) == true )		strMsg += "상태:"+status+"";
		if( (strMsg != null) && (strMsg.length()>0) ) return strMsg;
		
		return null;
	}
	
	@SuppressWarnings("unused")
	public String SetBpointReg(String action, String dev_eui, String commpart, String name,
			String latitude, String longitude, String zipcode, String zipcode_state, 
			String zipcode_city, String zipcode_etc, String status, String id) {
		
		String checkArg = CheckArg(action, dev_eui, name, latitude, longitude, zipcode, 
				zipcode_state, zipcode_city, zipcode_etc, status, id);
		if(checkArg!=null) return checkArg;

		try{
			
			if(isAdminRank(appMapper,id)==false)
				return MakeFailResponse(ERROR_NOT_EXIST_PRIVILEGE, "id : "+id, null);
			
			BpointVO vo = appMapper.getLtid(dev_eui);
			boolean bExist = true;
			boolean bModify = true;
			String strMsg = null;
			
			if(vo == null) {	
				bExist = false;
				vo = new BpointVO();
				vo.setBpoint_ltid(dev_eui);
				vo.setId_insert(id);		
			}
			else {			
				vo.setId_update(id);
				
				strMsg = IsUpdateArg(vo, name, latitude, longitude, zipcode, 
						zipcode_state, zipcode_city, zipcode_etc, status);
			}
			
			vo.setBpoint_name(name);
			vo.setBpoint_latitude(Double.parseDouble(latitude) );
			vo.setBpoint_longitude(Double.parseDouble(longitude));
			vo.setCommpart(Integer.parseInt(commpart));
			
			if((zipcode!=null) && (zipcode!="")) 			vo.setBpoint_zipcode(zipcode);
			if((zipcode_state!=null) && (zipcode_state!="")) vo.setBpoint_addr_state(zipcode_state);
			if((zipcode_city!=null) && (zipcode_city!="")) 	vo.setBpoint_addr_city(zipcode_city);
			if((zipcode_etc!=null) && (zipcode_etc!="")) 	vo.setBpoint_addr_etc(zipcode_etc);

			setStatus(vo, status);
			
			int nResult;
			if(action.equals(""+eTYPE_DB_EVENT.EVENT_INSERT.getValue())){	
				if(bExist)	return MakeFailResponse(ERROR_ALREADY_EXIST, "dev_eui:"+dev_eui, null);
				
				int nThingplugCount = appMapper.getThingplugCount(dev_eui);
				
				nResult = appMapper.insertBpoint(vo);
				
				logger.info("count rm_bpoint_thingplug_map dev_eui : " + dev_eui + ",count:"+nThingplugCount);
				
				if(nThingplugCount==0){
					int nCount = appMapper.insertThingplug(dev_eui);
					logger.info("insert rm_bpoint_thingplug_map dev_eui : " + dev_eui + ",count:"+nCount);
				}
								
				if(nResult > 0)	{				
					strMsg = InsertEventAndHistory(appMapper, eTYPE_EVENT.TYPE_EVENT_LTID, dev_eui, 
							eTYPE_EVENT_STATUS.STATUS_ADD, "dev_eui["+dev_eui+"]를 등록하였습니다.", 
							"dev_eui["+dev_eui+"], name : " + name +" 를 등록하였습니다.", id,
							eTHINGPLUG_STATUS.BPOINT_ADD, "recv app add dev_eui["+dev_eui+"]를 등록하였습니다."); 
					
					if(strMsg!=null) logger.warn("setEventHist : "+strMsg);
					return getLtidAndGroupMap(appMapper, dev_eui, true);
				}
				else return MakeFailResponse(ERROR_INSERT_FAIL, "dev_eui : "+dev_eui, null);
				
			} else { 
				if(!bExist)	return MakeFailResponse(ERROR_QUERY_NO_DATA, " ltid : "+dev_eui, null);

				if(strMsg!=null) {
						nResult = appMapper.appUpdateBpoint(vo);
						if(nResult>0){	
							strMsg = InsertEventHist(appMapper,  eTYPE_EVENT.TYPE_EVENT_LTID, dev_eui, 
									eTYPE_EVENT_STATUS.STATUS_MODIFY,	"dev_eui["+dev_eui+"]를 변경하였습니다.", 
										"dev_eui["+dev_eui+"], name : " + name + "로 변경하였습니다.", id);
							if(strMsg!=null) logger.warn("setEventHist : " + strMsg);
							
							return getLtidAndGroupMap(appMapper, dev_eui, false);
						}
						else    return MakeFailResponse(ERROR_UPDATE_FAIL, "dev_eui : " + dev_eui, null);
				}
				return getLtidAndGroupMap(appMapper, dev_eui, false);
			}
			
		}catch(Exception e)	{
			logger.error(e.toString());
		}
		
		return DEFAULT_ERROR_STRING;
	}
	
	public String CheckArgBpointStatus(String status, String dev_eui, String id){
		
		if(	dev_eui==null 		|| dev_eui=="" 	||
			status==null 	|| status==""	||
			id==null		|| id==""	) {
				
			String strMsg="";
					
			if(dev_eui==null 	|| dev_eui=="" 	) strMsg += "dev_eui,";
			if(status==null || status==""	) strMsg += "status,";
			if(id==null			|| id==""		) strMsg += "id,";
								
				return MakeFailResponse(ERROR_NOT_EXIST_IF, strMsg, null);
			}
			
		if( (status.equals(""+eTYPE_EVENT_STATUS.STATUS_ACTIVATE.getValue()) == false ) &&
			(status.equals(""+eTYPE_EVENT_STATUS.STATUS_DEACTIVATE.getValue()) == false) &&
			(status.equals(""+eTYPE_EVENT_STATUS.STATUS_DELETE.getValue())==false) )
			return MakeFailResponse(ERROR_INVALID_VALUE, "status : "+status, null);
			
		return null;
	}
	
	public String SetBpointStatus(String status, String dev_eui, String id) {
	
		String checkArg = CheckArgBpointStatus(status, dev_eui, id);
		if(checkArg!=null) return checkArg;
		
		try{
			if(isAdminRank(appMapper,id) == false)
				return MakeFailResponse(ERROR_NOT_EXIST_PRIVILEGE, "id : "+id, null);
			
			BpointVO vo = appMapper.getLtid(dev_eui);
			if(vo == null) return MakeFailResponse(ERROR_QUERY_NO_DATA, "dev_eui : "+dev_eui, null);
			
			logger.debug("status : " + status + ", old_status:" + vo.getBpoint_status());		

			boolean thingplug_flag = true;
			int nResult = 0;
			eTYPE_EVENT_STATUS event_status = eTYPE_EVENT_STATUS.STATUS_ACTIVATE;
			
			char oldStatus = vo.getBpoint_status();
			if(status.equals(""+vo.getBpoint_status()) == true){
				logger.debug("just return status");
				return MakeSuccessResponse(RET_SUCCESS, "dev_eui:"+dev_eui + " 변경을 성공하였습니다.", vo);
			}
			

			if(status.equals(""+eTYPE_EVENT_STATUS.STATUS_DELETE.getValue())){
				event_status = eTYPE_EVENT_STATUS.STATUS_DELETE;
				
				if(bpointProperties.existDevEui(dev_eui)==true){
					int nThingPlugCount = appMapper.getThingplugCount(dev_eui);
					logger.info("SetBpointStatus thingplug_count dev_eui : "+dev_eui+",count:"+nThingPlugCount);
				
					Map<String, Object> retMap = thingplugService.SendThingPlug_App(dev_eui,  eTHINGPLUG_STATUS.SUBSCRIPTION_RETRIEVE,
							eTHINGPLUG_STATUS.SUBSCRIPTION_RETRIEVE, "recv delete api", id, false);
				
					thingplug_flag = (boolean)retMap.get("success");
					if(thingplug_flag==true){ 
						retMap = thingplugService.SendThingPlug_App(dev_eui,  eTHINGPLUG_STATUS.SUBSCRIPTION_DEACTIVE,
								eTHINGPLUG_STATUS.STATUS_DEACTIVE_FINISHED, "recv delete api", id, false);
						thingplug_flag = (boolean)retMap.get("success");
					}
				}
							
				nResult = appMapper.deleteBpoint(dev_eui);
				int nCount = appMapper.deleteThingplug(dev_eui);
				logger.info("delete dev_eui : " + dev_eui  + ", main table : " + nResult + ", map table : "+nCount);
				
				BpointDevEuiVO tmpVo = new BpointDevEuiVO();
				tmpVo.setBpoint_ltid(dev_eui);
				
				if(nResult==1) {

					String strMsg = MakeResponse.InsertEventAndHistory(appMapper, eTYPE_EVENT.TYPE_EVENT_LTID, dev_eui, 
							event_status, "dev_eui["+dev_eui+"]를 삭제하였습니다.",	
							"dev_eui["+dev_eui+"]를 삭제하였습니다.", id,
							eTHINGPLUG_STATUS.BPOINT_DELETE, "recv app delete dev_eui["+dev_eui+"]를 삭제하였습니다."); 

					if(strMsg != null)	logger.warn("InsertEventHist : " + strMsg);

					return MakeSuccessResponse(RET_SUCCESS,
							"dev_eui : "+dev_eui+"를 삭제하였습니다.", tmpVo); 
				}
				else return MakeFailResponse(RET_SUCCESS,"dev_eui : "+dev_eui+" 삭제를 실패하하였습니다.", tmpVo);
			}
			
			
			if(status.equals(""+eTYPE_EVENT_STATUS.STATUS_ACTIVATE.getValue())){
				
				vo.setBpoint_status(eTYPE_EVENT_STATUS_CHAR.STATUS_ACTIVATE_CHAR.getValue());
				if(oldStatus==eTYPE_EVENT_STATUS_CHAR.STATUS_DEACTIVATE_CHAR.getValue() &&
						bpointProperties.existDevEui(dev_eui)==true){
					
					Map<String, Object> retMap = thingplugService.SendThingPlug_App(dev_eui, eTHINGPLUG_STATUS.SUBSCRIPTION_RETRIEVE,
								eTHINGPLUG_STATUS.SUBSCRIPTION_RETRIEVE, "recv modify api", id, false);
					thingplug_flag = (boolean)retMap.get("success");
					if(thingplug_flag==true){	// 존재하는 경우 update
						retMap = thingplugService.SendThingPlug_App(dev_eui, eTHINGPLUG_STATUS.SUBSCRIPTION_UPDATE,
								eTHINGPLUG_STATUS.STATUS_FINISHED, "recv modify api", id, false);
						thingplug_flag = (boolean)retMap.get("success");
					}else{
						retMap = thingplugService.SendThingPlug_App(dev_eui, eTHINGPLUG_STATUS.SUBSCRIPTION_ACTIVE,
								eTHINGPLUG_STATUS.STATUS_FINISHED, "recv modify api", id, false);
						thingplug_flag = (boolean)retMap.get("success");
					}
				}
				event_status = eTYPE_EVENT_STATUS.STATUS_ACTIVATE;

			}
			else if(status.equals(""+eTYPE_EVENT_STATUS.STATUS_DEACTIVATE.getValue())){
				
				vo.setBpoint_status(eTYPE_EVENT_STATUS_CHAR.STATUS_DEACTIVATE_CHAR.getValue());
				event_status = eTYPE_EVENT_STATUS.STATUS_DEACTIVATE;
				if(oldStatus==eTYPE_EVENT_STATUS_CHAR.STATUS_ACTIVATE_CHAR.getValue()&&
						bpointProperties.existDevEui(dev_eui)==true){
					
					Map<String, Object> retMap = thingplugService.SendThingPlug_App(dev_eui, eTHINGPLUG_STATUS.SUBSCRIPTION_RETRIEVE,
							eTHINGPLUG_STATUS.SUBSCRIPTION_RETRIEVE, "recv modify api", id, false);
					thingplug_flag = (boolean)retMap.get("success");
					if(thingplug_flag == true){
						retMap = thingplugService.SendThingPlug_App(dev_eui, eTHINGPLUG_STATUS.SUBSCRIPTION_DEACTIVE,
							eTHINGPLUG_STATUS.STATUS_FINISHED, "recv modify api", id, false);
						thingplug_flag = (boolean)retMap.get("success");
					}
				}
			}
			
			vo.setId_update(id);
			
			nResult = appMapper.appUpdateBpointStatus(vo);
			if(nResult>0){				
				String strMsg = InsertEventHist(appMapper,  eTYPE_EVENT.TYPE_EVENT_LTID, dev_eui, 
						event_status,	"dev_eui["+dev_eui+"]를 변경하였습니다.", 
						"status : "+oldStatus + " ==> "+status+" 로 변경하였습니다.", id);
				if(strMsg!=null) logger.warn("InsertEventHist : " + strMsg);
				
				return MakeSuccessResponse(RET_SUCCESS,"dev_eui : "+dev_eui+",status : "+status+" 로 변경하였습니다.",vo);
			}
			else return MakeFailResponse(ERROR_UPDATE_FAIL, "dev_eui : " + dev_eui, null);		
		}catch(Exception e)	{
			logger.error(e.toString());
		}
		return DEFAULT_ERROR_STRING;
	}
	
	public String CheckArgBpointList(String date, String order, String type,
			String latitude, String longitude, String value, String active, 
			String fail_status, String group, String page, String count, String id) {
		String strMsg="";
		if(	date==null 		|| date=="" 	||
			order==null 	|| order==""	||
			type==null		|| type==""		||
			active==null	|| active==""	||
			fail_status==null	|| fail_status==""	||
			page==null		|| page ==""	||
			count==null		|| count==""	||
			id==null		|| id=="") {
					
			if(date==null 	|| date=="" 	) strMsg += "event_date,";
			if(order==null 	|| order==""	) strMsg += "order,";
			if(type==null	|| type==""		) strMsg += "type,";
			if(active==null || active==""	) strMsg += "active,";
			if(fail_status==null || fail_status==""	) strMsg += "fail_status,";
			if(page==null	|| page==""		) strMsg += "page,";
			if(count==null	|| count==""	) strMsg += "count,";
			if(id==null		|| id==""		) strMsg += "id";	
			
			return MakeFailResponse(ERROR_NOT_EXIST_IF, strMsg, null);
		}
		
		if( (date.equals(""+BPOINT_LIST_DATE_RECV)!= true) &&
			(date.equals(""+BPOINT_LIST_DATE_REG)!= true) )
			 return MakeFailResponse(ERROR_INVALID_VALUE, "date : " + date, null);

		if( (order.equals(""+BPOINT_LIST_ORDER_ASC)!= true) &&
			(order.equals(""+BPOINT_LIST_ORDER_DESC)!= true) )
			 return MakeFailResponse(ERROR_INVALID_VALUE, "order : " + order, null);	

		if( (type.equals(""+BPOINT_LIST_TYPE_NONE)!= true) &&
			(type.equals(""+BPOINT_LIST_TYPE_GPS)!= true) &&
			(type.equals(""+BPOINT_LIST_TYPE_CITY)!= true) )
			return MakeFailResponse(ERROR_INVALID_VALUE, "type : " + type, null);	

		if( type.equals(""+BPOINT_LIST_TYPE_GPS) &&
			(value == null || value == "" || 
			 latitude == null || latitude == "" ||
			 longitude == null || longitude=="" ||
			 (Integer.parseInt(value)<=0) ||
			 (Double.parseDouble(latitude)<=0) ||
			 (Double.parseDouble(longitude)<=0) ) )
			return MakeFailResponse(ERROR_NOT_EXIST_IF, "value", null); 
		
		if ( type.equals(""+BPOINT_LIST_TYPE_CITY) &&
				(checkZipcodeState(value) == false) )
			return MakeFailResponse(ERROR_DONT_HAVE_ZIPCODE_STATE, "value : "+ value, null);
		
		if( (active.equals(""+BPOINT_LIST_ACTIVE_ALL)!= true) &&
			(active.equals(""+BPOINT_LIST_ACTIVE)!= true) &&
			(active.equals(""+BPOINT_LIST_ACTIVE_DE)!= true) )
			 return MakeFailResponse(ERROR_INVALID_VALUE, "active : " + active, null);
		
		if( (fail_status.equals(""+BPOINT_LIST_STATUS_ALL)!= true) &&
			(fail_status.equals(""+BPOINT_LIST_STATUS_LIVE)!= true) &&
			(fail_status.equals(""+BPOINT_LIST_STATUS_FAIL)!= true) )
			 return MakeFailResponse(ERROR_INVALID_VALUE, "status : " + fail_status, null);	

		if( (order.equals(""+BPOINT_LIST_ORDER_ASC)!= true) &&
			(order.equals(""+BPOINT_LIST_ORDER_DESC)!= true) )
		 return MakeFailResponse(ERROR_INVALID_VALUE, "order : " + order, null);	

		
		return null;
	}
	
	public String GetBpointList(String date, String order, 
			String type, String argLatitude, String argLongitude, String argValue, String active, 
			String fail_status, String argGroupNo, String argPage, String argCount, String id, 
			String if_id) {
				
		String checkArg = CheckArgBpointList(date, order, type, argLatitude, argLongitude,
				argValue,	active, fail_status, argGroupNo, argPage, argCount, id );
		if(checkArg!=null) return checkArg;
		int nPos=0;
		
		try{
			int group_no = 0;
			logger.debug("argGroupNo : " + argGroupNo);
			
			if(argGroupNo!=null) {
				group_no= Integer.parseInt(argGroupNo);
				logger.debug("group_no : " + argGroupNo +","+ group_no);
			}
			int count  = Integer.parseInt(argCount);
			int page = (Integer.parseInt(argPage)-1)*count;
					
			double latitude = 0;
			double longitude = 0;
			int value = 0;
			if((argLatitude!=null) && (argLatitude!="")) latitude = Double.parseDouble(argLatitude);
			if((argLongitude!=null) && (argLongitude!="")) longitude = Double.parseDouble(argLongitude);
			if((argValue!=null) && (argValue!="")) value = Integer.parseInt(argValue);
			
			nPos = 1;
			
			int nMemberGroupNo = getGroupNo(appMapper, id);	
			if(nMemberGroupNo==RET_FAIL)
				return MakeFailResponse(ERROR_NOT_EXIST_ID, "id : "+id, null);

			else if(nMemberGroupNo==USER_DEFAULT_NON_GROUP_NO)
				return MakeFailResponse(ERROR_DONT_HAVE_GROUP, "id : "+id, null); 

			nPos = 2;
						
			if(if_id.equals("bpoint_base_list")){
				List<BpointBaseVO> vo = appMapper.getBpointBaseList(date, order, type, 
						value, latitude, longitude, active, 
						fail_status, group_no, page, count, nMemberGroupNo);
				
				if((vo==null) || (vo.size()==0))
					return MakeSuccessResponse(ERROR_QUERY_NO_DATA, MSG_QUERY_NO_DATA, null);
				
				for(BpointBaseVO subVo : vo)
					 getLtidAndGroupMap(appMapper, subVo);
				
				return MakeSuccessResponse(RET_SUCCESS, null, vo);
			}
			else {	
				List<BpointVO> childVo = appMapper.getBpointList(date, order, type, 
						value, latitude, longitude, active, 
						fail_status, group_no, page, count, nMemberGroupNo);
				for(BpointVO subVo : childVo){
					 if(subVo.getBpoint_event_time()==null) subVo.setBpoint_event_time("");
					 if(subVo.getBpoint_addr_state()==null) subVo.setBpoint_addr_state("");
					 if(subVo.getBpoint_addr_city()==null)	subVo.setBpoint_addr_city("");
					 if(subVo.getBpoint_addr_etc()==null)	subVo.setBpoint_addr_etc("");				 
				}
				
				if((childVo==null) || (childVo.size()==0))
					return MakeSuccessResponse(ERROR_QUERY_NO_DATA, MSG_QUERY_NO_DATA, null);
				
				for(BpointBaseVO subVo : childVo){
					 getLtidAndGroupMap(appMapper, subVo);
				}
				
				return MakeSuccessResponse(RET_SUCCESS, null, childVo);
			}
			
		}catch(Exception e)	{
			logger.error(e.toString());
		}
		return DEFAULT_ERROR_STRING;
	}
	
	public String GetBpointDetail(String dev_eui, String id){
		try{
			if((id==null) || (id==""))	return MakeFailResponse(ERROR_NOT_EXIST_IF, "id", null);
			if((dev_eui==null) || (dev_eui==""))	return MakeFailResponse(ERROR_NOT_EXIST_IF, "dev_eui", null);
			
			if(isPartOpRank(appMapper,id) == true){
				int nCount = appMapper.getMemberAndLtidGroupMapCount(dev_eui, id);
				if(nCount==0) return MakeFailResponse(ERROR_DONT_HAVE_GROUP_LTID, "dev_eui:"+dev_eui+",id:"+id, null);
			}
			
			BpointVO vo = appMapper.getLtid(dev_eui);
			if(vo == null) return MakeFailResponse(ERROR_QUERY_NO_DATA, "dev_eui : "+dev_eui, null);
			
			getLtidAndGroupMap(appMapper, (BpointBaseVO)vo);
			
			return MakeSuccessResponse(RET_SUCCESS, null, vo);
			
		}catch(Exception e){
			logger.error(e.toString());
		}
		
		return DEFAULT_ERROR_STRING;
	}
	
	public String GetBpointContentReload(String dev_eui, String id){
		try{	
			if((dev_eui==null) || (dev_eui==""))	return MakeFailResponse(ERROR_NOT_EXIST_IF, "dev_eui", null);
				
			BpointDevEuiVO devVO = new BpointDevEuiVO();
			devVO.setBpoint_ltid(dev_eui);

			if((id==null) || (id==""))				return MakeFailResponse(ERROR_NOT_EXIST_IF, "id", devVO);
			if(bpointProperties.existDevEui(dev_eui) == false)
				return MakeFailResponse(ERROR_NOT_SUPPORT_DEV_EUI, dev_eui, devVO);
			
			int count = appMapper.getLtidCount(dev_eui);
			
			if(count == 0) return MakeFailResponse(ERROR_QUERY_NO_DATA, "dev_eui : "+dev_eui, devVO);
			
			Map<String, Object> retMap  = thingplugService.SendThingPlug_App(dev_eui, 
					eTHINGPLUG_STATUS.CONTAINER_LORA, 
					eTHINGPLUG_STATUS.STATUS_FINISHED, "recv api reload", id, true) ;
			if((boolean)retMap.get("success")==true) return MakeSuccessResponse(RET_SUCCESS, null, devVO);
			else return MakeFailResponse(ERROR_THINGPLUG_FAIL, null, devVO);
			
		}catch(Exception e){
			logger.error(e.toString());
		}
		
		return DEFAULT_ERROR_STRING;
	}
	
		
	public String GetBpointThingplugInfo(String dev_eui, String type){
		try{
			if((dev_eui==null) || (dev_eui==""))	return MakeFailResponse(ERROR_NOT_EXIST_IF, "dev_eui", null);

			BpointDevEuiVO vo = new BpointDevEuiVO();
			vo.setBpoint_ltid(dev_eui);
				
			if((type==null) || (type==""))			return MakeFailResponse(ERROR_NOT_EXIST_IF, "type", vo);
			if((type.equals("1")!=true) && (type.equals("2")!=true)) return MakeFailResponse(ERROR_INVALID_VALUE, "type : " + type, vo);
				
			ThingPlugVO thingplugVO = new ThingPlugVO();
			
			if(type.equals("1")==true)	thingplugVO = appMapper.getThingPlug(dev_eui);
			else			thingplugVO = appMapper.getThingPlugStatus(dev_eui);

			
			if(thingplugVO.getBpoint_ltid() == null) return MakeFailResponse(ERROR_QUERY_NO_DATA, "dev_eui : "+dev_eui, vo);

			return MakeSuccessResponse(RET_SUCCESS, null, thingplugVO);
			
		}catch(Exception e){
			logger.error(e.toString());
		}
		
		return DEFAULT_ERROR_STRING;
	}
	
	
}
