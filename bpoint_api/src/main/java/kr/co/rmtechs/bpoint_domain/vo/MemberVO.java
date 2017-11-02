package kr.co.rmtechs.bpoint_domain.vo;

import javax.annotation.Generated;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Repository
@JsonInclude(JsonInclude.Include.NON_NULL )
@Generated("org.jsonschema2pojo")
public class MemberVO {
	@JsonProperty("id")		
	private String member_id;
	@JsonIgnore	
	private String member_pwd;
	@JsonIgnore
	private String member_name;
	@JsonProperty("role")
	private char member_rank;
	@JsonProperty("group_no")
	private int member_group;
	@JsonIgnore
	private String member_phone;
	@JsonIgnore
	private String member_uuid;
	@JsonIgnore
	private String member_login_time;
	@JsonIgnore
	private String id_insert;
	@JsonIgnore
	private String dt_insert;
	@JsonIgnore
	private String id_update;
	@JsonIgnore
	private String dt_update;
	
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_pwd() {
		return member_pwd;
	}
	public void setMember_pwd(String member_pwd) {
		this.member_pwd = member_pwd;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public char getMember_rank() {
		return member_rank;
	}
	public void setMember_rank(char member_rank) {
		this.member_rank = member_rank;
	}
	public int getMember_group() {
		return member_group;
	}
	public void setMember_group(int member_group) {
		this.member_group = member_group;
	}
	public String getMember_phone() {
		return member_phone;
	}
	public void setMember_phone(String member_phone) {
		this.member_phone = member_phone;
	}
	public String getMember_uuid() {
		return member_uuid;
	}
	public void setMember_uuid(String member_uuid) {
		this.member_uuid = member_uuid;
	}
	public String getMember_login_time() {
		return member_login_time;
	}
	public void setMember_login_time(String member_login_time) {
		this.member_login_time = member_login_time;
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
	
	
}
