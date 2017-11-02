package kr.co.rmtechs.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class DateTimeUtil {
	public static final String yyyy_MM_dd__HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String yyyy_MM_dd__HH_mm = "yyyy-MM-dd HH:mm";
	public static final String yyyy_MM_dd__HH = "yyyy-MM-dd HH";
	
	public static final String ITMS_yyyy_MM_dd__HH = "yyyy-MM-dd_HH";
	
	public static final String yyyy_MM_dd = "yyyy-MM-dd";
	public static final String yyyy_ = "yyyy-";
	public static final String yyyyMMdd = "yyyyMMdd";
	public static final String yyyyMMddHHmmss_SSS = "yyyyMMddHHmmss.SSS";
	public static final String MM_dd = "MM-dd";
	public static final String MM_dd_HH_mm = "MM-dd HH:mm";
	public static final String MM_dd_HH_mm_ss = "MM-dd HH:mm:ss";
	public static final String HH_mm = "HH:mm";
	public static final String yyyy_MM_dd__HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

	
	public static int	DB_SUNDAY	= 1;
	public static int	DB_MONDAY	= 2;
	public static int	DB_TUESDAY	= 3;
	public static int	DB_WEDNESDAY =4;
	public static int	DB_THURSDAY	= 5;
	public static int	DB_FRIDAY	= 6;
	public static int	DB_SATURDAY	= 7;
	
		
	public static int getDayOfWeekToDB(){
		switch(LocalDate.now().getDayOfWeek()){
		case SUNDAY:
			return DB_SUNDAY;
		case MONDAY:
			return DB_MONDAY;
		case TUESDAY:
			return DB_TUESDAY;
		case WEDNESDAY:
			return DB_WEDNESDAY;
		case THURSDAY:
			return DB_THURSDAY;
		case FRIDAY:
			return DB_FRIDAY;
		default:
			return DB_SATURDAY;
		}
	}
	
	public static String getNow_YYYYMMdd() {
		LocalDateTime localtime = LocalDateTime.now();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		return localtime.format(dtf);
	}
	public static String getNow_YYYYMMddHHmm() {
		LocalDateTime localtime = LocalDateTime.now();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		return localtime.format(dtf);
	}
	public static String getNow_YYYYMMddHHmmss() {
		LocalDateTime localtime = LocalDateTime.now();

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return localtime.format(dtf);
	}

	public static long beforeTime_sec(LocalDateTime input){
		LocalDateTime localtime = LocalDateTime.now();
		return localtime.until(input,  ChronoUnit.SECONDS);
	}
	
	public static long afterTime_sec(LocalDateTime input){
		LocalDateTime localtime = LocalDateTime.now();
		return input.until(localtime,  ChronoUnit.SECONDS);
	}

	public static boolean diffTime_10MIN(String input){
		return diffTime_10MIN(input, yyyy_MM_dd__HH_mm);
	}
	
	public static boolean diffTime_10MIN(String input, String pattern){
		long time = diffTime_sec(input, pattern);
		if((time>=0) && (time>600)) return true;
		
		return false;
	}
	
	// 시간을 비교하여 무조건 +로 return 한다.
	// - 인경우는 문제가 존재함.
	public static long diffTime_sec(String input, String pattern){
		long returnTime = -1;
		
		try{		
			pattern = pattern.replaceAll("Y", "y");
		
			Date inputDate = stringToTime(input, pattern);
		
			 Instant instant = Instant.ofEpochMilli(inputDate.getTime());
			 LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			 returnTime =  afterTime_sec(ldt);
			 if(returnTime<0){
				 returnTime =  beforeTime_sec(ldt);
			 }
		}catch(Exception e){
			returnTime = -1;
		}
		
		return returnTime;
	}
	
	public static long afterTime_sec(String input, String pattern){
		LocalDateTime ldt;
		try{		
			pattern = pattern.replaceAll("Y", "y");
		
			Date inputDate = stringToTime(input, pattern);
		
			 Instant instant = Instant.ofEpochMilli(inputDate.getTime());
			 ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			
		}catch(Exception e){
			return -1;
		}
		return afterTime_sec(ldt);
	}
	
	public static long beforeTime_sec(String input, String pattern){
		LocalDateTime ldt;
		try{
			pattern = pattern.replaceAll("Y", "y");	
			
			Date inputDate = stringToTime(input, pattern);
			if(inputDate==null) return -1;

			ldt = LocalDateTime.ofInstant(inputDate.toInstant(), ZoneId.systemDefault());
			
		}catch(Exception e){
			return -1;
		}
		return beforeTime_sec(ldt);
	}

	public static Date stringToTime(String input, String pattern) throws ParseException{
		if(input==null || pattern==null) return null;
		pattern = pattern.replaceAll("Y", "y");	
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		return sdf.parse(input);
	}
	
	public static String getYYYYMMdd(int year, int month, int day, int hour, int min, int ss) {
		
		LocalDateTime localtime = LocalDateTime.of(year,  month, day, hour, min, ss);
		return "" + localtime.getYear() + localtime.getMonth() + localtime.getDayOfMonth();
	}
	public static String getYYYYMMddHHmm(int year, int month, int day, int hour, int min, int ss) {
		
		LocalDateTime localtime = LocalDateTime.of(year,  month, day, hour, min, ss);
		return "" + localtime.getYear() + localtime.getMonth() + localtime.getDayOfMonth()
			+ localtime.getHour()+localtime.getMinute();
	}	
	public static String getYYYYMMddHHmmss(int year, int month, int day, int hour, int min, int ss) {
		
		LocalDateTime localtime = LocalDateTime.of(year,  month, day, hour, min, ss);
		
		return "" + localtime.getYear() + localtime.getMonth() + localtime.getDayOfMonth()
			+ localtime.getHour()+localtime.getMinute()+localtime.getSecond();
	}
}
