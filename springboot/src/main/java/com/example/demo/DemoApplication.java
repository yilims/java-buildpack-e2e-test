package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.example")
@RestController
public class DemoApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);
	private final ApplicationContextUtil applicationContextUtil;

	@Autowired
	public DemoApplication(ApplicationContextUtil applicationContextUtil) {
		this.applicationContextUtil = applicationContextUtil;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
	@GetMapping("/check")
	public String checkRuntime(@RequestParam(value = "jdk", required = true) String jdkVersion) {
		Argument argument = new Argument();
		argument.setExpectedJdkVersion(jdkVersion);
		Collection<TestCase> testCases = applicationContextUtil.getBeans(TestCase.class);
		for (TestCase testCase : testCases) {
			try {
				testCase.test(argument);
			} catch (Exception e) {
				LOGGER.error("error occurred in test case: " + testCase.getClass().getName(), e);
				return FAILED;
			}
		}
		return SUCCESS;
	}

}
