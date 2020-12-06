package com.geek.geekstudio.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * 解析传入的json字符串
 */
@Component
public class ParamParseUtil {
    /**
     * 解析传入的单参 获得其值
     */
    public static String parseSingleParam(String param,String name){
        JSONObject json= JSON.parseObject(param);
        String word= (String) json.get(name);
        return word;
    }
}
