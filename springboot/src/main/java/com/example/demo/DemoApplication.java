package com.example.demo;

import com.example.test.Argument;
import com.example.test.TestCase;
import com.example.util.ApplicationContextUtil;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.example")
@RestController
public class DemoApplication {

	private final static Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);
	private final ApplicationContextUtil applicationContextUtil;

	@Autowired
	public DemoApplication(ApplicationContextUtil applicationContextUtil) {
		this.applicationContextUtil = applicationContextUtil;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public static final String SUCCESS = "SUCCESS";
	@GetMapping("/check")
	public String checkRuntime(@RequestParam(value = "jdk", required = true) String jdkVersion) {
		Argument argument = new Argument();
		argument.setExpectedJdkVersion(jdkVersion);
		Collection<TestCase> testCases = applicationContextUtil.getBeans(TestCase.class);
		for (TestCase testCase : testCases) {
			LOGGER.info("Testing case: " + testCase.getClass().getName());
			testCase.test(argument);
			LOGGER.info("Testing case: " + testCase.getClass().getName() + " passed");
		}
		return SUCCESS;
	}

	@GetMapping("/")
	public String hello() {
		return "Sample Spring boot app.";
	}

}
