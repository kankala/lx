package kr.co.rmtechs.bpoint_domain.vo;

import javax.annotation.Generated;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Repository
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class ThingPlugVO{
	@JsonProperty("bpoint_ltid")
	private String 	bpoint_ltid;
	@JsonProperty("status")
	private int 	thingplug_status;
	@JsonProperty("read_time")
	private String	thingplug_read_time;
	@JsonProperty("node_status")
	private int 	thingplug_node_status;
	@JsonProperty("node_recv_time")
	private String	thingplug_node_recv_time;
	@JsonProperty("node_response")
	private String	thingplug_node_response;
	@JsonProperty("remoteCES_status")
	private int		thingplug_remoteCES_status;
	@JsonProperty("remoteCES_recv_time")
	private	String	thingplug_remoteCES_recv_time;
	@JsonProperty("remoteCES_response")
	private	String	thingplug_remoteCSE_response;
	@JsonProperty("latest_status")
	private int		thingplug_container_latest_status;
	@JsonProperty("latest_recv_time")
	private	String	thingplug_container_latest_recv_time;
	@JsonProperty("latest_response")
	private String	thingplug_container_latest_response;
	@JsonProperty("subscription_status")
	private int		thingplug_subscription_status;
	@JsonProperty("subscription_recv_time")
	private String	thingplug_subscription_recv_time;
	@JsonProperty("subscription_response")
	private String	thingplug_subscription_response;
	@JsonProperty("retrieve_status")
	private int		thingplug_subscription_retrieve_status;
	@JsonProperty("retrieve_recv_time")
	private String	thingplug_subscription_retrieve_recv_time;
	@JsonProperty("retrieve_response")
	private String	thingplug_subscription_retrieve_response;
	@JsonIgnore
	private int		thingplug_delete_status;
	@JsonIgnore
	private String	thingplug_delete_recv_time;
	@JsonIgnore
	private String	thingplug_delete_response;
	
	public String getBpoint_ltid() {
		return bpoint_ltid;
	}
	public void setBpoint_ltid(String bpoint_ltid) {
		this.bpoint_ltid = bpoint_ltid;
	}
	public int getThingplug_status() {
		return thingplug_status;
	}
	public void setThingplug_status(int thingplug_status) {
		this.thingplug_status = thingplug_status;
	}
	public String getThingplug_read_time() {
		return thingplug_read_time;
	}
	public void setThingplug_read_time(String thingplug_read_time) {
		this.thingplug_read_time = thingplug_read_time;
	}
	public int getThingplug_node_status() {
		return thingplug_node_status;
	}
	public void setThingplug_node_status(int thingplug_node_status) {
		this.thingplug_node_status = thingplug_node_status;
	}
	public String getThingplug_node_recv_time() {
		return thingplug_node_recv_time;
	}
	public void setThingplug_node_recv_time(String thingplug_node_recv_time) {
		this.thingplug_node_recv_time = thingplug_node_recv_time;
	}
	public String getThingplug_node_response() {
		return thingplug_node_response;
	}
	public void setThingplug_node_response(String thingplug_node_response) {
		this.thingplug_node_response = thingplug_node_response;
	}
	public int getThingplug_remoteCES_status() {
		return thingplug_remoteCES_status;
	}
	public void setThingplug_remoteCES_status(int thingplug_remoteCSE_status) {
		this.thingplug_remoteCES_status = thingplug_remoteCSE_status;
	}
	public String getThingplug_remoteCES_recv_time() {
		return thingplug_remoteCES_recv_time;
	}
	public void setThingplug_remoteCES_recv_time(String thingplug_remoteCSE_recv_time) {
		this.thingplug_remoteCES_recv_time = thingplug_remoteCSE_recv_time;
	}
	public String getThingplug_remoteCSE_response() {
		return thingplug_remoteCSE_response;
	}
	public void setThingplug_remoteCSE_response(String thingplug_remoteCSE_response) {
		this.thingplug_remoteCSE_response = thingplug_remoteCSE_response;
	}
	public int getThingplug_container_latest_status() {
		return thingplug_container_latest_status;
	}
	public void setThingplug_container_latest_status(int thingplug_container_latest_status) {
		this.thingplug_container_latest_status = thingplug_container_latest_status;
	}
	public String getThingplug_container_latest_recv_time() {
		return thingplug_container_latest_recv_time;
	}
	public void setThingplug_container_latest_recv_time(String thingplug_container_latest_recv_time) {
		this.thingplug_container_latest_recv_time = thingplug_container_latest_recv_time;
	}
	public String getThingplug_container_latest_response() {
		return thingplug_container_latest_response;
	}
	public void setThingplug_container_latest_response(String thingplug_container_latest_response) {
		this.thingplug_container_latest_response = thingplug_container_latest_response;
	}
	public int getThingplug_subscription_status() {
		return thingplug_subscription_status;
	}
	public void setThingplug_subscription_status(int thingplug_subscription_status) {
		this.thingplug_subscription_status = thingplug_subscription_status;
	}
	public String getThingplug_subscription_recv_time() {
		return thingplug_subscription_recv_time;
	}
	public void setThingplug_subscription_recv_time(String thingplug_subscription_recv_time) {
		this.thingplug_subscription_recv_time = thingplug_subscription_recv_time;
	}
	public String getThingplug_subscription_response() {
		return thingplug_subscription_response;
	}
	public void setThingplug_subscription_response(String thingplug_subscription_response) {
		this.thingplug_subscription_response = thingplug_subscription_response;
	}	
	public int getThingplug_subscription_retrieve_status() {
		return thingplug_subscription_retrieve_status;
	}
	public void setThingplug_subscription_retrieve_status(int thingplug_subscription_retrieve_status) {
		this.thingplug_subscription_retrieve_status = thingplug_subscription_retrieve_status;
	}
	public String getThingplug_subscription_retrieve_recv_time() {
		return thingplug_subscription_retrieve_recv_time;
	}
	public void setThingplug_subscription_retrieve_recv_time(String thingplug_subscription_retrieve_recv_time) {
		this.thingplug_subscription_retrieve_recv_time = thingplug_subscription_retrieve_recv_time;
	}
	public String getThingplug_subscription_retrieve_response() {
		return thingplug_subscription_retrieve_response;
	}
	public void setThingplug_subscription_retrieve_response(String thingplug_subscription_retrieve_response) {
		this.thingplug_subscription_retrieve_response = thingplug_subscription_retrieve_response;
	}
	public int getThingplug_delete_status() {
		return thingplug_delete_status;
	}
	public void setThingplug_delete_status(int thingplug_delete_status) {
		this.thingplug_delete_status = thingplug_delete_status;
	}
	public String getThingplug_delete_recv_time() {
		return thingplug_delete_recv_time;
	}
	public void setThingplug_delete_recv_time(String thingplug_delete_recv_time) {
		this.thingplug_delete_recv_time = thingplug_delete_recv_time;
	}
	public String getThingplug_delete_response() {
		return thingplug_delete_response;
	}
	public void setThingplug_delete_response(String thingplug_delete_response) {
		this.thingplug_delete_response = thingplug_delete_response;
	}
	@Override
	public String toString() {
		return "ThingPlugVO [bpoint_ltid=" + bpoint_ltid + ", thingplug_status=" + thingplug_status
				+ ", thingplug_read_time=" + thingplug_read_time + ", thingplug_node_status=" + thingplug_node_status
				+ ", thingplug_node_recv_time=" + thingplug_node_recv_time + ", thingplug_node_response="
				+ thingplug_node_response + ", thingplug_remoteCES_status=" + thingplug_remoteCES_status
				+ ", thingplug_remoteCES_recv_time=" + thingplug_remoteCES_recv_time + ", thingplug_remoteCSE_response="
				+ thingplug_remoteCSE_response + ", thingplug_container_latest_status="
				+ thingplug_container_latest_status + ", thingplug_container_latest_recv_time="
				+ thingplug_container_latest_recv_time + ", thingplug_container_latest_response="
				+ thingplug_container_latest_response + ", thingplug_subscription_status="
				+ thingplug_subscription_status + ", thingplug_subscription_recv_time="
				+ thingplug_subscription_recv_time + ", thingplug_subscription_response="
				+ thingplug_subscription_response + ", thingplug_subscription_retrieve_status="
				+ thingplug_subscription_retrieve_status + ", thingplug_subscription_retrieve_recv_time="
				+ thingplug_subscription_retrieve_recv_time + ", thingplug_subscription_retrieve_response="
				+ thingplug_subscription_retrieve_response + ", thingplug_delete_status=" + thingplug_delete_status
				+ ", thingplug_delete_recv_time=" + thingplug_delete_recv_time + ", thingplug_delete_response="
				+ thingplug_delete_response + "]";
	}
	
	
}
