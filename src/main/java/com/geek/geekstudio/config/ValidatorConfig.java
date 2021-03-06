package com.geek.geekstudio.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
  *数据校验配置: 配置hibernate Validator为快速失败返回模式（参数错一个就抛出异常）
 */
@Configuration
public class ValidatorConfig {
    /**
     * 快速失败返回模式：快速失败返回模式只要有一个验证失败，则返回 两种配置
     */

    //@Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        return validator;
    }

   /* public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .failFast( true )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }*/
}
