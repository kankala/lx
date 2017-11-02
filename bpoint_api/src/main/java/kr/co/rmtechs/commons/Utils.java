package kr.co.rmtechs.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class Utils {
	public static final SimpleDateFormat DATE_YMD_FORMAT	= new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DATE_YMD_KR_FORMAT	= new SimpleDateFormat("yyyy년 MM월 dd일");
	public static final SimpleDateFormat DATE_YM_FORMAT	= new SimpleDateFormat("yyyy-MM");
	public static final SimpleDateFormat DATE_YM_KR_FORMAT	= new SimpleDateFormat("yyyy년 MM월");
	
	public static final SimpleDateFormat DATE_HEADER_FORMAT	= new SimpleDateFormat("yyyy년 MM월 dd일(E)");

	
	public static final SimpleDateFormat DATETIME_FORMAT	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATETIME_TIME_FORMAT	= new SimpleDateFormat("HH:mm:ss");
	
	
	public static String byteToHex(byte b) {
		String hex;
		
		hex = "" + Integer.toHexString(0xff & b);
		
		return hex;
	}
	
	public static String byteArrayToHex(byte[] ba) {
		String hex = "";
		
		for(byte b : ba) {
			hex += "" + Integer.toHexString(0xff & b);
			hex += " ";
		}
		
		return hex;
	}
	
	public static String byteArrayToHex(byte[] ba, int limit) {
		String hex = "";
		String str;
		for(int i = 0 ; i < limit ; i++) {
			byte b = ba[i];
			str = Integer.toHexString(0xff & b);
			if(str.length() == 1)
				str = "0" + str;

			hex += "" + str;
			hex += " ";
		}
		
		return hex;
	}	
	
	public static String getPhoneNumber(String no) {
		if(StringUtils.isEmpty(no))
			return "";
		
		return no;
	}
	
	
	public static int parseInt(String a) {
		if(a.length() <= 0)
			return 0;
		
		String i = a.replaceAll(",", "");
		i = i.replaceAll("_", "");
		i = i.replaceAll("/", "");
		i = i.trim();
		if(i.length() <= 0)
			return 0;

		return Integer.parseInt(i);
	}
	
	
	public static double parseDouble(String a) {
		if(a.length() <= 0)
			return 0;
		
		String i = a.replaceAll(",", "");
		i = i.trim();
		if(i.length() <= 0)
			return 0;

		return Double.parseDouble(i);
	}
	
	public static long parseLong(String a) {
		if(a.length() <= 0)
			return 0;
		
		String i = a.replaceAll(",", "");
		i = i.trim();
		if(i.length() <= 0)
			return 0;

		return Long.parseLong(i);
	}	
	
	/**
	 * 현재 날짜 YYYY-mm-dd 표시
	 * @return
	 */
	public static String getCurYMD() {
		Calendar cal = Calendar.getInstance();

		return DATE_YMD_FORMAT.format(cal.getTime());
	}
	
	
	/**
	 * 현재 날짜 YYYY년 mm월 dd일 표시
	 * @return
	 */
	public static String getCurYMDKR() {
		Calendar cal = Calendar.getInstance();

		return DATE_YMD_KR_FORMAT.format(cal.getTime());
	}
	
	/**
	 * 현재 월 YYYY-mm 표시
	 * @return
	 */
	public static String getCurYM() {
		Calendar cal = Calendar.getInstance();

		return DATE_YM_FORMAT.format(cal.getTime());
	}
	
	
	/**
	 * 해당 월 YYYY년 mm월 표시
	 * @return
	 */
	public static String getDateYMKR(String dt) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Utils.parseInt(dt.substring(0, 4)));
		cal.set(Calendar.MONTH, Utils.parseInt(dt.substring(5, 7)) -1);
		cal.set(Calendar.DAY_OF_MONTH, Utils.parseInt(dt.substring(8, 10)));
		
		return DATE_YM_KR_FORMAT.format(cal.getTime());
	}
	
	/**
	 * 해당 날짜 YYYY-mm-dd 표시
	 * @return
	 */
	public static String getDateYMD(String dt) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Utils.parseInt(dt.substring(0, 4)));
		cal.set(Calendar.MONTH, Utils.parseInt(dt.substring(5, 7)) -1);
		cal.set(Calendar.DAY_OF_MONTH, Utils.parseInt(dt.substring(8, 10)));
		
		return DATE_YMD_FORMAT.format(cal.getTime());
	}
	
	
	/**
	 * 해당 날짜 YYYY년 mm월 dd일 표시
	 * @return
	 */
	public static String getDateYMDKR(String dt) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Utils.parseInt(dt.substring(0, 4)));
		cal.set(Calendar.MONTH, Utils.parseInt(dt.substring(5, 7)) -1);
		cal.set(Calendar.DAY_OF_MONTH, Utils.parseInt(dt.substring(8, 10)));
		
		return DATE_YMD_KR_FORMAT.format(cal.getTime());
	}
	
	/**
	 * 해당 월 YYYY-mm 표시
	 * @return
	 */
	public static String getDateYM(String dt) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Utils.parseInt(dt.substring(0, 4)));
		cal.set(Calendar.MONTH, Utils.parseInt(dt.substring(5, 7)) -1);
		cal.set(Calendar.DAY_OF_MONTH, Utils.parseInt(dt.substring(8, 10)));
		
		return DATE_YM_FORMAT.format(cal.getTime());
	}
	
	
	/**
	 * 현재 월 YYYY년 mm월 표시
	 * @return
	 */
	public static String getCurYMKR() {
		Calendar cal = Calendar.getInstance();

		return DATE_YM_KR_FORMAT.format(cal.getTime());
	}
	
	
	public static String getCurDateTime() {
		Calendar cal = Calendar.getInstance();
		return DATETIME_FORMAT.format(cal.getTime());
	}
	
	public static String getCurDateTimeForStrTODate() {
		Calendar cal = Calendar.getInstance();
		
		return DATE_YMD_FORMAT.format(cal.getTime()) + "T" + DATETIME_TIME_FORMAT.format(cal.getTime());
	}	

	/**
	 * 공통 Header의 표시되는 날짜
	 * YYYY년 MM월 DD일(WEEK)
	 * @return
	 */
	public static String getHeaderDate() {
		Calendar cal = Calendar.getInstance();
		
		return DATE_HEADER_FORMAT.format(cal.getTime());
	}
	
	
	
	
	
	/**
	 * 해당일자의 해당월의 몇주차인지 계산처리한다.
	 * @param dt
	 * @return
	 */
	public static int getWeekIndex(String dt) {
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.YEAR, Utils.parseInt(dt.substring(0, 4)));
		cal.set(Calendar.MONTH, Utils.parseInt(dt.substring(5, 7)) -1);
		cal.set(Calendar.DAY_OF_MONTH, Utils.parseInt(dt.substring(8, 10)));
		
		int dw = cal.get(Calendar.WEEK_OF_MONTH);
		
		return dw;
	}
	
	
	/**
	 * 해당일자의 주차를 계산하고 주차의 시작일을 반환한다.
	 * @param dt YYYY-MM-DD 표현식
	 * @return
	 */
	public static String getWeekStartYMD(String dt) {
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.YEAR, Utils.parseInt(dt.substring(0, 4)));
		cal.set(Calendar.MONTH, Utils.parseInt(dt.substring(5, 7)) -1);
		cal.set(Calendar.DAY_OF_MONTH, Utils.parseInt(dt.substring(8, 10)));
		
		int dw = cal.get(Calendar.DAY_OF_WEEK);
		
		return getDateAdd(Calendar.DAY_OF_MONTH, dt, (dw -1) * -1);
	}
	
	/**
	 * Date Add
	 * @param ymd_kb	Calendar.YEAR, MONTH, DAY_OF_MONTH etc.
	 * @param dt		DateTime (YYYY-mm-dd, YYYY/mm/dd)
	 * @param add_val	Add value
	 * @return
	 */
	public static String getDateAdd(int ymd_kb, String dt, int add_val) {
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.YEAR, Utils.parseInt(dt.substring(0, 4)));
		cal.set(Calendar.MONTH, Utils.parseInt(dt.substring(5, 7)) -1);
		cal.set(Calendar.DAY_OF_MONTH, Utils.parseInt(dt.substring(8, 10)));
		
		cal.add(ymd_kb, add_val);
		
		return DATE_YMD_FORMAT.format(cal.getTime());
	}
	
	
	/**
	 * Request Body 부분을 검출한다.
	 * @param req
	 * @return
	 */
	public static String getRequestBody(HttpServletRequest req) throws Exception {
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
 
        try {
            InputStream inputStream = req.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
 
        body = stringBuilder.toString();
        return body;		
	}
	
	
	
	
	/**
	 * value 값을 소수점 point까지 계산한다.
	 * value[last]의 첫번째 byte는 +, - 로 처리
	 * 
	 * 2017-07-17 Little endian으로 계산처리	 *
	 *  
	 * @param value
	 * @param point
	 */
	public static double calcByteToDouble(byte[] value, int point) {
		int plus_minus = (value[value.length -1] >> 7 & 0x01) == 0x01 ? -1 : 1;
		value[value.length -1] = (byte)(value[value.length -1] & 0x7f);		// 01111111 AND 연산처리로 부호비트 제거
		
		double d = 0;
		int n = 0;
		for(int i = 0; i < value.length ; i++) {
			d += (Byte.toUnsignedInt(value[i]) << (8 * n));
			n++;
		}
		
		return d * plus_minus / Math.pow(10, point);
	}
	
	
	public static byte[] getUnitCD(String unit_cd) {
		byte[] buf = new byte[16];
		int idx = buf.length - unit_cd.length();
		System.arraycopy(unit_cd.getBytes(), 0, buf, idx, unit_cd.getBytes().length);
		
		return buf;
	}
	
	public static byte[] getRTCToByte() {
		final long _time = System.currentTimeMillis() / 1000;
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(_time);
	    byte[] arr = buffer.array();
	    
	    byte[] rval = new byte[4];
	    System.arraycopy(arr, 4, rval, 0, rval.length);
	    
	    return rval;
	}
	
	public static void main(String[] args) {
		System.out.println(new String(Utils.getUnitCD("861921031225233")));
		
	}
}
