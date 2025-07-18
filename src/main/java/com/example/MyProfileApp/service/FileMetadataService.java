package com.example.MyProfileApp.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.MyProfileApp.entity.FileMetadata;
import com.example.MyProfileApp.repository.FileMetadataRepository;

@Service
public class FileMetadataService {

    private final FileMetadataRepository repo;

    public FileMetadataService(FileMetadataRepository repo) {
        this.repo = repo;
    }

    public void saveMetadata(String s3Key, String title, String description, long size) {
        FileMetadata meta = new FileMetadata();
        meta.setS3Key(s3Key);
        meta.setTitle(title);
        meta.setDescription(description);
        meta.setSize(size); // ✅ Fix: set the size field
        meta.setUploadedAt(LocalDateTime.now());
        repo.save(meta);
        System.out.println("✅ Metadata saved to MySQL.");
    }
    

    public FileMetadata getById(Long id) {
        Optional<FileMetadata> result = repo.findById(id);
        return result.orElse(null);
    }

    public Iterable<FileMetadata> getAll() {
        return repo.findAll();
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
        System.out.println("🗑️ Metadata with ID " + id + " deleted.");
    }

}