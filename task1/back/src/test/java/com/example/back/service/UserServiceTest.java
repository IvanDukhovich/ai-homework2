package com.example.back.service;

import com.example.back.dto.request.UserRequest;
import com.example.back.dto.response.UserResponse;
import com.example.back.mapper.AddressMapper;
import com.example.back.mapper.CompanyMapper;
import com.example.back.mapper.GeoMapper;
import com.example.back.mapper.UserMapper;
import com.example.back.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;
    
    @Mock
    private AddressMapper addressMapper;
    
    @Mock
    private CompanyMapper companyMapper;
    
    @Mock
    private GeoMapper geoMapper;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    private User testUser;
    private UserRequest userRequest;
    private UserResponse userResponse;
    
    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("123-456-7890");
        testUser.setWebsite("test.com");
        
        userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setUsername("testuser");
        userRequest.setEmail("test@example.com");
        userRequest.setPhone("123-456-7890");
        userRequest.setWebsite("test.com");
        
        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("Test User");
        userResponse.setUsername("testuser");
        userResponse.setEmail("test@example.com");
        userResponse.setPhone("123-456-7890");
        userResponse.setWebsite("test.com");
    }
    
    @Test
    public void testFindAll() {
        when(userMapper.findAll()).thenReturn(Collections.singletonList(testUser));
        when(addressMapper.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(companyMapper.findByUserId(anyLong())).thenReturn(Optional.empty());
        
        List<UserResponse> users = userService.findAll();
        
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).isEqualTo(testUser.getName());
        assertThat(users.get(0).getEmail()).isEqualTo(testUser.getEmail());
        
        verify(userMapper).findAll();
    }
    
    @Test
    public void testFindById() {
        when(userMapper.findById(1L)).thenReturn(Optional.of(testUser));
        when(addressMapper.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(companyMapper.findByUserId(anyLong())).thenReturn(Optional.empty());
        
        Optional<UserResponse> userResponse = userService.findById(1L);
        
        assertThat(userResponse).isPresent();
        assertThat(userResponse.get().getId()).isEqualTo(testUser.getId());
        assertThat(userResponse.get().getName()).isEqualTo(testUser.getName());
        assertThat(userResponse.get().getEmail()).isEqualTo(testUser.getEmail());
        
        verify(userMapper).findById(1L);
    }
    
    @Test
    public void testFindByIdNotFound() {
        when(userMapper.findById(99L)).thenReturn(Optional.empty());
        
        Optional<UserResponse> userResponse = userService.findById(99L);
        
        assertThat(userResponse).isEmpty();
        
        verify(userMapper).findById(99L);
    }
    
    @Test
    public void testCreate() {
        // Setup
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        }).when(userMapper).insert(any(User.class));
        
        when(userMapper.findById(1L)).thenReturn(Optional.of(testUser));
        when(addressMapper.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(companyMapper.findByUserId(anyLong())).thenReturn(Optional.empty());
        
        // Execute
        UserResponse result = userService.create(userRequest);
        
        // Verify
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(testUser.getName());
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
        
        verify(userMapper).insert(any(User.class));
        verify(userMapper).findById(1L);
    }
    
    @Test
    public void testUpdate() {
        // Setup
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Updated User");
        updatedUser.setUsername("testuser");
        updatedUser.setEmail("test@example.com");
        
        // First call returns the original user, second call returns the updated user
        when(userMapper.findById(1L))
            .thenReturn(Optional.of(testUser))
            .thenReturn(Optional.of(updatedUser));
            
        when(userMapper.update(any(User.class))).thenReturn(1);
        when(addressMapper.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(companyMapper.findByUserId(anyLong())).thenReturn(Optional.empty());
        
        // Execute
        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Updated User");
        updateRequest.setUsername("testuser");
        updateRequest.setEmail("test@example.com");
        
        Optional<UserResponse> result = userService.update(1L, updateRequest);
        
        // Verify
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Updated User");
        
        verify(userMapper, times(2)).findById(1L);
        verify(userMapper).update(any(User.class));
    }
    
    @Test
    public void testUpdateNotFound() {
        when(userMapper.findById(99L)).thenReturn(Optional.empty());
        
        Optional<UserResponse> result = userService.update(99L, userRequest);
        
        assertThat(result).isEmpty();
        
        verify(userMapper).findById(99L);
        verify(userMapper, never()).update(any(User.class));
    }
    
    @Test
    public void testDelete() {
        when(userMapper.findById(1L)).thenReturn(Optional.of(testUser));
        when(addressMapper.findByUserId(1L)).thenReturn(Optional.empty());
        when(userMapper.delete(1L)).thenReturn(1);
        
        boolean result = userService.delete(1L);
        
        assertThat(result).isTrue();
        
        verify(userMapper).findById(1L);
        verify(addressMapper).findByUserId(1L);
        verify(userMapper).delete(1L);
    }
    
    @Test
    public void testDeleteNotFound() {
        when(userMapper.findById(99L)).thenReturn(Optional.empty());
        
        boolean result = userService.delete(99L);
        
        assertThat(result).isFalse();
        
        verify(userMapper).findById(99L);
        verify(userMapper, never()).delete(anyLong());
    }
} 