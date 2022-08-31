package com.jekyloco.service;

import com.jekyloco.dao.AuthRoleDao;
import com.jekyloco.domain.auth.AuthRole;
import com.jekyloco.domain.auth.AuthRoleElementOperation;
import com.jekyloco.domain.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleService {

    @Autowired
    private AuthRoleElementOperationService authRoleElementOperationService;

    @Autowired
    private AuthRoleMenuService authRoleMenuService;

    @Autowired
    private AuthRoleDao authRoleDao;

    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
        return authRoleElementOperationService.getRoleElementOperationsByRoleIds(roleIdSet);
    }

    public List<AuthRoleMenu> getAuthRoleMenus(Set<Long> roleIdSet) {
        return authRoleMenuService.getAuthRoleMenus(roleIdSet);
    }

    public AuthRole getRoleByCode(String code) {
        return authRoleDao.getRoleByCode(code);
    }
}
