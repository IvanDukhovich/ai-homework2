package com.example.back.mapper;

import com.example.back.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressMapperTest {
    
    @Mock
    private AddressMapper addressMapper;
    
    private Address testAddress;
    
    @BeforeEach
    public void setup() {
        testAddress = new Address();
        testAddress.setId(1L);
        testAddress.setUserId(1L);
        testAddress.setStreet("123 Test St");
        testAddress.setSuite("Apt 4B");
        testAddress.setCity("Testville");
        testAddress.setZipcode("12345");
    }
    
    @Test
    public void testFindByUserId() {
        // Setup
        when(addressMapper.findByUserId(1L)).thenReturn(Optional.of(testAddress));
        
        // Execute
        Optional<Address> result = addressMapper.findByUserId(1L);
        
        // Verify
        assertThat(result).isPresent();
        assertThat(result.get().getStreet()).isEqualTo("123 Test St");
        assertThat(result.get().getCity()).isEqualTo("Testville");
    }
    
    @Test
    public void testInsert() {
        // Setup
        when(addressMapper.insert(any(Address.class))).thenAnswer(invocation -> {
            Address address = invocation.getArgument(0);
            address.setId(2L);
            return 1;
        });
        
        Address newAddress = new Address();
        newAddress.setUserId(2L);
        newAddress.setStreet("456 New St");
        newAddress.setCity("Newville");
        
        // Execute
        int result = addressMapper.insert(newAddress);
        
        // Verify
        assertThat(result).isEqualTo(1);
        assertThat(newAddress.getId()).isEqualTo(2L);
    }
    
    @Test
    public void testUpdate() {
        // Setup
        when(addressMapper.update(any(Address.class))).thenReturn(1);
        
        testAddress.setStreet("Updated Street");
        
        // Execute
        int result = addressMapper.update(testAddress);
        
        // Verify
        assertThat(result).isEqualTo(1);
    }
    
    @Test
    public void testDelete() {
        // Setup
        when(addressMapper.deleteByUserId(anyLong())).thenReturn(1);
        
        // Execute
        int result = addressMapper.deleteByUserId(1L);
        
        // Verify
        assertThat(result).isEqualTo(1);
    }
} 