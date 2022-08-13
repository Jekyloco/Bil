package com.jekyloco.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public String getName(Long id);
}
