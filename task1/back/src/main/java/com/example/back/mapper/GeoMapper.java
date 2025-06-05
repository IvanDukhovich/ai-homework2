package com.example.back.mapper;

import com.example.back.model.Geo;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface GeoMapper {
    @Select("SELECT g.* FROM geo g " +
            "JOIN addresses a ON g.address_id = a.id " +
            "WHERE a.user_id = #{userId}")
    Optional<Geo> findByUserId(Long userId);
    
    @Insert("INSERT INTO geo(address_id, lat, lng) " +
            "VALUES (#{addressId}, #{lat}, #{lng})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Geo geo);
    
    @Update("UPDATE geo SET lat = #{lat}, lng = #{lng} " +
            "WHERE address_id = #{addressId}")
    int update(Geo geo);
    
    @Delete("DELETE FROM geo WHERE address_id = #{addressId}")
    int deleteByAddressId(Long addressId);
} 