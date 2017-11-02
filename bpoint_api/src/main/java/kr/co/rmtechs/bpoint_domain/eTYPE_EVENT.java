package kr.co.rmtechs.bpoint_domain;

public enum eTYPE_EVENT {
	TYPE_EVENT_LTID('1'),
	TYPE_EVENT_ALARM('2'),
	TYPE_EVENT_GROUP('3'),
	TYPE_EVENT_ALL('4'); 
	
	private final char value;
	private eTYPE_EVENT(char value) { this.value = value; }
	public char getValue() {  return value; }
	
	public String getName(){
		switch(value){
		case '1': return "LTID";
		case '2': return "ALARM";
		case '3': return "GROUP";
		case '4': return "ALL";
		}
		return "not define";
	}
}
