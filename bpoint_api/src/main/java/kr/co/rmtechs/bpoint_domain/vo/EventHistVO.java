package kr.co.rmtechs.bpoint_domain.vo;

import javax.annotation.Generated;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Repository
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class EventHistVO {
	@JsonProperty("no")
	private int event_seq;
	@JsonProperty("event_time")
	private String event_time;
	@JsonProperty("type")
	private char event_type;
	@JsonProperty("dev_eui")
	private String event_ltid;
	@JsonProperty("status")
	private int event_status;
	@JsonProperty("title")
	private String event_title;
	@JsonProperty("detail")
	private String event_detail;
	@JsonIgnore
	private String id_insert;
	@JsonIgnore
	private char event_read_status;
	@JsonIgnore
	private String event_read_time;
	@JsonIgnore
	private String event_write_time;
	

	public int getEvent_seq() {
		return event_seq;
	}
	public void setEvent_seq(int event_seq) {
		this.event_seq = event_seq;
	}
	public String getEvent_time() {
		return event_time;
	}
	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}
	public char getEvent_type() {
		return event_type;
	}
	public void setEvent_type(char event_type) {
		this.event_type = event_type;
	}
	public String getEvent_ltid() {
		return event_ltid;
	}
	public void setEvent_ltid(String event_ltid) {
		this.event_ltid = event_ltid;
	}
	public int getEvent_status() {
		return event_status;
	}
	public void setEvent_status(int event_status) {
		this.event_status = event_status;
	}
	public String getEvent_title() {
		return event_title;
	}
	public void setEvent_title(String event_title) {
		this.event_title = event_title;
	}
	public String getEvent_detail() {
		return event_detail;
	}
	public void setEvent_detail(String event_detail) {
		this.event_detail = event_detail;
	}
	public String getId_insert() {
		return id_insert;
	}
	public void setId_insert(String id_insert) {
		this.id_insert = id_insert;
	}
	public char getEvent_read_status() {
		return event_read_status;
	}
	public void setEvent_read_status(char event_read_status) {
		this.event_read_status = event_read_status;
	}
	public String getEvent_read_time() {
		return event_read_time;
	}
	public void setEvent_read_time(String event_read_time) {
		this.event_read_time = event_read_time;
	}
	public String getEvent_write_time() {
		return event_write_time;
	}
	public void setEvent_write_time(String event_write_time) {
		this.event_write_time = event_write_time;
	}
	
	@Override
	public String toString() {
		return "EventHistVO [event_seq=" + event_seq + ", event_time=" + event_time + ", event_type=" + event_type
				+ ", event_ltid=" + event_ltid + ", event_status=" + event_status + ", event_title=" + event_title
				+ ", event_detail=" + event_detail + ", id_insert=" + id_insert + ", event_read_status="
				+ event_read_status + ", event_read_time=" + event_read_time + ", event_write_time=" + event_write_time
				+ "]";
	}
}
