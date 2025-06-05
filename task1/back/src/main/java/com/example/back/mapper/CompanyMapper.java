package com.example.back.mapper;

import com.example.back.model.Company;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface CompanyMapper {
    @Select("SELECT * FROM companies WHERE user_id = #{userId}")
    Optional<Company> findByUserId(Long userId);
    
    @Insert("INSERT INTO companies(user_id, name, catch_phrase, bs) " +
            "VALUES (#{userId}, #{name}, #{catchPhrase}, #{bs})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Company company);
    
    @Update("UPDATE companies SET name = #{name}, catch_phrase = #{catchPhrase}, " +
            "bs = #{bs} WHERE user_id = #{userId}")
    int update(Company company);
    
    @Delete("DELETE FROM companies WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);
} 