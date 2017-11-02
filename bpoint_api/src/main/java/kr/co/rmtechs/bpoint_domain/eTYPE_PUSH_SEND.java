package kr.co.rmtechs.bpoint_domain;

public enum eTYPE_PUSH_SEND {
	LTID_BASE("100"),
	LTID_ADD("101"),
	LTID_MODIFY("102"),
	LTID_DEL("103"),
	LTID_ATIVATE("104"),
	LTID_DEACTIVATE("105"),
	LTID_FAIL("110"),
	LTID_FAILBACK("111"),
	
	GROUP_BASE("200"),
	GROUP_ADD("201"),
	GROUP_DEL("202"),
	
	SET_BASE("300"),
	SET_ALARM("301");
	
	private final String value;
	private eTYPE_PUSH_SEND(String value) { this.value = value; }
	public String getValue() {  return value; }
}
