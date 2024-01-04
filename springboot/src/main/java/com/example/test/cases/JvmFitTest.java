package com.example.test.cases;

import com.example.test.Argument;
import com.example.test.TestCase;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class JvmFitTest implements TestCase {

    private final static long KB = 1 << 10;
    private final static long MB = KB * KB;
    private final static long GB = KB * MB;
    private final static long TB = KB * GB;

    private static final String SizePattern = "(\\d+)([kmgtKMGT]?)";
    private static final Pattern XmxPattern = Pattern.compile(String.format("-Xmx%s", SizePattern));
    private static final Pattern MetaspacePattern = Pattern.compile(
        String.format("-XX:MaxMetaspaceSize=%s", SizePattern));
    private static final Pattern XssPattern = Pattern.compile(String.format("-Xss%s", SizePattern));
    private static final Pattern CodeCachePattern = Pattern.compile(
        String.format("-XX:ReservedCodeCacheSize=%s", SizePattern));
    private static final Pattern DirectPattern = Pattern.compile(
        String.format("-XX:MaxDirectMemorySize=%s", SizePattern));

    private static final Map<Pattern, Long> PatternsToVerify = new HashMap<>();

    static {
        PatternsToVerify.put(XmxPattern, 400 * MB);
        PatternsToVerify.put(XssPattern, MB);
        PatternsToVerify.put(MetaspacePattern, 50 * MB);
        PatternsToVerify.put(CodeCachePattern, 240 * MB);
        PatternsToVerify.put(DirectPattern, 10 * MB);
    }

    @Override
    public void test(Argument argument) {
        String opts = System.getenv("JAVA_TOOL_OPTIONS");
        if (opts == null || opts.isEmpty()) {
            throw new RuntimeException("JAVA_TOOL_OPTIONS should not be null or empty " + opts);
        }

        String[] splitOpts = opts.split(" ");
        for (Pattern p : PatternsToVerify.keySet()) {
            boolean match = false;
            String sizeStr = "0";
            String unit = "";
            String optionToFind = "";
            for (String opt : splitOpts) {
                Matcher m = p.matcher(opt);
                if (m.find()) {
                    match = true;
                    optionToFind = opt;
                    sizeStr = m.group(1);
                    unit = m.group(2);
                    break;
                }
            }

            Assert.isTrue(match, "Cannot find pattern match " + p.pattern() + " in JAVA_TOOL_OPTIONS");
            long threshold = PatternsToVerify.get(p);
            Assert.isTrue(unitToSize(sizeStr, unit) >= threshold,
                "Option " + optionToFind + " should have value greater than " + threshold);

        }
    }

    private long unitToSize(String sizeStr, String unit) {
        long size = Long.parseLong(sizeStr);
        switch (unit) {
            case "t":
            case "T":
                return size * TB;
            case "g":
            case "G":
                return size * GB;
            case "m":
            case "M":
                return size * MB;
            case "k":
            case "K":
                return size * KB;
            default:
                return size;
        }
    }
}
