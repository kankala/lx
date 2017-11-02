package kr.co.rmtechs.bpoint_domain.vo;

import java.util.List;

import javax.annotation.Generated;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Repository
@JsonInclude(JsonInclude.Include.NON_NULL )
@Generated("org.jsonschema2pojo")
public class BpointBaseVO extends BpointDevEuiVO{

		@JsonProperty("name")
		private String bpoint_name;
		@JsonProperty("status")
		private char bpoint_status;
		@JsonProperty("latitude")
		private double bpoint_latitude;
		@JsonProperty("longitude")
		private double bpoint_longitude;
		@JsonProperty("fail_status")
		private char bpoint_fail_status;
		@JsonProperty("reg_time")
		private String dt_insert;
		@JsonProperty("group_array")
		private List<Integer> bpoint_group_array;
		

		public String getBpoint_name() {
			return bpoint_name;
		}
		public void setBpoint_name(String bpoint_name) {
			this.bpoint_name = bpoint_name;
		}
		public char getBpoint_status() {
			return bpoint_status;
		}
		public void setBpoint_status(char bpoint_status) {
			this.bpoint_status = bpoint_status;
		}
		public double getBpoint_latitude() {
			return bpoint_latitude;
		}
		public void setBpoint_latitude(double bpoint_latitude) {
			this.bpoint_latitude = bpoint_latitude;
		}
		public double getBpoint_longitude() {
			return bpoint_longitude;
		}
		public void setBpoint_longitude(double bpoint_longitude) {
			this.bpoint_longitude = bpoint_longitude;
		}
		public char getBpoint_fail_status() {
			return bpoint_fail_status;
		}
		public void setBpoint_fail_status(char bpoint_fail_status) {
			this.bpoint_fail_status = bpoint_fail_status;
		}
		public String getDt_insert() {
			return dt_insert;
		}
		public void setDt_insert(String dt_insert) {
			this.dt_insert = dt_insert;
		}
		public List<Integer> getBpoint_group_array() {
			return bpoint_group_array;
		}
		public void setBpoint_group_array(List<Integer> bpoint_group_array) {
			this.bpoint_group_array = bpoint_group_array;
		}
		
		@Override
		public String toString() {
			return "BpointBaseVO [bpoint_name=" + bpoint_name + ", bpoint_status=" + bpoint_status
					+ ", bpoint_latitude=" + bpoint_latitude + ", bpoint_longitude=" + bpoint_longitude
					+ ", bpoint_fail_status=" + bpoint_fail_status + ", dt_insert=" + dt_insert
					+ ", bpoint_group_array=" + bpoint_group_array + ", getBpoint_ltid()=" + getBpoint_ltid() + "]";
		}
	
}
