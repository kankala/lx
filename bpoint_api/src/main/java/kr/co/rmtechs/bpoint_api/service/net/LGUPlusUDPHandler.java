package kr.co.rmtechs.bpoint_api.service.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import kr.co.rmtechs.bpoint_api.service.MakeResponse;
import kr.co.rmtechs.bpoint_api.service.UDPService;
import kr.co.rmtechs.bpoint_domain.BPOINT_RESOURCE;
import kr.co.rmtechs.bpoint_domain.eTHINGPLUG_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT;
import kr.co.rmtechs.bpoint_domain.eTYPE_EVENT_STATUS;
import kr.co.rmtechs.bpoint_domain.eTYPE_SETTING;
import kr.co.rmtechs.bpoint_domain.vo.BasePacket;
import kr.co.rmtechs.bpoint_domain.vo.BpointContentVO;
import kr.co.rmtechs.bpoint_domain.vo.BpointVO;
import kr.co.rmtechs.bpoint_domain.vo.SettingVO;
import kr.co.rmtechs.commons.Utils;

@Configuration
@Component
public class LGUPlusUDPHandler implements IoHandler {
	
	private Logger logger = Logger.getLogger(LGUPlusUDPHandler.class);
	
	public static final int BUFFER_READ		= 128;
	
	public LGUPlusUDPHandler() {
	}
	
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		if(cause instanceof IOException) {
			logger.error("IO EXCEPTION THROWS (" + session.getRemoteAddress() + ") - " + cause.getMessage());
			sessionClosed(session);			
		}else {
			logger.error("EXCEPTION THROWS (" + session.getRemoteAddress() + ") - " + cause.toString());
			cause.printStackTrace();
		}
	}
	
	@Override
	public void inputClosed(IoSession session) throws Exception {
	}	

	public void messageSent(IoSession session, Object obj) throws Exception {
		if(obj instanceof byte[]) {
			byte[] buf = (byte[])obj;
			logger.debug("MESSAGE SEND TO : " + session.getRemoteAddress() + " - " + Utils.byteArrayToHex(buf));
		}
//		System.out.println("MESSAGE SEND TO : " + session.getRemoteAddress() + " :: " + obj);
	}

	public void sessionClosed(IoSession session) throws Exception {
		InetSocketAddress addr = (InetSocketAddress)session.getRemoteAddress();
		logger.debug(session.getRemoteAddress() + " Session closed.");
		
//		ModelManager.getInstance().removeScanner(session);
		
		if(session != null) {
			session.close(true);
			session = null;
		}
	}

	public void sessionCreated(IoSession session) throws Exception {
		InetSocketAddress addr = (InetSocketAddress)session.getRemoteAddress();
		logger.debug(session.getRemoteAddress() + " WAS ACCEPTED!!!!");
		
		((DatagramSessionConfig)session.getConfig()).setReceiveBufferSize(BUFFER_READ);
//		((DatagramSessionConfig)session.getConfig()).setKeepAlive(true);
//		((DatagramSessionConfig)session.getConfig()).setSoLinger(0);
//		((DatagramSessionConfig)session.getConfig()).setTcpNoDelay(true);
		
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, LGUPlusServer.DEFAULT_IDLE_TIME);
		session.getConfig().setIdleTime(IdleStatus.READER_IDLE, LGUPlusServer.DEFAULT_IDLE_TIME);
		session.getConfig().setIdleTime(IdleStatus.WRITER_IDLE, LGUPlusServer.DEFAULT_IDLE_TIME);
		
		IoBuffer buf = IoBuffer.allocate(2);
		buf.setAutoExpand(true);
		
//		EifEquipVO vo = ModelManager.getInstance().getScanner(session);
//		if(vo == null) {
//			// logger.debug(session.getRemoteAddress() + " Not registered Scanner...!!!");
//		}else {
//			// 함체가 이미 존재하는 경우 시스템 정보를 업데이트 한다.
//			ModelManager.getInstance().writeSystemInfo(session);
//		}
		
		// TODO 생성된 Session 정보 처리
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		if(status == IdleStatus.BOTH_IDLE) {
			logger.debug("SESSION IDLE... CLOSE SESSION :: " + session.getRemoteAddress());
			sessionClosed(session);
//		}else if(status == IdleStatus.READER_IDLE) {
//			logger.debug("SESSION READ IDLE... CLOSE SESSION :: " + session.getRemoteAddress());
//			sessionClosed(session);
//		}else if(status == IdleStatus.WRITER_IDLE) {
//			logger.debug("SESSION WRITE IDLE... CLOSE SESSION :: " + session.getRemoteAddress());
//			sessionClosed(session);
		}
	}

	public void sessionOpened(IoSession session) throws Exception {
	}

	public void messageReceived(IoSession session, Object obj) throws Exception {
		// TODO Parsing Received packet data
		
		if(obj instanceof BasePacket) {
			final BasePacket _packet = (BasePacket)obj;
			if(_packet.getResponseCode().equals(BasePacket.RESPONSE_OK)) {

				// Parsing Data and event do
				
				
				
				// write Packet data to DB
//				EqUnit unit = UDPService.getInstance().getPacketService().searchUnit(_packet);
//				if(unit == null) {
//					_packet.setLog_cd("L_" + System.currentTimeMillis());
//					_packet.setResponseCode(BasePacket.RESPONSE_NOT_EXISTS_UNIT);
//				}else {
//					_packet.setLog_cd("L_" + System.currentTimeMillis());
//					_packet.setSite_cd(unit.getSite_cd());
//					_packet.setGroup_cd(unit.getGroup_cd());
//					_packet.setComp_cd(unit.getTele_comp());
//				}
//
//				UDPService.getInstance().getPacketService().insertPacket(_packet);
				
				parsingDataV10(_packet);
			}
			
			session.write(_packet);
			session.closeOnFlush();
		}else {
			// throw Exception
		}
	}
	
	
	
	private void parsingDataV10(BasePacket packet) {
		try {
			// gara
//			packet.setUnit_cd("1111222233330010");
			
			System.out.println("[NB-IoT] PACKET RECVED : " + packet.getUnit_cd() + " == " + packet.getUnit_cd().length() + " -- " + Utils.byteArrayToHex(packet.getUnit_cd().getBytes()));
			
			BpointVO vo = UDPService.getInstance().getAppMapper().getLtid(packet.getUnit_cd().trim());
			if(vo == null){
				System.out.println("[NB-IoT] No ID in Database : " + packet.getUnit_cd());
				// No data
				return;
//				logger.warn("can't search X-M2M-RI header ltid : " + ltid + ",dev_eui: " + dev_eui+ ", body :"+strBody);
//				return;
			}
			
			// 미등록/비활성상태의 경계점은 사용하지 않는다. 
			if(vo.getBpoint_status() == '0' || vo.getBpoint_status() == '3') {
				System.out.println("[NB-IoT] " + packet.getUnit_cd() + " not registered or unactivity state");
				return;
			}
			
	
			SettingVO setVo = UDPService.getInstance().getAppMapper().getSetting(eTYPE_SETTING.ALARM.getValue());
			double settingX, settingY, settingZ;
			
			settingX = Double.parseDouble(setVo.getValue1());
			settingY = Double.parseDouble(setVo.getValue2());
			settingZ = Double.parseDouble(setVo.getValue3());
			
			BpointContentVO contentVO = new BpointContentVO();
			contentVO.setBpoint_ltid(packet.getUnit_cd());
			contentVO.setBpoint_base_event_time(Utils.getCurDateTimeForStrTODate());
			
			contentVO.setBpoint_base_latitude(packet.getLatitude());
			contentVO.setBpoint_base_longitude(packet.getLongitude());
			contentVO.setBpoint_base_battery(packet.getBattery());
			contentVO.setBpoint_base_signal(packet.getSignal());
			contentVO.setBpoint_base_x(packet.getAcc_x());
			contentVO.setBpoint_base_y(packet.getAcc_y());
			contentVO.setBpoint_base_z(packet.getAcc_z());
			contentVO.setBpoint_degrees(packet.getTemporature());
			contentVO.setBpoint_humidity((float)packet.getHumi());

			// State tag ??
			contentVO.setBpoint_base_statetag(1);
			contentVO.setBpoint_fail_clear(true);
			
			
			String title = "[LGU+] dev_eui[" + packet.getUnit_cd() + "]를 변경하였습니다.";
			String detail = "";
			eTYPE_EVENT_STATUS event_status = eTYPE_EVENT_STATUS.STATUS_MODIFY;
			
	logger.debug("base_time : " + vo.getBpoint_base_event_time() 
			+ ", recv : " + contentVO.getBpoint_base_x() +"," + contentVO.getBpoint_base_y() +"," + contentVO.getBpoint_base_z()
			+ ", orig : " + vo.getBpoint_x() +","+ vo.getBpoint_y() +","+ vo.getBpoint_z() 
			+ ", base : " + vo.getBpoint_base_x() +","+ vo.getBpoint_base_y() +","+ vo.getBpoint_base_z()
			+ ", sett : " + settingX + ","+ settingY + ","+ settingZ );
	
	System.out.println("[NB-IoT] " + "base_time : " + vo.getBpoint_base_event_time() 
					+ ", recv : " + contentVO.getBpoint_base_x() +"," + contentVO.getBpoint_base_y() +"," + contentVO.getBpoint_base_z()
					+ ", orig : " + vo.getBpoint_x() +","+ vo.getBpoint_y() +","+ vo.getBpoint_z() 
					+ ", base : " + vo.getBpoint_base_x() +","+ vo.getBpoint_base_y() +","+ vo.getBpoint_base_z()
					+ ", sett : " + settingX + ","+ settingY + ","+ settingZ);
	
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
						detail = "recv push[가속도 장애], dev_eui "+packet.getUnit_cd()+", " + detail;
						logger.info(detail);
						event_status = eTYPE_EVENT_STATUS.STATUS_FAIL;
						title = "recv push dev_eui["+packet.getUnit_cd()+"]가 장애가 발생하였습니다.";
						thingplugStatus = eTHINGPLUG_STATUS.SUBSCRIPTION_PUSH_FAIL;
					}
					
				}else{	
					if(vo.getBpoint_fail_status()==BPOINT_RESOURCE.STATUS_FAIL){
						vo.setBpoint_fail_status(BPOINT_RESOURCE.STATUS_NORMAL);
						detail = "recv push[가속도 장애 해제], dev_eui "+packet.getUnit_cd()+", " + detail;
						logger.info(detail);
						event_status = eTYPE_EVENT_STATUS.STATUS_FAILBACK;
						title = "recv push dev_eui["+packet.getUnit_cd()+"]가 장애가 해지되었습니다.";
						thingplugStatus = eTHINGPLUG_STATUS.SUBSCRIPTION_PUSH_FAILBACK;
					}
				}
			
				if(detail.length()==0) detail = "recv push , dev_eui:" + packet.getUnit_cd() + ", 가속도 x,y,z" 
						+ contentVO.getBpoint_base_x()+","+contentVO.getBpoint_base_y() +"," + contentVO.getBpoint_base_z();
	
				detail += "[수신 data:" + packet.getPacket() + "]";
	logger.debug("detail msg : " + detail);		
	
				vo.setBpoint_x(contentVO.getBpoint_base_x());
				vo.setBpoint_y(contentVO.getBpoint_base_y());
				vo.setBpoint_z(contentVO.getBpoint_base_z());
				vo.setBpoint_degrees(contentVO.getBpoint_degrees());
				vo.setBpoint_humidity(contentVO.getBpoint_humidity());
				vo.setBpoint_event_time(contentVO.getBpoint_base_event_time());
				vo.setBpoint_signal(contentVO.getBpoint_base_signal());
				vo.setBpoint_battery(contentVO.getBpoint_base_battery());
	
	
				if(UDPService.getInstance().getAppMapper().updateBpointFailStatus(vo)== 0){
					logger.warn("recv push , dev_eui:" + packet.getUnit_cd() + "에 대한 정보를 저장하지 못하였습니다.");
					return;
				}
			}else{
				contentVO.setBpoint_fail_clear(true);
				if(UDPService.getInstance().getAppMapper().setBpointBaseContent(contentVO)==0){
					logger.warn("recv push , dev_eui:" + packet.getUnit_cd() + "에 대한 정보를 저장하지 못하였습니다.");
					return;
				}
				detail = "recv push , dev_eui:" +  packet.getUnit_cd() + ", 가속도 x,y,z" 
				+ contentVO.getBpoint_base_x()+","+contentVO.getBpoint_base_y() +"," + contentVO.getBpoint_base_z();
			}
	
	
			String strTmp = MakeResponse.InsertEventAndHistory(UDPService.getInstance().getAppMapper(), eTYPE_EVENT.TYPE_EVENT_LTID,	
					packet.getUnit_cd(), event_status, title, detail, "system",
					eTHINGPLUG_STATUS.SUBSCRIPTION_PUSH, detail,
					contentVO.getBpoint_degrees(), contentVO.getBpoint_humidity(),
					contentVO.getBpoint_base_x(), contentVO.getBpoint_base_y(),
					contentVO.getBpoint_base_z(),
					contentVO.getBpoint_base_battery(), contentVO.getBpoint_base_signal(), 
					contentVO.getBpoint_base_statetag(),
					contentVO.getBpoint_base_event_time());
			if(strTmp!=null) logger.warn("InsertEventAndHistory fail : " + strTmp);
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("recv thingplug : " + e.toString());
		}		
	}
	
}
