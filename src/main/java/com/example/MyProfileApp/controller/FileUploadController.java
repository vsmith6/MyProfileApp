package com.example.MyProfileApp.controller;


import com.example.MyProfileApp.DTO.S3FileUploadDTO;
import com.example.MyProfileApp.service.FileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@Tag(name = "File Upload", description = "Upload files to S3 and save metadata")

@CrossOrigin(origins = "*") // ⚠️ Adjust this for production
public class FileUploadController {

    private final FileService fileService;

    public FileUploadController(FileService fileService) {
        this.fileService = fileService;
    }
    @Operation(summary = "Upload a file", description = "Uploads a file to S3 and stores metadata in MySQL")

    @PostMapping
    public void upload(@ModelAttribute S3FileUploadDTO uploadDTO) throws IOException {
        MultipartFile multipartFile = uploadDTO.getFile();
        File file = convertToFile(multipartFile);

        fileService.uploadFile(file, uploadDTO.getTitle(), uploadDTO.getDescription());

        file.delete(); // Clean up temp file
    }

    private File convertToFile(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("upload-", multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }
}