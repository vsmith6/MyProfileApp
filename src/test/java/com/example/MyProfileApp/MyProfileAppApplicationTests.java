package com.example.MyProfileApp;

import com.amazonaws.services.s3.AmazonS3;
import com.example.MyProfileApp.entity.FileEntity;
import com.example.MyProfileApp.repository.FileEntityRepository;
import com.example.MyProfileApp.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") // Activates MockS3Config
public class MyProfileAppApplicationTests {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileEntityRepository repository;

    @Autowired
    private AmazonS3 amazonS3;

    @Test
    public void testUploadFileAndSaveMetadata() throws Exception {
        // Create a temporary file
        File tempFile = File.createTempFile("test-upload-", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Hello, this is a test file.");
        }

        String title = "Test File";
        String description = "Uploaded during unit test";

        // Upload file
        FileEntity savedEntity = fileService.uploadFile(tempFile, title, description);

        // Verify metadata was saved
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getTitle()).isEqualTo(title);
        assertThat(savedEntity.getDescription()).isEqualTo(description);
        assertThat(savedEntity.getS3Key()).isEqualTo(tempFile.getName());
        assertThat(savedEntity.getUploadedAt()).isBeforeOrEqualTo(LocalDateTime.now());

        // Verify file exists in mocked S3
        boolean exists = amazonS3.doesObjectExist("myprofileapp-dev", tempFile.getName());
        assertThat(exists).isTrue();

        // Clean up
        tempFile.delete();
    }
}