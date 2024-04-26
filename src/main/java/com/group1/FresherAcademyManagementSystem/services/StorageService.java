package com.group1.FresherAcademyManagementSystem.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface StorageService{
    String uploadFile(MultipartFile file);
    byte[] downloadFile(String fileName);
    String deleteFile(String fileName);

}
