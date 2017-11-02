package kr.co.rmtechs.bpoint_domain;

public enum eTYPE_EVENT_STATUS_CHAR {
	STATUS_ADD_CHAR('1'),
	STATUS_ACTIVATE_CHAR('2'),
	STATUS_DEACTIVATE_CHAR('3'),
	STATUS_DELETE_CHAR('4');
	
	private final char value;
	private eTYPE_EVENT_STATUS_CHAR(char value) { this.value = value; }
	public char getValue() {  return value; }
	
	public String getName(){
		switch(value){
		case '1': return "추가";
		case '2': return "활성화";
		case '3': return "비활성화";
		case '4': return "삭제";
		}
		return "not define";
	}
}
