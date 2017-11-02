package kr.co.rmtechs.bpoint_domain;

public enum eTYPE_SETTING {
	ALARM(1);
	
	private final int value;
	private eTYPE_SETTING(int value) { this.value = value; }
	public int getValue() {  return value; }
}
