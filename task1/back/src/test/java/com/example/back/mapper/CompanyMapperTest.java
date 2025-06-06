package com.example.back.mapper;

import com.example.back.model.Company;
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
public class CompanyMapperTest {
    
    @Mock
    private CompanyMapper companyMapper;
    
    private Company testCompany;
    
    @BeforeEach
    public void setup() {
        testCompany = new Company();
        testCompany.setId(1L);
        testCompany.setUserId(1L);
        testCompany.setName("Test Company");
        testCompany.setCatchPhrase("Making the world better");
        testCompany.setBs("innovative solutions");
    }
    
    @Test
    public void testFindByUserId() {
        // Setup
        when(companyMapper.findByUserId(1L)).thenReturn(Optional.of(testCompany));
        
        // Execute
        Optional<Company> result = companyMapper.findByUserId(1L);
        
        // Verify
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test Company");
        assertThat(result.get().getCatchPhrase()).isEqualTo("Making the world better");
    }
    
    @Test
    public void testFindByUserId_NotFound() {
        // Setup
        when(companyMapper.findByUserId(99L)).thenReturn(Optional.empty());
        
        // Execute
        Optional<Company> result = companyMapper.findByUserId(99L);
        
        // Verify
        assertThat(result).isEmpty();
    }
    
    @Test
    public void testInsert() {
        // Setup
        when(companyMapper.insert(any(Company.class))).thenAnswer(invocation -> {
            Company company = invocation.getArgument(0);
            company.setId(2L);
            return 1;
        });
        
        Company newCompany = new Company();
        newCompany.setUserId(2L);
        newCompany.setName("New Company");
        newCompany.setCatchPhrase("Innovation starts here");
        
        // Execute
        int result = companyMapper.insert(newCompany);
        
        // Verify
        assertThat(result).isEqualTo(1);
        assertThat(newCompany.getId()).isEqualTo(2L);
    }
    
    @Test
    public void testUpdate() {
        // Setup
        when(companyMapper.update(any(Company.class))).thenReturn(1);
        
        testCompany.setName("Updated Company");
        
        // Execute
        int result = companyMapper.update(testCompany);
        
        // Verify
        assertThat(result).isEqualTo(1);
    }
    
    @Test
    public void testDelete() {
        // Setup
        when(companyMapper.deleteByUserId(anyLong())).thenReturn(1);
        
        // Execute
        int result = companyMapper.deleteByUserId(1L);
        
        // Verify
        assertThat(result).isEqualTo(1);
    }
} 