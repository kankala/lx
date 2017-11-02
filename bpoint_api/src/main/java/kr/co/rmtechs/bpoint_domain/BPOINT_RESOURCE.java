package kr.co.rmtechs.bpoint_domain;

public class BPOINT_RESOURCE {
	public static final int APP_EUI_SIZE = 16;
	public static final int APP_EUI_BOTTOM_SIZE = 8;
	public static final int DEV_EUI_SIZE = 16;
	public static final int LTID_SIZE = APP_EUI_BOTTOM_SIZE + DEV_EUI_SIZE;
	public static final String THINGPLUG_APP_EUI = "0050781000000023";
	
	public static final char STATUS_NORMAL ='1';
	public static final char STATUS_FAIL = '2';
	
	
	public static final String MODEL = "EQ00001";
	
/*		
	public final String ConvertDeveuiToLtid(String appEui, String ltid){
		return appEui.substring(APP_EUI_BOTTOM_SIZE)+ltid;
		//return THINGPLUG_APP_EUI.substring(APP_EUI_BOTTOM_SIZE)+ltid;
	}
*/	
}
