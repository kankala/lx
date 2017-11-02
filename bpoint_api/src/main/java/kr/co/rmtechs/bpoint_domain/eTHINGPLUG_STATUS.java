package kr.co.rmtechs.bpoint_domain;

public enum eTHINGPLUG_STATUS {
	NOT_REGISTED(0), READ_NODE(1), NODE(2), 
	READ_REMOTE_CSE(3), REMOTE_CSE(4), 
	READ_CONTAINER_LORA(5),	CONTAINER_LORA(6), 
	READ_SUBSCRIPTION_RETRIEVE(7), SUBSCRIPTION_RETRIEVE(8),
	READ_SUBSCRIPTION(9), SUBSCRIPTION_ACTIVE(10), 
	READ_SUBSCRIPTION_UPDATE(11), SUBSCRIPTION_UPDATE(12),
	READ_SUBSCRIPTION_DEACTIVE(13), SUBSCRIPTION_DEACTIVE(14), 
	STATUS_FINISHED(15), STATUS_DEACTIVE_FINISHED(16), 
	SUBSCRIPTION_PUSH(17), SUBSCRIPTION_PUSH_FAIL(18),SUBSCRIPTION_PUSH_FAILBACK(19),
	CONTENTS_FAIL(20), BPOINT_ADD(21), BPOINT_DELETE(22);
	private final int value;
	private eTHINGPLUG_STATUS(int value) { this.value = value; }
	public int getValue() {  return value; }
	
	public String getName() {
		switch(value){
		case 0:	return ("NOT_REGISTED");
		case 1: return ("READ_NODE");
		case 2: return ("NODE");
		case 3: return ("READ_REMOTE_CSE");
		case 4: return ("REMOTE_CSE");
		case 5: return ("READ_CONTAINER_LORA");
		case 6: return ("CONTAINER_LORA");
		case 7: return ("READ_SUBSCRIPTION_RETRIEVE");
		case 8: return ("SUBSCRIPTION_RETRIEVE");
		case 9: return ("READ_SUBSCRIPTION_ACTIVE");
		case 10: return ("SUBSCRIPTION_ACTIVE");
		case 11: return ("READ_SUBSCRIPTION_UPDATE");
		case 12: return ("SUBSCRIPTION_UPDATE");
		case 13: return ("READ_SUBSCRIPTION_DEACTIVE");
		case 14: return ("SUBSCRIPTION_DEACTIVE");
		case 15: return ("FINISHED");
		case 16: return ("FINISHED_DEACTIVE");
		case 17: return ("SUBSCRIPTION_PUSH");
		case 18: return ("SUBSCRIPTION_PUSH_FAIL");
		case 19: return ("SUBSCRIPTION_PUSH_FAILBACK");
		case 20: return ("CONTENTS_FAIL");
		case 21: return ("경계점 등록");
		case 22: return ("경계점 삭제");
		default:
			return ("NotDefineMethod : " + value);
		}
	}
	
	public String getCodeValue(){
		if( (value>=NOT_REGISTED.getValue()) &&
			(value<CONTENTS_FAIL.getValue()) ) return "" + (400+value);
		return "" + (400+CONTENTS_FAIL.getValue());
	}
	
}
