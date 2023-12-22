package com.example.test.cases;

import com.example.test.Argument;
import com.example.test.TestCase;
import java.util.Properties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class JavaVersionTest implements TestCase {

    @Override
    public void test(Argument argument) {
        Properties props = System.getProperties();
        String actualJdkVersion = props.getProperty("java.version");
        if (argument.getExpectedJdkVersion().equals("8")) {
            Assert.isTrue(actualJdkVersion.startsWith("1.8"), "Actual version: " + actualJdkVersion
                + " not same as the expected version: " + argument.getExpectedJdkVersion());
        } else if (!actualJdkVersion.startsWith(argument.getExpectedJdkVersion())) {
            Assert.isTrue(actualJdkVersion.startsWith(argument.getExpectedJdkVersion()), "Actual version: " + actualJdkVersion
                + " not same as the expected version: " + argument.getExpectedJdkVersion());
        }
    }
}
