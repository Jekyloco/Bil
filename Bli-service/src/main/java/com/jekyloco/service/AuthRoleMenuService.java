package com.jekyloco.service;

import com.jekyloco.dao.AuthRoleMenuDao;
import com.jekyloco.domain.auth.AuthRoleMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleMenuService {
    @Autowired
    private AuthRoleMenuDao authRoleMenuDao;

    public List<AuthRoleMenu> getAuthRoleMenus(Set<Long> roleIdSet) {
        return authRoleMenuDao.getAuthRoleMenus(roleIdSet);
    }
}
