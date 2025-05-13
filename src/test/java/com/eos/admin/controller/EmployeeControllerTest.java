package com.eos.admin.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.eos.admin.dto.EmployeeDto;
import com.eos.admin.service.EmployeeService;
import com.eos.admin.serviceImpl.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeServiceImpl employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Value("${project.file.upload-dir}")
    private String path;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void testCreateEmployee_Success() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L); // Set other properties as needed
        employeeDto.setAadhaarNumber("966555555555");
        employeeDto.setEmail("test@gmail.com");

        String employeeJson = new ObjectMapper().writeValueAsString(employeeDto);

        MockMultipartFile employeeFile = new MockMultipartFile("employee", "", "application/json", employeeJson.getBytes());
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        List<MultipartFile> images = Collections.singletonList(image);

        when(employeeService.createEmployee(any(EmployeeDto.class), anyList(), anyString())).thenReturn(employeeDto);

        mockMvc.perform(multipart("/api/employees/createEmployee")
                .file(employeeFile) // Send employee as a multipart file
        		.file(image)
                .param("employee", "{\"id\":1}") // Assuming JSON string for employeeDto
                .contentType("multipart/form-data"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
        
        verify(employeeService, times(1)).createEmployee(any(EmployeeDto.class), anyList(), anyString());
    }
//    @Test
//    public void testCreateEmployee_Success() throws Exception {
//        // Create and initialize the EmployeeDto
//        EmployeeDto employeeDto = new EmployeeDto();
//        employeeDto.setId(1L); // Set other properties as needed
//
//        // Create a JSON representation of the EmployeeDto
//        String employeeJson = new ObjectMapper().writeValueAsString(employeeDto);
//        
//        MockMultipartFile employeeFile = new MockMultipartFile("employee", "", "application/json", employeeJson.getBytes());
//        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
//        List<MultipartFile> images = Collections.singletonList(image);
//
//        // Mock the service to return the initialized EmployeeDto
//        when(employeeService.createEmployee(any(EmployeeDto.class), anyList(), anyString())).thenReturn(employeeDto);
//
//        mockMvc.perform(multipart("/api/employees/createEmployee")
//                .file(employeeFile) // Send employee as a multipart file
//                .file(image) // Send the image file
//                .contentType("multipart/form-data"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1));
//        
//        verify(employeeService, times(1)).createEmployee(any(EmployeeDto.class), anyList(), anyString());
//    }

    @Test
    public void testCreateEmployee_BadRequest() throws Exception {
        mockMvc.perform(multipart("/api/employees/createEmployee")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).createEmployee(any(), anyList(), anyString());
    }

    @Test
    public void testCreateEmployee_InternalServerError() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        List<MultipartFile> images = Collections.singletonList(image);

        when(employeeService.createEmployee(any(EmployeeDto.class), anyList(), anyString())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(multipart("/api/employees/createEmployee")
                .file(image)
                .param("employee", "{\"id\":1}") // Assuming JSON string for employeeDto
                .contentType("multipart/form-data"))
                .andExpect(status().isInternalServerError());

        verify(employeeService, times(1)).createEmployee(any(EmployeeDto.class), anyList(), anyString());
    }
}
