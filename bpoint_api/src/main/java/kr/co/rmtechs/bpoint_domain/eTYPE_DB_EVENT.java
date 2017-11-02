package kr.co.rmtechs.bpoint_domain;

public enum eTYPE_DB_EVENT {
	EVENT_INSERT(1),
	EVENT_UPDATE(2),
	EVENT_DELETE(3);
	
	private final int value;
	private eTYPE_DB_EVENT(int value) { this.value = value; }
	public int getValue() {  return value; }
}
