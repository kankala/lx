package kr.co.rmtechs.bpoint_domain.vo;

import javax.annotation.Generated;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import kr.co.rmtechs.bpoint_domain.BPOINT_RESOURCE;

@Repository
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class BpointHistVO {
	@JsonProperty("seq")
	private String bpoint_seq;
	@JsonProperty("dev_eui")
	private String bpoint_ltid;
	@JsonProperty("event_time")
	private String bpoint_event_time;
	@JsonProperty("status")
	private String bpoint_status;
	@JsonProperty("degrees")
	private double bpoint_degrees;
	@JsonProperty("humidity")
	private float bpoint_humidity;
	@JsonProperty("x")
	private double bpoint_x;
	@JsonProperty("y")
	private double bpoint_y;
	@JsonProperty("z")
	private double bpoint_z;
	@JsonProperty("commpart")
	private int commpart;
	@JsonProperty("model")
	private String model = BPOINT_RESOURCE.MODEL;
	@JsonProperty("signal")
	private int bpoint_signal;
	@JsonProperty("battery")
	private int bpoint_battery;
	@JsonProperty("msg")
	private String bpoint_msg;
	@JsonProperty("statetag")
	private long bpoint_statetag;
	@JsonProperty("last_modified_time")
	private String bpoint_last_modified_time;
	
	public String getBpoint_seq() {
		return bpoint_seq;
	}
	public void setBpoint_seq(String bpoint_seq) {
		this.bpoint_seq = bpoint_seq;
	}
	public String getBpoint_ltid() {
		return bpoint_ltid;
	}
	public void setBpoint_ltid(String bpoint_ltid) {
		this.bpoint_ltid = bpoint_ltid;
	}
	public String getBpoint_event_time() {
		return bpoint_event_time;
	}
	public void setBpoint_event_time(String bpoint_event_time) {
		this.bpoint_event_time = bpoint_event_time;
	}
	public String getBpoint_status() {
		return bpoint_status;
	}
	public void setBpoint_status(String bpoint_status) {
		this.bpoint_status = bpoint_status;
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
	public double getBpoint_x() {
		return bpoint_x;
	}
	public void setBpoint_x(double bpoint_x) {
		this.bpoint_x = bpoint_x;
	}
	public double getBpoint_y() {
		return bpoint_y;
	}
	public void setBpoint_y(double bpoint_y) {
		this.bpoint_y = bpoint_y;
	}
	public double getBpoint_z() {
		return bpoint_z;
	}
	public void setBpoint_z(double bpoint_z) {
		this.bpoint_z = bpoint_z;
	}
	public String getBpoint_msg() {
		return bpoint_msg;
	}
	public void setBpoint_msg(String bpoint_msg) {
		this.bpoint_msg = bpoint_msg;
	}
	public long getBpoint_statetag() {
		return bpoint_statetag;
	}
	public void setBpoint_statetag(long bpoint_statetag) {
		this.bpoint_statetag = bpoint_statetag;
	}
	public String getBpoint_last_modified_time() {
		return bpoint_last_modified_time;
	}
	public void setBpoint_last_modified_time(String bpoint_last_modified_time) {
		this.bpoint_last_modified_time = bpoint_last_modified_time;
	}
	public int getCommpart() {
		return commpart;
	}
	public void setCommpart(int commpart) {
		this.commpart = commpart;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getBpoint_signal() {
		return bpoint_signal;
	}
	public void setBpoint_signal(int bpoint_signal) {
		this.bpoint_signal = bpoint_signal;
	}
	public int getBpoint_battery() {
		return bpoint_battery;
	}
	public void setBpoint_battery(int bpoint_battery) {
		this.bpoint_battery = bpoint_battery;
	}
	@Override
	public String toString() {
		return "BpointHistVO [bpoint_seq=" + bpoint_seq + ", bpoint_ltid=" + bpoint_ltid + ", bpoint_event_time="
				+ bpoint_event_time + ", bpoint_status=" + bpoint_status + ", bpoint_degrees=" + bpoint_degrees
				+ ", bpoint_humidity=" + bpoint_humidity + ", bpoint_x=" + bpoint_x + ", bpoint_y=" + bpoint_y
				+ ", bpoint_z=" + bpoint_z + ", bpoint_msg=" + bpoint_msg + ", bpoint_statetag=" + bpoint_statetag
				+ ", bpoint_last_modified_time=" + bpoint_last_modified_time + "]";
	}

}
