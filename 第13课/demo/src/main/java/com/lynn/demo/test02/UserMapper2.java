package com.lynn.demo.test02;

import com.lynn.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper2 {

    @Insert("insert into test_user1(name,age) values(#{name},#{age})")
    void addUser(@Param("name") String name,@Param("age") int age);
}
