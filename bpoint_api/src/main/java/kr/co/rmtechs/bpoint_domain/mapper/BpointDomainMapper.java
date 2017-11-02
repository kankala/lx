package kr.co.rmtechs.bpoint_domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.rmtechs.bpoint_api.config.BpointProperties;
import kr.co.rmtechs.bpoint_domain.vo.BpointBaseVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointContentVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointGroupMapVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointHistVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointVO;
import kr.co.rmtechs.bpoint_domain.vo.EventHistVO;
import kr.co.rmtechs.bpoint_domain.vo.GroupVO;
import kr.co.rmtechs.bpoint_domain.vo.MemberVO;
import kr.co.rmtechs.bpoint_domain.vo.SettingVO;
import kr.co.rmtechs.bpoint_domain.vo.ThingPlugVO;
import kr.co.rmtechs.bpoint_domain.vo.UserVO;
import kr.co.rmtechs.bpoint_domain.vo.VersionVO;

@Mapper
public interface BpointDomainMapper {
	
	// test 를 위함
	UserVO 		findAll();
	
	//get_version
	VersionVO getLastVersion(String os);
	// login fail..
	int 		checkLogin(MemberVO vo);
	int 		updateLogin(MemberVO vo);
	MemberVO	getUserRank(@Param("member_id") String member_id);
	MemberVO	getUser(@Param("member_id") String member_id);
	
	//group_list
	List<GroupVO> getGroupList();
	
	//bpoint_list_base
	List<BpointBaseVO> getBpointBaseList(@Param("event_date") String date, @Param("order") String order,
					@Param("type") String type,	@Param("value") int value,
					@Param("latitude")double latitude, @Param("longitude") double longitude,
					@Param("active") String active,	@Param("status") String status,	
					@Param("group_no") int group_no, @Param("start") int start,	
					@Param("count") int count, @Param("member_group_no") int member_group_no);	
	//bpoint_list
	List<BpointVO> getBpointList(@Param("event_date") String date, @Param("order") String order,
					@Param("type") String type,	@Param("value") int value,
					@Param("latitude")double latitude, @Param("longitude") double longitude,
					@Param("active") String active,	@Param("status") String status,	
					@Param("group_no") int group_no, @Param("start") int start,	
					@Param("count") int count, @Param("member_group_no") int member_group_no);			
	//bpoint_modify
	int			appUpdateBpointStatus(BpointVO vo);
	int			deleteBpoint(@Param("bpoint_ltid")String bpoint_ltid);
	// bpoint_reg 
	int 		getLtidCount(String ltid);
	BpointVO	getLtid(@Param("bpoint_ltid") String ltid);
	int			getMemberAndLtidGroupMapCount(@Param("bpoint_ltid") String ltid, @Param("id") String id);
	int 		insertBpoint(BpointVO vo);
	int 		appUpdateBpoint(BpointVO vo);
	List<BpointGroupMapVO> getBpointGroupMap(@Param("bpoint_ltid") String ltid);
	//bpoint_history
	List<BpointHistVO> getBpointHist(@Param("bpoint_ltid") String ltid, 
				@Param("table_name") String table_name,
				@Param("start") int start, @Param("count") int count);
	int 		insertBpointHist(BpointHistVO bpointHist);
	
	//bpoint_group_modify
	int	insertGroupMap(@Param("ltidGroupMap") List<BpointGroupMapVO> ltidGroupMap);
	int	deleteGroupMap(@Param("ltidGroupMap") List<BpointGroupMapVO> ltidGroupMap);
	int getLtidGroupMapCount(@Param("ltidGroupMap") List<BpointGroupMapVO> ltidGroupMap);
	
	//event_list
	int insertEvent(EventHistVO vo);
	List<EventHistVO> getEventList(@Param("type") String type, @Param("ltid") String ltid,
			@Param("total") int total, @Param("count") int count);
	//alarm_set
	int 		appUpdateSetting(@Param("value1") String value1, @Param("value2") String value2, 
			@Param("value3") String value3, @Param("id") String id, @Param("no") int no);
	//alarm 
	SettingVO 	getSetting(int no);
	//change_pwd
	int 		changePwd(MemberVO vo);
	int			updateBpointFailStatus(BpointVO vo);
	
	int			setEventList(@Param("event_read_status")char event_read_status, 
				@Param("event_seq") int event_seq);
	
	int			setPushResult(@Param("send_status") char send_status, @Param("response") String response, 
				@Param("succ_count") int succ_count, @Param("fail_count") int fail_count,
				@Param("event_seq") int event_seq);
	
	List<String> getMemberToken();
	
	int			setBpointBaseContent(BpointContentVO vo);
	
	List<String> getLtidForThingPlugNode(BpointProperties vo);
	List<String> getLtidForThingPlugRemoteCES(BpointProperties vo);
	List<String> getLtidForThingPlugContainer(BpointProperties vo);
	List<String> getLtidForThingPlugSubscription(BpointProperties vo);
	List<String> getLtidForThingPlugSubscriptionDelete(BpointProperties vo);
	
	int			getThingplugCount(@Param("bpoint_ltid")String bpoint_ltid);
	int			deleteThingplug(@Param("bpoint_ltid")String bpoint_ltid);
	int			insertThingplug(@Param("bpoint_ltid")String bpoint_ltid);
	int			setThingplugResponse(@Param("bpoint_ltid")String bpoint_ltid,
				@Param("thingplug_status") int thingplug_status,
				@Param("db_status") int db_status,
				@Param("response_status")int response_status, @Param("body")String body);

	ThingPlugVO		  getThingPlug(@Param("bpoint_ltid")String bpoint_ltid);
	ThingPlugVO		  getThingPlugStatus(@Param("bpoint_ltid")String bpoint_ltid);
}
