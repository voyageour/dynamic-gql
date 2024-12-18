package com.generator.gql.controller;

import com.generator.gql.config.GraphQLSchemaGenerator;
import com.generator.gql.s3.S3Service;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/graphql")
public class GraphQLController {
    private final S3Service s3Service;

    private final GraphQLSchemaGenerator schemaGenerator;
    private final String bucketName;
    private final String schemaKey;

    private Boolean isLoaded = Boolean.FALSE;

    @Value("classpath:sample.graphqls")
    Resource resourceFile;

    public GraphQLController(S3Service s3Service, GraphQLSchemaGenerator schemaGenerator,
                             @Value("${aws.s3.bucket-name}") String bucketName,
                             @Value("${aws.s3.schema-key}") String schemaKey) {
        this.s3Service = s3Service;
        this.schemaGenerator = schemaGenerator;
        this.bucketName = bucketName;
        this.schemaKey = schemaKey;
    }

    @PostMapping
    public Object executeGraphQLQuery(@RequestBody String query) {
        if(!isLoaded) {
            setup();
            isLoaded = Boolean.TRUE;
        }
        String schemaContent = s3Service.fetchSchema(bucketName, schemaKey);
        GraphQLSchema schema = schemaGenerator.generateSchema(schemaContent);

        var graphQL = GraphQL.newGraphQL(schema).build();
        return graphQL.execute(query).toSpecification();
    }

    public void setup() {
        try {
            s3Service.uploadSchema(bucketName,schemaKey,resourceFile.getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

