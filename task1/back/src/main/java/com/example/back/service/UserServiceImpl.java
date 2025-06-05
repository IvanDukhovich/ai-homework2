package com.example.back.service;

import com.example.back.dto.request.AddressRequest;
import com.example.back.dto.request.CompanyRequest;
import com.example.back.dto.request.GeoRequest;
import com.example.back.dto.request.UserRequest;
import com.example.back.dto.response.UserResponse;
import com.example.back.mapper.AddressMapper;
import com.example.back.mapper.CompanyMapper;
import com.example.back.mapper.GeoMapper;
import com.example.back.mapper.UserMapper;
import com.example.back.model.Address;
import com.example.back.model.Company;
import com.example.back.model.Geo;
import com.example.back.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private GeoMapper geoMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userMapper.findAll();
        return users.stream()
                .map(this::enrichUserWithRelations)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponse> findById(Long id) {
        return userMapper.findById(id)
                .map(this::enrichUserWithRelations)
                .map(this::convertToResponse);
    }

    @Override
    @Transactional
    public UserResponse create(UserRequest userRequest) {
        User user = new User();
        BeanUtils.copyProperties(userRequest, user);
        
        userMapper.insert(user);
        
        if (userRequest.getAddress() != null) {
            createAddress(user.getId(), userRequest.getAddress());
        }
        
        if (userRequest.getCompany() != null) {
            createCompany(user.getId(), userRequest.getCompany());
        }
        
        return findById(user.getId()).orElseThrow();
    }

    @Override
    @Transactional
    public Optional<UserResponse> update(Long id, UserRequest userRequest) {
        return userMapper.findById(id).map(existingUser -> {
            BeanUtils.copyProperties(userRequest, existingUser);
            existingUser.setId(id);
            
            userMapper.update(existingUser);
            
            if (userRequest.getAddress() != null) {
                updateAddress(id, userRequest.getAddress());
            }
            
            if (userRequest.getCompany() != null) {
                updateCompany(id, userRequest.getCompany());
            }
            
            return findById(id).orElseThrow();
        });
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return userMapper.findById(id).map(user -> {
            addressMapper.findByUserId(id).ifPresent(address -> {
                geoMapper.deleteByAddressId(address.getId());
                addressMapper.deleteByUserId(id);
            });
            
            companyMapper.deleteByUserId(id);
            userMapper.delete(id);
            
            return true;
        }).orElse(false);
    }

    private User enrichUserWithRelations(User user) {
        addressMapper.findByUserId(user.getId()).ifPresent(address -> {
            geoMapper.findByUserId(user.getId()).ifPresent(address::setGeo);
            user.setAddress(address);
        });
        
        companyMapper.findByUserId(user.getId()).ifPresent(user::setCompany);
        
        return user;
    }

    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    private void createAddress(Long userId, AddressRequest addressRequest) {
        Address address = new Address();
        BeanUtils.copyProperties(addressRequest, address);
        address.setUserId(userId);
        
        addressMapper.insert(address);
        
        if (addressRequest.getGeo() != null) {
            Geo geo = new Geo();
            BeanUtils.copyProperties(addressRequest.getGeo(), geo);
            geo.setAddressId(address.getId());
            
            geoMapper.insert(geo);
        }
    }

    private void updateAddress(Long userId, AddressRequest addressRequest) {
        addressMapper.findByUserId(userId).ifPresentOrElse(
            existingAddress -> {
                BeanUtils.copyProperties(addressRequest, existingAddress);
                addressMapper.update(existingAddress);
                
                if (addressRequest.getGeo() != null) {
                    updateGeo(existingAddress.getId(), addressRequest.getGeo());
                }
            },
            () -> createAddress(userId, addressRequest)
        );
    }

    private void updateGeo(Long addressId, GeoRequest geoRequest) {
        Geo geo = new Geo();
        BeanUtils.copyProperties(geoRequest, geo);
        geo.setAddressId(addressId);
        
        // Try to update, if not exists then insert
        if (geoMapper.update(geo) == 0) {
            geoMapper.insert(geo);
        }
    }

    private void createCompany(Long userId, CompanyRequest companyRequest) {
        Company company = new Company();
        BeanUtils.copyProperties(companyRequest, company);
        company.setUserId(userId);
        
        companyMapper.insert(company);
    }

    private void updateCompany(Long userId, CompanyRequest companyRequest) {
        companyMapper.findByUserId(userId).ifPresentOrElse(
            existingCompany -> {
                BeanUtils.copyProperties(companyRequest, existingCompany);
                companyMapper.update(existingCompany);
            },
            () -> createCompany(userId, companyRequest)
        );
    }
} 