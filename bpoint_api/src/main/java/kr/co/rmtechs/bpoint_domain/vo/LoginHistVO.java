package kr.co.rmtechs.bpoint_domain.vo;

public class LoginHistVO {
	private int login_seq;
	private String login_time;
	private String member_id;
	private String login_ip;
	private char   login_os;
	private char   login_status;
	private String login_mdn;
	
	public int getLogin_seq() {
		return login_seq;
	}
	public void setLogin_seq(int login_seq) {
		this.login_seq = login_seq;
	}
	public String getLogin_time() {
		return login_time;
	}
	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getLogin_ip() {
		return login_ip;
	}
	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}
	public char getLogin_os() {
		return login_os;
	}
	public void setLogin_os(char login_os) {
		this.login_os = login_os;
	}
	public char getLogin_status() {
		return login_status;
	}
	public void setLogin_status(char login_status) {
		this.login_status = login_status;
	}
	public String getLogin_mdn() {
		return login_mdn;
	}
	public void setLogin_mdn(String login_mdn) {
		this.login_mdn = login_mdn;
	}
	
	
}
