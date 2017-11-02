package kr.co.rmtechs.bpoint_domain;

public enum eTYPE_SUBSCRIPTION_EVENT_TYPE {
	CHILD_CREATED('1'), CHILD_DELETED('2'),
	UPDATED('3'), DELETED('4'), CHILD_UPDATED('5');
	private final char value;
	private eTYPE_SUBSCRIPTION_EVENT_TYPE(char value) { this.value = value; }
	public char getValue() {  return value; }
}
