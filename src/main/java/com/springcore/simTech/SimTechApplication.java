package com.springcore.simTech;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@OpenAPIDefinition(
//		info = @Info(
//				title = "SimTech Bank",
//				description = "Server side APIs for SimTech Bank",
//				version = "v1.0",
//				contact = @Contact(
//						name = "Nwachukwu Samuel",
//						email = "nwachukwusamuel123@gmail.com"
//				),
//				license = @License(
//						name = "Springcore Africa",
//						url = "https://github.com/springcoreafrica"
//				)
//		),
//		externalDocs = @ExternalDocumentation(
//				description = "Server side APIs Documentation for SimTech Bank",
//				url = "https://github.com/springcoreafrica"
//		)
//)
public class SimTechApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimTechApplication.class, args);
	}

}
