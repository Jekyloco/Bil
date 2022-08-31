package com.jekyloco.domain.auth;

import lombok.Data;

import java.util.Date;

@Data
public class AuthElementOperation {
    private Long id;

    private String elementName;

    private String elementCode;

    private String operationType;

    private Date createTime;

    private Date updateTime;
}
