package com.example.back.mapper;

import com.example.back.model.Address;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface AddressMapper {
    @Select("SELECT * FROM addresses WHERE user_id = #{userId}")
    Optional<Address> findByUserId(Long userId);
    
    @Insert("INSERT INTO addresses(user_id, street, suite, city, zipcode) " +
            "VALUES (#{userId}, #{street}, #{suite}, #{city}, #{zipcode})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Address address);
    
    @Update("UPDATE addresses SET street = #{street}, suite = #{suite}, " +
            "city = #{city}, zipcode = #{zipcode} " +
            "WHERE user_id = #{userId}")
    int update(Address address);
    
    @Delete("DELETE FROM addresses WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);
} 