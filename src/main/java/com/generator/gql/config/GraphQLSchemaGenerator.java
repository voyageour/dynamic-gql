package com.generator.gql.config;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import org.springframework.context.annotation.Configuration;

import java.io.StringReader;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Configuration
public class GraphQLSchemaGenerator {

    public GraphQLSchema generateSchema(String typeDefinitions) {
        var schemaParser = new SchemaParser();
        var typeRegistry = schemaParser.parse(new StringReader(typeDefinitions));

        var runtimeWiring = buildRuntimeWiring();
        var schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query").dataFetcher("hello", env -> "Hello, World!"))
                .build();
    }
}

