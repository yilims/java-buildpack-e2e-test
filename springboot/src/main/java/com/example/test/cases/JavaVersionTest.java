package com.example.test.cases;

import com.example.test.Argument;
import com.example.test.TestCase;
import java.util.Properties;
import org.springframework.stereotype.Component;

@Component
public class JavaVersionTest implements TestCase {

    @Override
    public void test(Argument argument) {
        Properties props = System.getProperties();
        String actualJdkVersion = props.getProperty("java.version");
        if (!actualJdkVersion.startsWith(argument.getExpectedJdkVersion())) {
            throw new RuntimeException("Actual version: " + actualJdkVersion + " not same as the expected version: " + argument.getExpectedJdkVersion());
        }
    }
}
