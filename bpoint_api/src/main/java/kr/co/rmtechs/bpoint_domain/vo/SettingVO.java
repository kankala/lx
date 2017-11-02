package kr.co.rmtechs.bpoint_domain.vo;

import javax.annotation.Generated;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Repository
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class SettingVO {
	@JsonProperty("no")
	private int no;
	@JsonIgnore
	private String title;
	@JsonIgnore
	private String detail;
	@JsonProperty("x")
	private String value1;
	@JsonProperty("y")
	private String value2;
	@JsonProperty("z")
	private String value3;
	@JsonIgnore
	private String id_insert;
	@JsonIgnore
	private String dt_insert;
	@JsonIgnore
	private String id_update;
	@JsonIgnore
	private String dt_update;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	public String getValue3() {
		return value3;
	}
	public void setValue3(String value3) {
		this.value3 = value3;
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
	
	@Override
	public String toString() {
		return "SettingVO [no=" + no + ", title=" + title + ", detail=" + detail + ", value1=" + value1 + ", value2="
				+ value2 + ", value3=" + value3 + ", id_insert=" + id_insert + ", dt_insert=" + dt_insert
				+ ", id_update=" + id_update + ", dt_update=" + dt_update + "]";
	}
	
	
}


