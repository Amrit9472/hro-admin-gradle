package com.eos.admin.controller;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.eos.admin.dto.EmployeeDto;
import com.eos.admin.dto.ProfileScreaningResponseDto;
import com.eos.admin.dto.StatusRequestDTO;
import com.eos.admin.enums.RemarksType;
import com.eos.admin.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private String path;

    @BeforeEach
    public void setup() {
        this.path = "/mock/upload/dir"; // or any path you want
        ReflectionTestUtils.setField(employeeController, "path", this.path);
    }
    @Test
    public void testCreateEmployee_Success() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L); // Set other properties as needed
        employeeDto.setAadhaarNumber("966555555555");
        employeeDto.setEmail("test@gmail.com");
        employeeDto.setFullName("Test junit");
        employeeDto.setMobileNo(12345698790l);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dob = sdf.parse("12/09/2007");
        employeeDto.setDob(dob); 
       

        MockMultipartFile image1 = new MockMultipartFile("image",  "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("image", "test2.jpg", "image/jpeg", "test image content".getBytes());
        

        List<MultipartFile> images = Arrays.asList(image1, image2);
        when(employeeService.createEmployee(any(EmployeeDto.class), anyList(), eq(path))).thenReturn(employeeDto);

        ResponseEntity<EmployeeDto> response = employeeController.createEmployee( employeeDto,images);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getFullName()).isEqualTo("Test junit");
       
        verify(employeeService).createEmployee(eq(employeeDto), eq(images), eq(path));
    }


   @Test
   public void testCreateEmployee_badRequest() throws IOException {
	   ResponseEntity<EmployeeDto> response = employeeController.createEmployee(null, null);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
       assertThat(response.getBody()).isNull();   
       verify(employeeService ,never()).createEmployee(any(),any(),any());
   }
   
 
   
   @Test
   public void testGetListOfEmployeesProfilScreaning_success() {
	   String location = "Mumbai"; 
	   List<ProfileScreaningResponseDto> mockList = new ArrayList<>();
	   mockList.add(new ProfileScreaningResponseDto( 101l,"Test 1","testEmail@mail.com",12345698745l, "Software Engineer","123 Main Street","Male",  
               new Date(),
               Arrays.asList("English", "Hindi")));
	   mockList.add(new ProfileScreaningResponseDto(102L,"Test User 2","test2@mail.com",9876543210L,"QA Engineer","456 Park Avenue","Female",
               new Date(),
               Arrays.asList("English", "Spanish")
       ));

	   when(employeeService.getListOfEmployeesOnProfileScreanig(location)).thenReturn(mockList);
	   ResponseEntity<List<ProfileScreaningResponseDto>> response = employeeController.getListOfEmployeesProfileScreaning(location);
	   assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	   assertThat(response.getBody()).isEqualTo(mockList);
	   
	   
   }
   
   @Test
   public void testGetListOfEmployeesProfilScreaningWithEmptyList() {
	   String location = "Mumbai"; 
	   List<ProfileScreaningResponseDto> mockList = new ArrayList<>();
	   when(employeeService.getListOfEmployeesOnProfileScreanig(location)).thenReturn(mockList);
	   ResponseEntity<List<ProfileScreaningResponseDto>> response = employeeController.getListOfEmployeesProfileScreaning(location);
	   assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	   assertThat(response.getBody()).isEqualTo(mockList);
	   
	   
   }
   
   @Test
   public void testGetListOfEmployeesProfileScreaning_Returns500OnException() {
	   String location = "Thane";
	   when(employeeService.getListOfEmployeesOnProfileScreanig(location)).thenThrow(new RuntimeException("DB error"));
	   ResponseEntity<List<ProfileScreaningResponseDto>> response = employeeController.getListOfEmployeesProfileScreaning(location);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
       assertThat(response.getBody()).isNull();
   }
   
   
   @Test
   public void testUpdateResponseOnProfileScreaning_success() throws Exception {
	   Long employeeId = 101l;
	   
	   StatusRequestDTO  mockData = new StatusRequestDTO(employeeId,"newStatus","remarks","responseSubmitbyName","processName","jobProfile","clientRound");
	   doNothing().when(employeeService).updateRemarks(anyLong(), any(StatusRequestDTO.class), eq(RemarksType.PROFILE));   
   
	   ObjectMapper objectMapper = new ObjectMapper();
	    String requestJson = objectMapper.writeValueAsString(mockData);

	    mockMvc.perform(put("/hrResponseSubmitionOnProfilePage/{id}", employeeId)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(requestJson))
	        .andExpect(status().isOk())
	        .andExpect(content().string("Profile remarks and status updated successfully."));
   }
    
}
