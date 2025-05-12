package com.eos.admin.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
	 List<String> uploadImage(String path , List<MultipartFile> file,String aadhaarNumber) throws IOException;
}
