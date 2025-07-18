package com.example.MyProfileApp.controller;



import com.example.MyProfileApp.DTO.FileMetadataDTO;
import com.example.MyProfileApp.entity.FileMetadata;
import com.example.MyProfileApp.service.FileMetadataService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
public class FileMetadataController {

    private final FileMetadataService metadataService;

    public FileMetadataController(FileMetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @PostMapping
    public void uploadMetadata(@RequestBody FileMetadataDTO dto) {
        metadataService.saveMetadata(dto.getS3Key(), dto.getTitle(), dto.getDescription(), dto.getSize());
    }

    @GetMapping
    public Iterable<FileMetadata> getAll() {
        return metadataService.getAll();
    }

    @GetMapping("/{id}")
    public FileMetadata getById(@PathVariable Long id) {
        return metadataService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        metadataService.deleteById(id);
    }
}