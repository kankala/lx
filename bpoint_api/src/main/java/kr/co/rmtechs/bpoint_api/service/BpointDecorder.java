package kr.co.rmtechs.bpoint_api.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;

import kr.co.rmtechs.commons.DateTimeUtil;

//import org.springframework.security.crypto.encrypt.BytesEncryptor;
//import org.springframework.security.crypto.encrypt.Encryptors;
//import org.springframework.security.crypto.keygen.KeyGenerators;

public class BpointDecorder {
	public static final String main_key = "rudrPwja#123";
	public static final Integer date_key_size = 17 ;//yyyy-MM-DD HH:MM,
													 //1234567890123456
	
	@SuppressWarnings("null")
	public static final Map<String, String> GetDecode(String str_decoding){
		
		Map<String, String> ret_map = new HashMap<String, String>();
		
		try{
			
			BytesEncryptor standardDecriptor = Encryptors.standard(main_key, KeyGenerators.string().generateKey());
		
			byte[] tmpEncode = standardDecriptor.encrypt("2017-05-08 03:52,test".getBytes());
				

			byte[] tmpDecode = standardDecriptor.decrypt(str_decoding.getBytes());
		
			if(tmpDecode.length <= date_key_size){
				ret_map.put("result", "false");
				ret_map.put("message", "date_key_size invalid : "+ tmpDecode.length );
				return ret_map;
			}
		
			String date_key = str_decoding.substring(0, 16); 
			String data = str_decoding.substring(16);
System.out.println("date_key:"+date_key+","+data);
		
			if(data==null && data.length()<=0){
				ret_map.put("result",  "false");
				ret_map.put("message", "don't have encording data");
				return ret_map;
			}
			
			if(DateTimeUtil.diffTime_10MIN(date_key)==false){
				ret_map.put("result",  "false");
				ret_map.put("message", "time over");
				return ret_map;				
			}

		}catch(Exception e){

			ret_map.put("result",  "false");
			ret_map.put("message",  "exception:"+e.toString());
		}
		
		
		return ret_map;
	}
}
