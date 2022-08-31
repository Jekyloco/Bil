package com.jekyloco.dao;

import com.jekyloco.domain.auth.AuthRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleDao {
    AuthRole getRoleByCode(String code);
}
