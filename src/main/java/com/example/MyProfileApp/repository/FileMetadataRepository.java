package com.example.MyProfileApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MyProfileApp.entity.FileMetadata;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
}