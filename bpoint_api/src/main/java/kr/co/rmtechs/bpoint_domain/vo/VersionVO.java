package kr.co.rmtechs.bpoint_domain.vo;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Repository
public class VersionVO {
	@JsonProperty("ver_major")
	private int app_ver_major;
	@JsonProperty("ver_minor")
	private int app_ver_minor;
	@JsonIgnore
	private char app_os;
	@JsonIgnore
	private String app_title;
	@JsonIgnore
	private String app_detail;
	@JsonProperty("url")
	private String app_url;
	@JsonProperty("type")
	private char app_type;
	@JsonIgnore
	private String id_insert;
	@JsonIgnore
	private String dt_insert;
	@JsonIgnore
	private String id_update;
	@JsonIgnore
	private String dt_update;
	
	
	public int getApp_ver_major() {
		return app_ver_major;
	}
	public void setApp_ver_major(int app_ver_major) {
		this.app_ver_major = app_ver_major;
	}
	public int getApp_ver_minor() {
		return app_ver_minor;
	}
	public void setApp_ver_minor(int app_ver_minor) {
		this.app_ver_minor = app_ver_minor;
	}
	public char getApp_os() {
		return app_os;
	}
	public void setApp_os(char app_os) {
		this.app_os = app_os;
	}
	public String getApp_title() {
		return app_title;
	}
	public void setApp_title(String app_title) {
		this.app_title = app_title;
	}
	public String getApp_detail() {
		return app_detail;
	}
	public void setApp_detail(String app_detail) {
		this.app_detail = app_detail;
	}
	public String getApp_url() {
		return app_url;
	}
	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}
	public char getApp_type() {
		return app_type;
	}
	public void setApp_type(char app_type) {
		this.app_type = app_type;
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
