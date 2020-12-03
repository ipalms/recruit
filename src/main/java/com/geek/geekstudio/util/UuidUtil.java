package com.geek.geekstudio.util;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

/**
 * 产生UUID随机字符串工具类
 */
@Component
public class UuidUtil {
	private UuidUtil(){}
	//生成随机uuid
	public static String getUuid(){
		return UUID.randomUUID().toString().replace("-","");
	}

	//生成6位随机验证码
	public static String getActiveCode(){
		Random random = new Random();
		String result="";
		for (int i=0;i<6;i++)
		{
			result+=random.nextInt(10);
		}
		return result;
	}
}
