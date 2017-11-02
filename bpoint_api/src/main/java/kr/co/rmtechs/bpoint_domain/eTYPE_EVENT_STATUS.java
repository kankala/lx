package kr.co.rmtechs.bpoint_domain;

public enum eTYPE_EVENT_STATUS {
	STATUS_ADD(1),
	STATUS_ACTIVATE(2),
	STATUS_DEACTIVATE(3),
	STATUS_DELETE(4),
	STATUS_MODIFY(5),
	STATUS_FAIL(10),
	STATUS_FAILBACK(11),
	STATUS_LTID_GROUP_ADD(20),
	STATUS_LTID_GROUP_DELETE(21);
	
	private final int value;
	private eTYPE_EVENT_STATUS(int value) { this.value = value; }
	public int getValue() {  return value; }
	
	public String getName(){
		switch(value)
		{
		case 1: return "등록";
		case 2: return "활성화";
		case 3: return "비활성화";
		case 4: return "삭제";
		case 5: return "변경";
		case 10: return "장애";
		case 11: return "장애해제";
		case 20: return "그룹등록";
		case 21: return "그룹삭제";
		}
		return "not define";
	}
	
}
