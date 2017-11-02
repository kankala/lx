package kr.co.rmtechs.bpoint_domain.vo;

import javax.annotation.Generated;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import kr.co.rmtechs.bpoint_domain.BPOINT_RESOURCE;

@Repository
@JsonInclude(JsonInclude.Include.NON_NULL )
@Generated("org.jsonschema2pojo")
public class BpointVO extends BpointBaseVO{
	@JsonProperty("zipcode")
	private String bpoint_zipcode;
	@JsonProperty("addr_state")
	private String bpoint_addr_state;
	@JsonProperty("addr_city")
	private String bpoint_addr_city;
	@JsonProperty("addr_etc")
	private String bpoint_addr_etc;
	@JsonProperty("degrees")
	private double bpoint_degrees;
	@JsonProperty("humidity")
	private float  bpoint_humidity;
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
	@JsonProperty("event_time")
	private String bpoint_event_time;
	@JsonIgnore
	private String bpoint_base_event_time;
	@JsonIgnore
	private double bpoint_base_x;
	@JsonIgnore
	private double bpoint_base_y;
	@JsonIgnore
	private double bpoint_base_z;
	@JsonIgnore
	private double bpoint_base_latitude;
	@JsonIgnore
	private double bpoint_base_longitude;
	@JsonIgnore
	private long   bpoint_base_statetag;
	@JsonIgnore
	private String id_insert;
	@JsonProperty("reg_time")
	private String dt_insert;
	@JsonIgnore
	private String id_update;
	@JsonIgnore
	private String dt_update;
	public String getBpoint_zipcode() {
		return bpoint_zipcode;
	}
	public void setBpoint_zipcode(String bpoint_zipcode) {
		this.bpoint_zipcode = bpoint_zipcode;
	}
	public String getBpoint_addr_state() {
		return bpoint_addr_state;
	}
	public void setBpoint_addr_state(String bpoint_addr_state) {
		this.bpoint_addr_state = bpoint_addr_state;
	}
	public String getBpoint_addr_city() {
		return bpoint_addr_city;
	}
	public void setBpoint_addr_city(String bpoint_addr_city) {
		this.bpoint_addr_city = bpoint_addr_city;
	}
	public String getBpoint_addr_etc() {
		return bpoint_addr_etc;
	}
	public void setBpoint_addr_etc(String bpoint_addr_etc) {
		this.bpoint_addr_etc = bpoint_addr_etc;
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
	public String getBpoint_event_time() {
		return bpoint_event_time;
	}
	public void setBpoint_event_time(String bpoint_event_time) {
		this.bpoint_event_time = bpoint_event_time;
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
	public String getId_insert() {
		return id_insert;
	}
	public void setId_insert(String id_insert) {
		this.id_insert = id_insert;
	}
	public String getDt_insert() {
		return dt_insert;
	}
	public void setDt_insert(String dt_insert) {
		this.dt_insert = dt_insert;
	}
	public String getId_update() {
		return id_update;
	}
	public void setId_update(String id_update) {
		this.id_update = id_update;
	}
	public String getDt_update() {
		return dt_update;
	}
	public void setDt_update(String dt_update) {
		this.dt_update = dt_update;
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
		return "BpointVO [bpoint_zipcode=" + bpoint_zipcode + ", bpoint_addr_state=" + bpoint_addr_state
				+ ", bpoint_addr_city=" + bpoint_addr_city + ", bpoint_addr_etc=" + bpoint_addr_etc
				+ ", bpoint_degrees=" + bpoint_degrees + ", bpoint_humidity=" + bpoint_humidity + ", bpoint_x="
				+ bpoint_x + ", bpoint_y=" + bpoint_y + ", bpoint_z=" + bpoint_z + ", bpoint_event_time="
				+ bpoint_event_time + ", bpoint_base_event_time=" + bpoint_base_event_time + ", bpoint_base_x="
				+ bpoint_base_x + ", bpoint_base_y=" + bpoint_base_y + ", bpoint_base_z=" + bpoint_base_z
				+ ", bpoint_base_latitude=" + bpoint_base_latitude + ", bpoint_base_longitude=" + bpoint_base_longitude
				+ ", bpoint_base_statetag=" + bpoint_base_statetag + ", id_insert=" + id_insert + ", dt_insert="
				+ dt_insert + ", id_update=" + id_update + ", dt_update=" + dt_update + ", toString()="
				+ super.toString() + "]";
	}
}
