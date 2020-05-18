package com.qjp.sec_kill.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/**
 * description: isMobile
 * date: 2020/5/18 22:32
 * author: 雨夜微凉
 * version: 1.0
 */

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IsMobileValidator.class}
)
public @interface isMobile {
    String message() default "手机号码格式错误";
    Class<?>[] groups() default {};
    boolean required() default true;

    Class<? extends Payload>[] payload() default {};
}
