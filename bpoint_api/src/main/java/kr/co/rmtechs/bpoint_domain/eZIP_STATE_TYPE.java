package kr.co.rmtechs.bpoint_domain;

public enum eZIP_STATE_TYPE {
	GANWON(20), GYEONGGI(21), GYEONGSANGNAM(22), GYEONGSANGBUK(23), 
	JEOLLANAM(24), JEOLLABUK(25), CHUNGCHEONGNAM(26), CHUNGCHEONGBUK(27), 
	JEJU(28);
	private final int value;
	private eZIP_STATE_TYPE(int value) { this.value = value; }
	public int getValue() {  return value; }	
}
