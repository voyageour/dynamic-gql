package com.generator.gql.config;

import com.adobe.testing.s3mock.S3MockApplication;
import com.adobe.testing.s3mock.S3MockConfiguration;
import com.adobe.testing.s3mock.store.KmsKeyStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Configuration
public class S3MockConfig {

    @Bean(destroyMethod = "stop")
    public S3MockApplication s3MockStarter() {
        S3MockApplication mock = S3MockApplication.start(new HashMap<>(Map.of(S3MockApplication.PROP_SILENT, false)));
        return mock;
    }

    @Bean
    @Primary
    public S3Client s3Client() {
        URI endpoint = URI.create("http://localhost:9090");
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("dummy-access-key", "dummy-secret-key")
                ))
                .region(Region.US_EAST_1)
                .endpointOverride(endpoint)
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    @Bean
    public KmsKeyStore createKms() {
        HashSet<String> kmsKeys = new HashSet<String>();
        kmsKeys.add("arn:aws:kms:us-east-1:1234567890:key/valid-test-key-id");
        return new KmsKeyStore(kmsKeys);
    }

    @Bean
    public S3MockConfiguration s3MockConfiguration() {
        return new S3MockConfiguration();
    }

}

