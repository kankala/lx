package kr.co.rmtechs.bpoint_domain;

public enum eTYPE_PUSH {
	
	READ_STATUS_ADD('1'), 	//등록
	READ_STATUS_READ('2'),	//읽음
	READ_STATUS_SEND('3'),	//전송
	READ_STATUS_RECV('4'),	//수신
	READ_STATUS_FAIL('5');	//실패

	private final char value;
	private eTYPE_PUSH(char value) { this.value = value; }
	public char getValue() {  return value; }
}
