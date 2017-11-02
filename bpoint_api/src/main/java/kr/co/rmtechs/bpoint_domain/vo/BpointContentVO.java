package kr.co.rmtechs.bpoint_domain.vo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.rmtechs.commons.Utils;

public class BpointContentVO {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String bpoint_ltid;
	private double bpoint_degrees=0.0;
	private float  bpoint_humidity=0.0f;
	private String bpoint_base_event_time;
	private double bpoint_base_x=0.0;
	private double bpoint_base_y=0.0;
	private double bpoint_base_z=0.0;
	private double bpoint_base_latitude;
	private double bpoint_base_longitude;
	private long   bpoint_base_statetag;
	private int bpoint_base_battery;
	private int bpoint_base_signal;
	private String contents;
	private boolean bpoint_fail_clear;
	
	public static final int MAX_CONTENT_SIZE = 38;
	
	
	public BpointContentVO() {
	}
	
	public BpointContentVO(String ltid, String contents, String lt, String property,
			String statetag, boolean fail_clear){
		try{
			this.bpoint_ltid = ltid;
			this.contents = contents.trim().toLowerCase();
			this.bpoint_fail_clear = fail_clear;

			// 마지막 변경 시간
			bpoint_base_event_time = lt;
			
			// gps정보
			if(property != null){
				String [] splite_ppt = property.split(",");
				
				if(splite_ppt.length >= 2){
					bpoint_base_latitude = Double.parseDouble(splite_ppt[0].toString().trim());
					bpoint_base_longitude = Double.parseDouble(splite_ppt[1].toString().trim());
				}
			}
			
			// statetag
			if(statetag != null){
				bpoint_base_statetag = Long.parseLong(statetag);
			}
			// 컨텐츠 값
			boolean positive = true;
			// 첫 2번재는 +, - 의 값
			if(this.contents.substring(0, 2).equals("00")==false) positive = false;
			Integer upper, lower, pos = 0;

			List<String> values = new ArrayList<String>();
			String str_temp;
			for(int i = 2 ; i < this.contents.length() ; i += 2) {
				str_temp = contents.substring(i, i +2);
				values.add(str_temp);
			}
			
			int[] b_protocol = new int[values.size()];
			int idx = 0;
			for(String str : values) {
				b_protocol[idx] = Integer.parseUnsignedInt(str, 16);
				idx++;
			}
			
			// Protocol 분석 처리
			// 1 : 온도 (2)
			bpoint_degrees = (double)(b_protocol[0] * 256 + b_protocol[1]) / 10.0;
			bpoint_degrees = bpoint_degrees * (positive ? 1 : -1);
			
			// 2 : 습도 (2)
			bpoint_humidity = (float)(b_protocol[2] * 256 + b_protocol[3]) / 10;
			
			byte[] acc = new byte[4];
			// 3 : 가속도 x (4)
			acc[0] = (byte)b_protocol[4];	acc[1] = (byte)b_protocol[5];	acc[2] = (byte)b_protocol[6];	acc[3] = (byte)b_protocol[7];
			bpoint_base_x = Utils.calcByteToDouble(acc, 7);
			
			// 4 : 가속도 y (4)
			acc[0] = (byte)b_protocol[8];	acc[1] = (byte)b_protocol[9];	acc[2] = (byte)b_protocol[10];	acc[3] = (byte)b_protocol[11];
			bpoint_base_y = Utils.calcByteToDouble(acc, 7);
			
			// 5 : 가속도 z (4)
			acc[0] = (byte)b_protocol[12];	acc[1] = (byte)b_protocol[13];	acc[2] = (byte)b_protocol[14];	acc[3] = (byte)b_protocol[15];
			bpoint_base_z = Utils.calcByteToDouble(acc, 7);
			
			if(b_protocol.length > 16) {
				// 6 : 베티러 (1)
				bpoint_base_battery = b_protocol[16];
				
				// 7 : Signal (1)
				bpoint_base_signal = b_protocol[17];
				if(bpoint_base_signal > 0)
					bpoint_base_signal = bpoint_base_signal * -1;
			}
			
//			for(int i=2; i<this.contents.length(); pos++){
//				
//				upper = Integer.parseUnsignedInt(this.contents.substring(i, i+2),16);
//				logger.debug("upper["+i+"]: " +this.contents.substring(i, i+2) + ","+upper);
//				i+=2;
//				lower = Integer.parseUnsignedInt(this.contents.substring(i, i+2),16);
//				logger.debug("lower["+i+"]: " +this.contents.substring(i, i+2) + ","+lower);
//				i+=2 ;
//				
//				if(pos == 0){
//					bpoint_degrees = (double)(upper*256 + lower)/10.0;
//					if(positive == false) bpoint_degrees = bpoint_degrees * -1;
//				}
//				else if(pos == 1)	bpoint_humidity = (float)(upper*256 + lower)/10;
//				else if(pos == 2)	bpoint_base_x = (double)((short)(upper*256 + lower))*3.91*0.001;
//				else if(pos == 3)	bpoint_base_y = (double)((short)(upper*256 + lower))*3.91*0.001;
//				else if(pos == 4)	bpoint_base_z = (double)((short)(upper*256 + lower))*3.91*0.001;
//				else {
//					bpoint_base_battery = upper;
//					bpoint_base_signal = lower;
//				}
//			}
//			if(bpoint_base_x!=0)	bpoint_base_x = (double)((long)(bpoint_base_x * 10000))/10000;
//			if(bpoint_base_y!=0)	bpoint_base_y = (double)((long)(bpoint_base_y * 10000))/10000;
//			if(bpoint_base_z!=0)	bpoint_base_z = (double)((long)(bpoint_base_z * 10000))/10000;
		
		}catch(Exception e){
			this.contents = null;
		}
	}

	public String getBpoint_ltid() {
		return bpoint_ltid;
	}

	public void setBpoint_ltid(String bpoint_ltid) {
		this.bpoint_ltid = bpoint_ltid;
	}

	public double getBpoint_degrees() {
		return bpoint_degrees;
	}

	public void setBpoint_degrees(double bpoint_degrees) {
		this.bpoint_degrees = bpoint_degrees;
	}

	public float getBpoint_humidity() {
		return bpoint_humidity;
	}

	public void setBpoint_humidity(float bpoint_humidity) {
		this.bpoint_humidity = bpoint_humidity;
	}

	public String getBpoint_base_event_time() {
		return bpoint_base_event_time;
	}

	public void setBpoint_base_event_time(String bpoint_base_event_time) {
		this.bpoint_base_event_time = bpoint_base_event_time;
	}

	public double getBpoint_base_x() {
		return bpoint_base_x;
	}

	public void setBpoint_base_x(double bpoint_base_x) {
		this.bpoint_base_x = bpoint_base_x;
	}

	public double getBpoint_base_y() {
		return bpoint_base_y;
	}

	public void setBpoint_base_y(double bpoint_base_y) {
		this.bpoint_base_y = bpoint_base_y;
	}

	public double getBpoint_base_z() {
		return bpoint_base_z;
	}

	public void setBpoint_base_z(double bpoint_base_z) {
		this.bpoint_base_z = bpoint_base_z;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	
	public double getBpoint_base_latitude() {
		return bpoint_base_latitude;
	}

	public void setBpoint_base_latitude(double bpoint_base_latitude) {
		this.bpoint_base_latitude = bpoint_base_latitude;
	}

	public double getBpoint_base_longitude() {
		return bpoint_base_longitude;
	}

	public void setBpoint_base_longitude(double bpoint_base_longitude) {
		this.bpoint_base_longitude = bpoint_base_longitude;
	}

	public long getBpoint_base_statetag() {
		return bpoint_base_statetag;
	}

	public void setBpoint_base_statetag(long bpoint_base_statetag) {
		this.bpoint_base_statetag = bpoint_base_statetag;
	}

	public boolean isBpoint_fail_clear() {
		return bpoint_fail_clear;
	}

	public void setBpoint_fail_clear(boolean bpoint_fail_clear) {
		this.bpoint_fail_clear = bpoint_fail_clear;
	}

	public int getBpoint_base_battery() {
		return bpoint_base_battery;
	}

	public void setBpoint_base_battery(int bpoint_base_battery) {
		this.bpoint_base_battery = bpoint_base_battery;
	}

	public int getBpoint_base_signal() {
		return bpoint_base_signal;
	}

	public void setBpoint_base_signal(int bpoint_base_signal) {
		this.bpoint_base_signal = bpoint_base_signal;
	}

	public static int getMaxContentSize() {
		return MAX_CONTENT_SIZE;
	}

	@Override
	public String toString() {
		return "BpointContentVO [bpoint_ltid=" + bpoint_ltid + ", bpoint_degrees=" + bpoint_degrees
				+ ", bpoint_humidity=" + bpoint_humidity + ", bpoint_base_event_time=" + bpoint_base_event_time
				+ ", bpoint_base_x=" + bpoint_base_x + ", bpoint_base_y=" + bpoint_base_y + ", bpoint_base_z="
				+ bpoint_base_z + ", bpoint_base_latitude=" + bpoint_base_latitude + ", bpoint_base_longitude="
				+ bpoint_base_longitude + ", bpoint_base_statetag=" + bpoint_base_statetag + ", contents=" + contents
				+ ", bpoint_fail_clear=" + bpoint_fail_clear + "]";
	}
}
