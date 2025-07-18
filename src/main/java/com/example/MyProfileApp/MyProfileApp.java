package com.example.MyProfileApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



import com.example.MyProfileApp.service.FileMetadataService;

@SpringBootApplication
public class MyProfileApp{

//    private final FileMetadataService metaDataService;
//
//    public MyProfileApp(FileMetadataService metaDataService) {
//        this.metaDataService = metaDataService;
//    }

    public static void main(String[] args) {
        SpringApplication.run(MyProfileApp.class, args);
    }

//    @Override
//    public void run(String... args) {
//    	metaDataService.saveMetadata("test-s3-key", "Test File", "Testing local save");
//    }
}
