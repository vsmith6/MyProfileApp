package com.example.MyProfileApp.repository;

import com.example.MyProfileApp.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByS3Key(String s3Key);
}