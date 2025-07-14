package com.mercurius.accessing_data_neo4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class AccessingDataNeo4jApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessingDataNeo4jApplication.class, args);
	}
}
