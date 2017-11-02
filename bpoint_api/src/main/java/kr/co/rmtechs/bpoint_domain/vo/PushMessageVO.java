package kr.co.rmtechs.bpoint_domain.vo;


import java.util.List;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL )
@Generated("org.jsonschema2pojo")
public class PushMessageVO {
	@JsonProperty("send_type")
	private String send_type;
	@JsonProperty("pk_code")
	private String pk_code;
	@JsonProperty("sub_code")
	private List<String> sub_code;
	@JsonProperty("title")
	private String title;
	@JsonProperty("body")
	private String body;
	@JsonProperty("event_time")
	private String event_time;
	
	public void setPushMessageVo(String send_type, String pk_code, 
			List<String> sub_code, String title, String body, String event_time){
		this.send_type = send_type;
		this.pk_code = pk_code;
		this.sub_code = sub_code;
		this.title = title;
		this.body = body;
		this.event_time = event_time;
	}
	
	public String getSend_type() {
		return send_type;
	}
	public void setSend_type(String send_type) {
		this.send_type = send_type;
	}
	public String getPk_code() {
		return pk_code;
	}
	public void setPk_code(String pk_code) {
		this.pk_code = pk_code;
	}
	public List<String> getSub_code() {
		return sub_code;
	}
	public void setSub_code(List<String> sub_code) {
		this.sub_code = sub_code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
		
	public String getEvent_time() {
		return event_time;
	}

	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}

	@Override
	public String toString() {
		return "PushMessageVO [send_type=" + send_type + ", pk_code=" + pk_code + ", sub_code=" + sub_code + ", title="
				+ title + ", body=" + body + ", event_time=" + event_time + "]";
	}

}
