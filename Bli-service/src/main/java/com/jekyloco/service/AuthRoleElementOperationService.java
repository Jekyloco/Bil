package com.jekyloco.service;

import com.jekyloco.dao.AuthRoleElementOperationDao;
import com.jekyloco.domain.auth.AuthRoleElementOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleElementOperationService {

    @Autowired

    private AuthRoleElementOperationDao authRoleElementOperationDao;

    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
        return authRoleElementOperationDao.getRoleElementOperationsByRoleIds(roleIdSet);
    }
}
