package kr.co.rmtechs.bpoint_domain.vo;

import javax.annotation.Generated;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Repository
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class GroupVO {
	@JsonProperty("no")
	private int group_no;
	@JsonProperty("name")
	private String group_name;
	@JsonProperty("role")
	private char group_role;
	@JsonIgnore
	private String group_detail;
	@JsonIgnore
	private String id_insert;
	@JsonIgnore
	private String dt_insert;
	@JsonIgnore
	private String id_update;
	@JsonIgnore
	private String dt_update;
	public int getGroup_no() {
		return group_no;
	}
	public void setGroup_no(int group_no) {
		this.group_no = group_no;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public char getGroup_role() {
		return group_role;
	}
	public void setGroup_role(char group_role) {
		this.group_role = group_role;
	}
	public String getGroup_detail() {
		return group_detail;
	}
	public void setGroup_detail(String group_detail) {
		this.group_detail = group_detail;
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
		return "GroupVO [group_no=" + group_no + ", group_name=" + group_name + ", group_role=" + group_role
				+ ", group_detail=" + group_detail + ", id_insert=" + id_insert + ", dt_insert=" + dt_insert
				+ ", id_update=" + id_update + ", dt_update=" + dt_update + "]";
	}
		
}
