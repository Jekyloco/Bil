package com.jekyloco.controller.aspect;


import com.jekyloco.controller.support.UserSupport;
import com.jekyloco.domain.annotation.ControllerLimitedRole;
import com.jekyloco.domain.auth.UserRole;
import com.jekyloco.exception.ConditionException;
import com.jekyloco.service.UserRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class ControllerLimitedRoleAspect {
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.jekyloco.domain.annotation.ControllerLimitedRole)")
    public void check() {

    }

    @Before("check() && @annotation(controllerLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ControllerLimitedRole controllerLimitedRole) {
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        String[] limitedRoleCodeList = controllerLimitedRole.limitedRoleCodeList();
        Set<String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        roleCodeSet.retainAll(limitedRoleCodeSet);
        if (roleCodeSet.size() > 0) {
            throw new ConditionException("权限不足");
        }
    }
}
