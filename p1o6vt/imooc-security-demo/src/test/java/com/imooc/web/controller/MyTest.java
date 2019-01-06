package com.imooc.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;

/**
 * Created by 爱喝咖啡的土拨鼠 on 2018/11/18 0018.
 */
public class MyTest {
    private static final String SESSION_CODE="SESSION_CODE_";
    public static void main(String[] args) {
        String str = "woainiguoyiqing";
        String before = StringUtils.substringBefore(str, "i");
        System.out.println(before);
        SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
        System.out.println("验证码是否一样"+StringUtils.equalsIgnoreCase("str", "STR"));
    }
}
