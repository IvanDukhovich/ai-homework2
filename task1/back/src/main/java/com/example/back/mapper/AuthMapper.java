package com.example.back.mapper;

import com.example.back.model.UserAuth;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface AuthMapper {
    @Select("SELECT * FROM user_auth WHERE email = #{email}")
    Optional<UserAuth> findByEmail(String email);
    
    @Insert("INSERT INTO user_auth(name, email, password_hash) " +
            "VALUES (#{name}, #{email}, #{passwordHash})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserAuth userAuth);
} 