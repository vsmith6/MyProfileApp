package com.example.MyProfileApp.service;



import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.MyProfileApp.entity.FileEntity;
import com.example.MyProfileApp.repository.FileEntityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;


@Service
public class FileService {

    private final AmazonS3 s3Client;
    private final FileEntityRepository repository;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public FileService(AmazonS3 s3Client, FileEntityRepository repository) {
        this.s3Client = s3Client;
        this.repository = repository;
    }

    public FileEntity uploadFile(File file, String title, String description) {
        String s3Key = file.getName();
        s3Client.putObject(new PutObjectRequest(bucketName, s3Key, file));

        FileEntity entity = new FileEntity();
        entity.setS3Key(s3Key);
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setUploadedAt(LocalDateTime.now());

        return repository.save(entity);
    }

    public FileEntity getFiledata(String s3Key) {
        return repository.findByS3Key(s3Key)
                .orElseThrow(() -> new RuntimeException("s3 File data not found"));
    }
}