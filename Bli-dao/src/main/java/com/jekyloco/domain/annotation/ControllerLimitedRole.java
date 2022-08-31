package com.jekyloco.domain.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface ControllerLimitedRole {

    String[] limitedRoleCodeList() default {};
}
