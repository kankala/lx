package kr.co.rmtechs.bpoint_domain.vo;

import javax.annotation.Generated;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Repository
@JsonInclude(JsonInclude.Include.NON_NULL )
@Generated("org.jsonschema2pojo")
public class BpointDevEuiVO {
	@JsonProperty("dev_eui")	
	private String bpoint_ltid;
	
	public String getBpoint_ltid() {
		return bpoint_ltid;
	}
	public void setBpoint_ltid(String bpoint_ltid) {
		this.bpoint_ltid = bpoint_ltid;
	}
	
	@Override
	public String toString() {
		return "BpointDevEui [bpoint_ltid=" + bpoint_ltid + "]";
	}
}
