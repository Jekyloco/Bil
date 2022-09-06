package com.jekyloco.dao;

import com.jekyloco.domain.File;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface FileDao {

    Integer addFile(File file);
    File getFileByMD5(String md5);
}
