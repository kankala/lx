package kr.co.rmtechs.bpoint_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import kr.co.rmtechs.bpoint_domain.BPOINT_RESOURCE;

//PROPERTY 암호화 JASYPT
@Component
@ConfigurationProperties(prefix="kr.co.rmtechs.bpoint")
public class BpointProperties {
	
	private String properties_name;
	private String push_url;
	private String dev_eui_1;
	private String dev_eui_2;
	private String download_path;
	private String app_eui;
	private String subscription_name;
	private String ukey;
	private String push_token;
	private String udp_port;
	
	public final String ConvertDeveuiToLtid(String ltid){
		return this.app_eui.substring(BPOINT_RESOURCE.APP_EUI_BOTTOM_SIZE).trim()+ltid.trim();
	}
	
	public String getProperties_name() {
		return properties_name;
	}
	public void setProperties_name(String properties_name) {
		this.properties_name = properties_name;
	}
	
	public String getPush_url() {
		return push_url;
	}

	public void setPush_url(String push_url) {
		this.push_url = push_url;
	}

	public String getDev_eui_1() {
		return dev_eui_1;
	}
	public void setDev_eui_1(String dev_eui_1) {
		this.dev_eui_1 = dev_eui_1;
	}
	public String getDev_eui_2() {
		return dev_eui_2;
	}
	public void setDev_eui_2(String dev_eui_2) {
		this.dev_eui_2 = dev_eui_2;
	}
	public String getDownload_path() {
		return download_path;
	}
	public void setDownload_path(String download_path) {
		this.download_path = download_path;
	}
	public String getApp_eui() {
		return app_eui;
	}
	public void setApp_eui(String app_eui) {
		this.app_eui = app_eui;
	}
	public String getSubscription_name() {
		return subscription_name;
	}

	public void setSubscription_name(String subscription_name) {
		this.subscription_name = subscription_name;
	}
	
	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}
	public String getPush_token() {
		return push_token;
	}

	public void setPush_token(String push_token) {
		this.push_token = push_token;
	}

	public boolean existDevEui(String dev_eui) {	
		boolean check = true;
	
		if((dev_eui_1!=null) && (dev_eui_1.length()>0)){
			if(dev_eui.substring(0,dev_eui_1.length()).equals(dev_eui_1) == true) return true;
			check = false;
		}
		
		if((dev_eui_2!=null) && (dev_eui_2.length()>0)){
			if(dev_eui.substring(0,dev_eui_2.length()).equals(dev_eui_2) == true) return true;
			check = false;
		}
		
		if(check == true) return true;
		
		return false;
	}
	
	public String getUdp_port() {
		return udp_port;
	}

	public void setUdp_port(String udp_port) {
		this.udp_port = udp_port;
	}

	@Override
	public String toString() {
		return "BpointProperties [properties_name=" + properties_name + ", push_url=" + push_url + ", dev_eui_1="
				+ dev_eui_1 + ", dev_eui_2=" + dev_eui_2 + ", download_path=" + download_path + ", app_eui=" + app_eui
				+ ", subscription_name=" + subscription_name + ", ukey=" + ukey + ", push_token=" + push_token + "]";
	}

}
