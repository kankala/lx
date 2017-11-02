package kr.co.rmtechs.bpoint_api.service.net.codec;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import kr.co.rmtechs.bpoint_domain.vo.BasePacket;
import kr.co.rmtechs.commons.Utils;

public class LGUPlusDecoder extends ProtocolDecoderAdapter {
//	private ScannerProtocol data = new ScannerProtocol();
	// IP, ScannerProtocol
//	private Map<IoSession, ScannerProtocol> dMap = new HashMap<IoSession, ScannerProtocol>();
	private byte[] buf = null;
	
	// todo : session별로 나머지 데이터를 처리하도록 재 설정
	private static final int STOP_SYNC		= 0;
	private static final int STOP_LENGTH	= 1;
	private static final int STOP_CMD		= 2;
	private static final int STOP_DATA		= 3;
	private static final int STOP_ETX		= 4;
	
	private byte[] site_code = new byte[6];
	private byte packet_idx = 0x00;
	private byte[] group_code = new byte[4];
	private byte[] comm_comp = new byte[2];
	private byte unit_cd_dummy = 0x00;
	private byte[] unit_cd = new byte[15];
	private byte[] sn_temp = new byte[2];
	private byte sn_humi = 0x00;
	private byte[] sn_noise = new byte[2];
	private byte[] sn_light = new byte[2];
	private byte[] sn_acc_x = new byte[4];
	private byte[] sn_acc_y = new byte[4];
	private byte[] sn_acc_z = new byte[4];
	private byte sn_tilt = 0x00;
	private byte[] sn_dust = new byte[2];
	private byte[] sn_pressure = new byte[2];
	private byte sn_magnatic = 0x00;
	private byte[] sn_proximity = new byte[2];
	private byte[] sn_gps_lati = new byte[4];
	private byte[] sn_gps_long = new byte[4];
	private byte sn_beep = 0x00;
	private byte sn_switch = 0x00;
	private byte sn_led = 0x00;
	private byte[] timestamp = new byte[4];
	private byte sn_battery = 0x00;
	private byte sn_signal = 0x00;
	private byte[] extra = new byte[10];
	
	
//	private int cur_stop = STOP_SYNC;
//	private int cur_idx = 0;
	
	public LGUPlusDecoder() {
		super();
	}
	
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
//		data.setBuffer(in.array());
//		System.out.println("IN LENGTH : " + in.position() + " , " + in.limit() + " , " + in.capacity());
//		System.out.println("DECODE ENTER (" + session.getRemoteAddress().toString() + ")- " + in.position() + " :: " + Utils.byteArrayToHex(in.array(), in.limit()));
//		buf = in.array();
		
		InetSocketAddress addr = (InetSocketAddress)session.getRemoteAddress();
		
		byte[] b = new byte[in.limit()];
		System.arraycopy(in.array(), 0, b, 0, in.limit());
		
		try {
			decodeProtocol(session, in, out); 
			
//			ScannerProtocol data;
//			decodeProtocol(session, in, out);
//			System.out.println("RECV DATA - " + Utils.getBytesToString(data.getData()));
			
			in.clear();
			in.position(0);
			in.limit(0);
			in.flip();
			
			// TODO DELETE Temporary ECHO
			//      Change Response OK
//			session.setAttribute("RESPONSE", Boolean.TRUE);
//			session.write(b);
//			System.out.println("RECEIVED AND ECHO TO : " + new String(b));
			
			// 응답처리후 종료
//			session.closeNow();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Recv Byte의 남은 부분이 있는 경우 
	 * buf에 해당 데이터를 설정한다.
	 * @param buf
	 * @param out
	 * @return
	 * @throws Exception
	 */
	private void decodeProtocol(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		BasePacket packet = new BasePacket();

		System.out.println("PACKET RECVED : " + in.limit());
		
		
		if(in.limit() < 79) {
			System.out.println("PACKET LENGTH ERROR");
			
			packet.setSite_cd("000000");
			packet.setGroup_cd("0000");
			packet.setComp_cd("LG");
			packet.setUnit_cd("0000000000000000");
			packet.setResponseCode(BasePacket.RESPONSE_ERR_LENGTH);
			out.write(packet);
			return;
		}
		
		packet.setPacket(Utils.byteArrayToHex(in.array(), in.limit()));
		
		in.get(site_code, 0, site_code.length);
//		in.get(group_code, 0, group_code.length);		// v0.2c 삭제
		packet_idx = in.get();							// v0.30 추가
		in.get(comm_comp, 0, comm_comp.length);
		unit_cd_dummy = in.get();
		in.get(unit_cd, 0, unit_cd.length);
		in.get(sn_temp, 0, sn_temp.length);
		sn_humi = in.get();
		in.get(sn_noise, 0, sn_noise.length);
		in.get(sn_light, 0, sn_light.length);
		in.get(sn_acc_x, 0, sn_acc_x.length);
		in.get(sn_acc_y, 0, sn_acc_y.length);
		in.get(sn_acc_z, 0, sn_acc_z.length);
		sn_tilt = in.get();
		in.get(sn_dust, 0, sn_dust.length);
		in.get(sn_pressure, 0, sn_pressure.length);
		sn_magnatic = in.get();
		in.get(sn_proximity, 0, sn_proximity.length);
		in.get(sn_gps_lati, 0, sn_gps_lati.length);
		in.get(sn_gps_long, 0, sn_gps_long.length);
		sn_beep = in.get();
		sn_switch = in.get();
		sn_led = in.get();
		in.get(timestamp, 0, timestamp.length);
		sn_battery = in.get();
		sn_signal = in.get();
		in.get(extra, 0, extra.length);
		
		packet.setSite_cd(new String(site_code));
//		packet.setGroup_cd(new String(group_code));
		packet.setPacket_index(packet_idx);
		packet.setComp_cd(new String(comm_comp));
		packet.setUnit_cd(new String(unit_cd));
		packet.setTemporature(Utils.calcByteToDouble(sn_temp, 1));
		packet.setHumi(sn_humi);
		packet.setNoise(Utils.calcByteToDouble(sn_noise, 0));
		packet.setLight((int)Utils.calcByteToDouble(sn_dust, 0));
		packet.setAcc_x(Utils.calcByteToDouble(sn_acc_x, 7));
		packet.setAcc_y(Utils.calcByteToDouble(sn_acc_y, 7));
		packet.setAcc_z(Utils.calcByteToDouble(sn_acc_z, 7));
		packet.setTilt(sn_tilt);
		packet.setDust((int)Utils.calcByteToDouble(sn_dust, 0));
		packet.setPressure(Utils.calcByteToDouble(sn_dust, 1));
		packet.setMagnatic_1(sn_magnatic & 0x01);
		packet.setMagnatic_2(sn_magnatic >> 1 & 0x01);
		packet.setMagnatic_3(sn_magnatic >> 2 & 0x01);
		packet.setMagnatic_4(sn_magnatic >> 3 & 0x01);
		packet.setMagnatic_5(sn_magnatic >> 4 & 0x01);
		packet.setProximity((int)Utils.calcByteToDouble(sn_proximity, 0));
		packet.setLatitude(Utils.calcByteToDouble(sn_gps_lati, 7));
		packet.setLongitude(Utils.calcByteToDouble(sn_gps_long, 7));
		packet.setBeep(sn_beep);
		packet.setBeep(sn_switch);
		packet.setBeep(sn_led);
		packet.setTimeStamp(new String(timestamp));
		packet.setBattery(sn_battery);
		packet.setSignal(sn_signal);
		// in.get(extra, 0, extra.length);		
		
		packet.setResponseCode(BasePacket.RESPONSE_OK);
		
		out.write(packet);
		
//		byte sync = 0x00;
//		int _re = 0;
//		
//		ScannerProtocol data = null;
//		if(dMap.containsKey(session)) {
//			data = dMap.get(session);
//		}else {
//			data = new ScannerProtocol();
//			data.setBuffer(in.array());
//		}
//		
//		// 이전에 남은 부분에 대한 처리
//		if(data.getCur_stop() > STOP_SYNC && dMap.containsKey(session)) {
////			System.out.println("PREV : " + Utils.getBytesToString(data.getBuffer()));
////			System.out.println("CHECK... REMIND SYNC (" + session.getRemoteAddress() + ") -- " + Utils.getBytesToString(in.array()));
//			switch(data.getCur_stop()) {
//			case STOP_LENGTH :
//				byte[] b_length = data.getLength();
//				for(int i = 0 ; i < 4 - data.getCur_idx() ; i++) {
//					b_length[data.getCur_idx() + i] = in.get();
//				}
//				data.setLength(b_length);
//				data.calcurateLength();
//			case STOP_CMD :
//				data.setCmd(in.get());
//			case STOP_DATA :
//				byte[] b_data = data.getData();
//				for(int i = 0 ; i < data.getData().length - data.getCur_idx() ; i++) {
//					b_data[data.getCur_idx() + i] = in.get();
//				}
//				data.setData(b_data);
//			case STOP_ETX :
//				data.setEtx(in.get());
//				break;
//			}
//			
//			out.write(data);
//
//			removeRemindData(session, data);
//
////			System.out.println("REMIND DATA (" + session.getRemoteAddress() + ") , " + data.getCur_stop() + ") : " + data);
//		}
//		
//		int position = 0;
//		while(in.remaining() > 0) {
//			data = new ScannerProtocol();
//			data.setBuffer(in.array());
//			
//			// TODO 이전데이터가 있는가에 대한 조사
//			sync = in.get();
//			if(sync == ProtocolInstance.SYNC) {
//				position = in.position() -1;
//				data.setSync(sync);
//				
//				data.setCur_stop(STOP_SYNC);
//				
//				// length
//				_re = in.remaining();
//				if(_re < 4) {
//					// stop
//					byte[] buf = new byte[4];
//					in.get(buf, 0, _re);
//					
//					data.setLength(buf);
//					
//					setRemindData(session, data, STOP_LENGTH, _re);
//					break;
//				}else {
//					in.get(data.getLength(), 0, data.getLength().length);
//					int len = data.calcurateLength();
//				}
//				
//				// cmd
//				_re = in.remaining();
//				if(_re <= 0) {
//					// stop
//					setRemindData(session, data, STOP_CMD, 0);
//					break;
//				}else {
//					data.setCmd(in.get());
//				}
//				
//				// data
//				_re = in.remaining();
//				if(_re < data.getData().length) {
//					// stop
//					byte[] buf = new byte[data.getData().length];
//					in.get(buf, 0, _re);
//					
//					data.setData(buf);
//					
//					setRemindData(session, data, STOP_DATA, _re);
//					break;
//				}else {
//					in.get(data.getData(), 0, data.getData().length);
//				}
//				
//				// ext
//				_re = in.remaining();
//				if(_re <= 0) {
//					// stop
//					setRemindData(session, data, STOP_ETX, 0);
//					break;
//				}else {
//					data.setEtx(in.get());
//				}
//				
//				out.write(data);
//				removeRemindData(session, data);
//			}else if(sync == 0x00) {		// NULL Data
//				// Clear All
//				removeRemindData(session, data);
//				
//				break;
//			}else {		// 데이터 없음
//				removeRemindData(session, data);
//				break;
//			}
//		}
	}
	
	
//	private void setRemindData(IoSession session, ScannerProtocol data, int stop, int idx) {
//		data.setCur_stop(stop);
//		data.setCur_idx(idx);
//		
//		dMap.put(session, data);		
//	}
//	
//	private void removeRemindData(IoSession session, ScannerProtocol data) {
//		dMap.remove(session);
//		
//		data.setCur_stop(STOP_SYNC);
//		data.setCur_idx(0);		
//	}
	
	
	@Override
	public void dispose(IoSession session) throws Exception {
		super.dispose(session);
	}
	
	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
		super.finishDecode(session, out);
	}
}
