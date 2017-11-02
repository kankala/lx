package kr.co.rmtechs.bpoint_domain.vo;

import javax.annotation.Generated;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Repository
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class BpointGroupMapVO {
	@JsonIgnore
	private String bpoint_ltid;
	@JsonProperty("group_no")
	private int group_no;
	@JsonIgnore
	private String id_insert;
	@JsonIgnore
	private String dt_insert;
	@JsonIgnore
	private String id_update;
	@JsonIgnore
	private String dt_update;
	
	public String getBpoint_ltid() {
		return bpoint_ltid;
	}
	public void setBpoint_ltid(String bpoint_ltid) {
		this.bpoint_ltid = bpoint_ltid;
	}
	public int getGroup_no() {
		return group_no;
	}
	public void setGroup_no(int group_no) {
		this.group_no = group_no;
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
		return "BpointGroupMapVO [bpoint_ltid=" + bpoint_ltid + ", group_no=" + group_no + ", id_insert=" + id_insert
				+ ", dt_insert=" + dt_insert + ", id_update=" + id_update + ", dt_update=" + dt_update + "]";
	}
	
}
