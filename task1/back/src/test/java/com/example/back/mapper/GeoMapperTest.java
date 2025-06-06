package com.example.back.mapper;

import com.example.back.model.Geo;
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
public class GeoMapperTest {
    
    @Mock
    private GeoMapper geoMapper;
    
    private Geo testGeo;
    
    @BeforeEach
    public void setup() {
        testGeo = new Geo();
        testGeo.setId(1L);
        testGeo.setAddressId(1L);
        testGeo.setLat("40.7128");
        testGeo.setLng("-74.0060");
    }
    
    @Test
    public void testFindByUserId() {
        // Setup
        when(geoMapper.findByUserId(1L)).thenReturn(Optional.of(testGeo));
        
        // Execute
        Optional<Geo> result = geoMapper.findByUserId(1L);
        
        // Verify
        assertThat(result).isPresent();
        assertThat(result.get().getLat()).isEqualTo("40.7128");
        assertThat(result.get().getLng()).isEqualTo("-74.0060");
    }
    
    @Test
    public void testFindByUserId_NotFound() {
        // Setup
        when(geoMapper.findByUserId(99L)).thenReturn(Optional.empty());
        
        // Execute
        Optional<Geo> result = geoMapper.findByUserId(99L);
        
        // Verify
        assertThat(result).isEmpty();
    }
    
    @Test
    public void testInsert() {
        // Setup
        when(geoMapper.insert(any(Geo.class))).thenAnswer(invocation -> {
            Geo geo = invocation.getArgument(0);
            geo.setId(2L);
            return 1;
        });
        
        Geo newGeo = new Geo();
        newGeo.setAddressId(2L);
        newGeo.setLat("34.0522");
        newGeo.setLng("-118.2437");
        
        // Execute
        int result = geoMapper.insert(newGeo);
        
        // Verify
        assertThat(result).isEqualTo(1);
        assertThat(newGeo.getId()).isEqualTo(2L);
    }
    
    @Test
    public void testUpdate() {
        // Setup
        when(geoMapper.update(any(Geo.class))).thenReturn(1);
        
        testGeo.setLat("51.5074");
        testGeo.setLng("-0.1278");
        
        // Execute
        int result = geoMapper.update(testGeo);
        
        // Verify
        assertThat(result).isEqualTo(1);
    }
    
    @Test
    public void testDelete() {
        // Setup
        when(geoMapper.deleteByAddressId(anyLong())).thenReturn(1);
        
        // Execute
        int result = geoMapper.deleteByAddressId(1L);
        
        // Verify
        assertThat(result).isEqualTo(1);
    }
} 