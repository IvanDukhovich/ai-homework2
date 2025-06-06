package com.example.back.mapper;

import com.example.back.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private UserMapper userMapper;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("123-456-7890");
        testUser.setWebsite("test.com");
    }

    @Test
    public void testFindAll() {
        // Setup
        when(userMapper.findAll()).thenReturn(Collections.singletonList(testUser));

        // Execute
        List<User> users = userMapper.findAll();

        // Verify
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getName()).isEqualTo("Test User");
    }

    @Test
    public void testFindById() {
        // Setup
        when(userMapper.findById(1L)).thenReturn(Optional.of(testUser));

        // Execute
        Optional<User> user = userMapper.findById(1L);

        // Verify
        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo("Test User");
        assertThat(user.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testFindByEmail() {
        // Setup
        when(userMapper.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Execute
        Optional<User> user = userMapper.findByEmail("test@example.com");

        // Verify
        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo("Test User");
    }

    @Test
    public void testInsertUpdateAndDelete() {
        // Setup for insert
        User newUser = new User();
        newUser.setName("New User");
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        newUser.setPhone("555-555-5555");
        newUser.setWebsite("newuser.com");

        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L);
            return 1;
        });

        // Execute insert
        int insertResult = userMapper.insert(newUser);

        // Verify insert
        assertThat(insertResult).isEqualTo(1);
        assertThat(newUser.getId()).isEqualTo(2L);

        // Setup for update
        when(userMapper.update(any(User.class))).thenReturn(1);

        // Execute update
        newUser.setName("Updated User");
        int updateResult = userMapper.update(newUser);

        // Verify update
        assertThat(updateResult).isEqualTo(1);

        // Setup for delete
        when(userMapper.delete(anyLong())).thenReturn(1);

        // Execute delete
        int deleteResult = userMapper.delete(2L);

        // Verify delete
        assertThat(deleteResult).isEqualTo(1);
    }
} 