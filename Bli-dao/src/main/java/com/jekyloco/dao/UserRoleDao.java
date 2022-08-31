package com.jekyloco.dao;

import com.jekyloco.domain.auth.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleDao {
    List<UserRole> getUserRoleByUserId(Long userId);

    void addUserRole(UserRole userRole);
}
