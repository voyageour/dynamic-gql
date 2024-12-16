package com.generator.gql.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

@Service
public class S3Service {
    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadSchema(String bucketName, String key, String schemaContent) {
        var request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        s3Client.putObject(request, Path.of(schemaContent));
    }

    public String fetchSchema(String bucketName, String key) {
        var request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseBytes<?> responseBytes = s3Client.getObjectAsBytes(request);
        return responseBytes.asUtf8String();
    }
}

