package com.example.test.cases;

import com.example.test.Argument;
import com.example.test.TestCase;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class JvmFitTest implements TestCase {

    private static final String SizePattern = "(\\d+)([kmgtKMGT]?)";
    private static final Pattern XmxPattern = Pattern.compile(String.format("-Xmx%s", SizePattern));
    private static final Pattern MetaspacePattern = Pattern.compile(
        String.format("-XX:MaxMetaspaceSize=%s", SizePattern));
    private static final Pattern XssPattern = Pattern.compile(String.format("-Xss%s", SizePattern));
    private static final Pattern CodeCachePattern = Pattern.compile(
        String.format("-XX:ReservedCodeCacheSize=%s", SizePattern));
    private static final Pattern DirectPattern = Pattern.compile(
        String.format("-XX:MaxDirectMemorySize=%s", SizePattern));

    private static final Pattern[] PatternsToVerify = new Pattern[] {
        XmxPattern,
        XssPattern,
        MetaspacePattern,
        CodeCachePattern,
        DirectPattern
    };

    @Override
    public void test(Argument argument) {
        String opts = System.getenv("JAVA_TOOL_OPTIONS");
        if (opts == null || opts.isEmpty()) {
            throw new RuntimeException("JAVA_TOOL_OPTIONS should not be null or empty " + opts);
        }

        String[] splitOpts = opts.split(" ");
        for (Pattern p : PatternsToVerify) {
            boolean match = false;
            for (String opt : splitOpts) {
                if (p.matcher(opt).find()) {
                    match = true;
                }
            }

            Assert.isTrue(match, "Cannot find pattern match " + p.pattern() + " in JAVA_TOOL_OPTIONS");
        }
    }
}
