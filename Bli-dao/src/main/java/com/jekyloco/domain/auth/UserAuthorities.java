package com.jekyloco.domain.auth;

import lombok.Data;

import java.util.List;

@Data
public class UserAuthorities {
    List<AuthRoleElementOperation> roleElementOperations;

    List<AuthRoleMenu> roleMenuList;
}
