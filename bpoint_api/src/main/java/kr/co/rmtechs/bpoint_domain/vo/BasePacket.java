package kr.co.rmtechs.bpoint_domain.vo;

public class BasePacket {
	public static final String RESPONSE_OK = "100";
	public static final String RESPONSE_RESEND = "200";
	public static final String RESPONSE_ERR_LENGTH = "201";
	public static final String RESPONSE_ERR_PARSING = "202";
	public static final String RESPONSE_NOT_EXISTS_UNIT = "404";
	
	private String log_cd;
	private String model_cd;
	private String comp_cd;
	private String unit_cd;
	private String site_cd;
	private String group_cd;
	
	private int packet_index;
	private double temporature;
	private double humi;
	private double noise;
	private int light;
	private double acc_x;
	private double acc_y;
	private double acc_z;
	private int tilt;
	private double pressure;
	private int dust;
	private int magnatic_1;
	private int magnatic_2;
	private int magnatic_3;
	private int magnatic_4;
	private int magnatic_5;
	private int proximity;
	private double latitude;
	private double longitude;
	private int beep;
	private int switch_1;
	private int gyroscope;
	private int battery;
	private int signal;
	private String timeStamp;
	private String packet;
	private String recv_dt;
	
	private String responseCode;
	
	public String getLog_cd() {
		return log_cd;
	}
	public void setLog_cd(String log_cd) {
		this.log_cd = log_cd;
	}
	public String getModel_cd() {
		return model_cd;
	}
	public void setModel_cd(String model_cd) {
		this.model_cd = model_cd;
	}
	public int getPacket_index() {
		return packet_index;
	}
	public void setPacket_index(int packet_index) {
		this.packet_index = packet_index;
	}
	public String getUnit_cd() {
		return unit_cd;
	}
	public void setUnit_cd(String unit_cd) {
		this.unit_cd = unit_cd;
	}
	public String getSite_cd() {
		return site_cd;
	}
	public void setSite_cd(String site_cd) {
		this.site_cd = site_cd;
	}
	public String getGroup_cd() {
		return group_cd;
	}
	public void setGroup_cd(String group_cd) {
		this.group_cd = group_cd;
	}
	public String getComp_cd() {
		return comp_cd;
	}
	public void setComp_cd(String comp_cd) {
		this.comp_cd = comp_cd;
	}
	public double getTemporature() {
		return temporature;
	}
	public void setTemporature(double temporature) {
		this.temporature = temporature;
	}
	public double getHumi() {
		return humi;
	}
	public void setHumi(double humi) {
		this.humi = humi;
	}
	public double getNoise() {
		return noise;
	}
	public void setNoise(double noise) {
		this.noise = noise;
	}
	public int getLight() {
		return light;
	}
	public void setLight(int light) {
		this.light = light;
	}
	public double getAcc_x() {
		return acc_x;
	}
	public void setAcc_x(double acc_x) {
		this.acc_x = acc_x;
	}
	public double getAcc_y() {
		return acc_y;
	}
	public void setAcc_y(double acc_y) {
		this.acc_y = acc_y;
	}
	public double getAcc_z() {
		return acc_z;
	}
	public void setAcc_z(double acc_z) {
		this.acc_z = acc_z;
	}
	public int getTilt() {
		return tilt;
	}
	public void setTilt(int tilt) {
		this.tilt = tilt;
	}
	public double getPressure() {
		return pressure;
	}
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	public int getDust() {
		return dust;
	}
	public void setDust(int dust) {
		this.dust = dust;
	}
	public int getMagnatic_1() {
		return magnatic_1;
	}
	public void setMagnatic_1(int magnatic_1) {
		this.magnatic_1 = magnatic_1;
	}
	public int getMagnatic_2() {
		return magnatic_2;
	}
	public void setMagnatic_2(int magnatic_2) {
		this.magnatic_2 = magnatic_2;
	}
	public int getMagnatic_3() {
		return magnatic_3;
	}
	public void setMagnatic_3(int magnatic_3) {
		this.magnatic_3 = magnatic_3;
	}
	public int getMagnatic_4() {
		return magnatic_4;
	}
	public void setMagnatic_4(int magnatic_4) {
		this.magnatic_4 = magnatic_4;
	}
	public int getMagnatic_5() {
		return magnatic_5;
	}
	public void setMagnatic_5(int magnatic_5) {
		this.magnatic_5 = magnatic_5;
	}
	public int getProximity() {
		return proximity;
	}
	public void setProximity(int proximity) {
		this.proximity = proximity;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getBeep() {
		return beep;
	}
	public void setBeep(int beep) {
		this.beep = beep;
	}
	public int getSwitch_1() {
		return switch_1;
	}
	public void setSwitch_1(int switch_1) {
		this.switch_1 = switch_1;
	}
	public int getGyroscope() {
		return gyroscope;
	}
	public void setGyroscope(int gyroscope) {
		this.gyroscope = gyroscope;
	}
	public int getBattery() {
		return battery;
	}
	public void setBattery(int battery) {
		this.battery = battery;
	}
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getPacket() {
		return packet;
	}
	public void setPacket(String packet) {
		this.packet = packet;
	}
	public String getRecv_dt() {
		return recv_dt;
	}
	public void setRecv_dt(String recv_dt) {
		this.recv_dt = recv_dt;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
}
