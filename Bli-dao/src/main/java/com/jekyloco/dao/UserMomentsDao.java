package com.jekyloco.dao;

import com.jekyloco.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

@Mapper
public interface UserMomentsDao {
    Integer addUserMoments(UserMoment userMoment);
}
