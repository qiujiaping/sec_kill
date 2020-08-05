package com.qjp.sec_kill.validator;

import com.alibaba.druid.util.StringUtils;
import com.qjp.sec_kill.util.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * description: @isMobile注解真正交给该类真正干活
 * date: 2020/5/18 22:37
 * author: 雨夜微凉
 * version: 1.0
 */

//String为被注解的属性类型，isMobile为注解
public class IsMobileValidator implements ConstraintValidator<isMobile, String> {

    private boolean required = false;

    public void initialize(isMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    //value为注解的属性值
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(required) {
            return ValidatorUtil.isMobile(value);
        }else {
            if(StringUtils.isEmpty(value)) {
                return true;
            }else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }

}
