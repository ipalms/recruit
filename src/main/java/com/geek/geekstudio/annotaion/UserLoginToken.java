package com.geek.geekstudio.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 进行用户登录验证
 */
/**
 * 自定义注解搭配反射使用时，注解的作用范围应该到运行时--即RetentionPolicy.RUNTIME
 * 这样在VM运行时方法区会保存该注解的信息
 *
 * 需要token注解,管理员注解和超级管理员注解其实可以使用一个注解完成，在注解中加入type的属性
 * 在AuthenticationInterceptor只需要区判断属性值就行
 * 定义多个注解也行，更清晰易懂，但是使用的注解数就会增加
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginToken {
}
