package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Properties;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
	@GetMapping("/check")
	public String checkRuntime(@RequestParam(value = "jdk", required = true) String jdkVersion) {
		Properties props = System.getProperties();
		if(jdkVersion!=null) {
			if (!props.getProperty("java.version").startsWith(jdkVersion)) {
				return FAILED;
			}
		}
		return SUCCESS;
	}

}
