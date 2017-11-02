package kr.co.rmtechs.bpoint_domain.vo;

import javax.annotation.Generated;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Repository
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class BpointThingplugStepVO {
	@JsonProperty("dev_eui")
	private String dev_eui;
	@JsonProperty("code")
	private String code;
	@JsonProperty("err_msg")
	private String err_msg;
	@JsonProperty("sub_msg")
	private String sub_msg;
	@JsonProperty("response_body")
	private String response_body;
	@JsonProperty("body")
	private String body;
	@JsonProperty("url")
	private String url;
	@JsonProperty("header")
	private String header;

	public String getDev_eui() {
		return dev_eui;
	}
	public void setDev_eui(String dev_eui) {
		this.dev_eui = dev_eui;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getErr_msg() {
		return err_msg;
	}
	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
	public String getSub_msg() {
		return sub_msg;
	}
	public void setSub_msg(String sub_msg) {
		this.sub_msg = sub_msg;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getResponse_body() {
		return response_body;
	}
	public void setResponse_body(String response_body) {
		this.response_body = response_body;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	@Override
	public String toString() {
		return "BpointThingplugStepVO [dev_eui=" + dev_eui + ", code=" + code + ", err_msg=" + err_msg + ", sub_msg="
				+ sub_msg + ", response_body=" + response_body + ", body=" + body + ", url=" + url + ", header="
				+ header + "]";
	}

	
}
