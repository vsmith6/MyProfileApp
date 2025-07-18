package com.example.MyProfileApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;



@Configuration
public class AwsConfig {

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Bean
    public AmazonS3 amazonS3() {

            BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);

            return AmazonS3ClientBuilder.standard()
                    .withRegion("us-east-1") // or your actual region
                    .withCredentials(new AWSStaticCredentialsProvider(creds))
                    .build();

    }
}