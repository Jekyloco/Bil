package com.jekyloco.dao;

import com.jekyloco.domain.auth.AuthRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AuthRoleMenuDao {
    List<AuthRoleMenu> getAuthRoleMenus(@Param("roleIdSet") Set<Long> roleIdSet);

}
