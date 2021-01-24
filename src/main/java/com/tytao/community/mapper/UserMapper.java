package com.tytao.community.mapper;

import com.tytao.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into user(accountid, token, name,gmt_create, gmt_modified)" +
            "values (#{accountId},#{token},#{name},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}
