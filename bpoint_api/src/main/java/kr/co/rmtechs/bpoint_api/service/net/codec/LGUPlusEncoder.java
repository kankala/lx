package kr.co.rmtechs.bpoint_api.service.net.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import kr.co.rmtechs.bpoint_domain.vo.BasePacket;
import kr.co.rmtechs.commons.Utils;

public class LGUPlusEncoder extends ProtocolEncoderAdapter {

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		IoBuffer buf = null;
		
		if(message instanceof String) {
			buf = IoBuffer.allocate(((String)message).length());
		}else if(message instanceof byte[]) {
			buf = IoBuffer.allocate(((byte[])message).length);
		}else if(message instanceof BasePacket) {
			BasePacket _data = (BasePacket)message;
			buf = IoBuffer.allocate(31);
		}
		
		if(buf != null) {
			buf.setAutoExpand(true);
			
			if(message instanceof String)
				buf.put(((String)message).getBytes());
			else if(message instanceof byte[]) 
				buf.put((byte[])message);
			else if(message instanceof BasePacket) {
				BasePacket _data = (BasePacket)message;
				
				buf.put(_data.getSite_cd().getBytes());
//				buf.put(_data.getGroup_cd().getBytes());
				buf.put((byte)_data.getPacket_index());
				buf.put(_data.getComp_cd().getBytes());
				
				// Unit에 대해 16 byte 맞춤
				buf.put(Utils.getUnitCD(_data.getUnit_cd()));
				buf.put(_data.getResponseCode().getBytes());
				buf.put(Utils.getRTCToByte());
			}

			
			// System.out.println("========== SEND TO : " + session.getRemoteAddress().toString() + " :: " + new String((byte[])message));
			
			buf.flip();
			try {
				out.write(buf);
				out.flush();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
//			// 응답 전송후 Session을 종료처리한다.
//			Object obj = session.getAttribute("RESPONSE");
//			if(obj instanceof Boolean) {
//				if(((Boolean)obj)) {
//					session.closeNow();
//				}
//			}
		}else {
		}
	}
}
