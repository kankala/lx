package kr.co.rmtechs.bpoint_domain;

public enum eZIP_CITY_TYPE {
	SEOUL(1), GWANGJU(2), DAEGU(3), DAEJEON(4), 
	BUSAN(5), SEJONG(6), ULSAN(7), INCHEON(8);
	private final int value;
	private eZIP_CITY_TYPE(int value) { this.value = value; }
	public int getValue() {  return value; }
}
