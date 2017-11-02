package kr.co.rmtechs.bpoint_domain;

public enum eTYPE_USER_RANK {
	
	USER_RANK_ADMIN_CHAR('1'),
	USER_RANK_OPERATOR_CHAR('2'),
	USER_RANK_PART_OP_CHAR('3');
		
	private final char value;
	private eTYPE_USER_RANK(char value) { this.value = value; }
	public char getValue() {  return value; }
}
