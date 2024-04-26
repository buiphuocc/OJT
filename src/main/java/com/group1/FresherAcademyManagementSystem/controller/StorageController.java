package com.group1.FresherAcademyManagementSystem.controller;


import com.group1.FresherAcademyManagementSystem.exceptions.CustomSuccessHandler;
import com.group1.FresherAcademyManagementSystem.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file")
public class StorageController {
    @Autowired
    private StorageService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        return CustomSuccessHandler.responseBuilder("Upload successful !", HttpStatus.OK, service.uploadFile(file));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileName") String fileName) {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok().contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<Object> deleteFile(@PathVariable("fileName") String fileName) {
        return CustomSuccessHandler.responseBuilder("Delete successful !", HttpStatus.OK, service.deleteFile(fileName));
    }
}


