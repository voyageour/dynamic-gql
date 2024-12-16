package com.generator.gql;

import com.adobe.testing.s3mock.store.KmsKeyStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;

@SpringBootApplication
public class DynamicGqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicGqlApplication.class, args);
	}

}
