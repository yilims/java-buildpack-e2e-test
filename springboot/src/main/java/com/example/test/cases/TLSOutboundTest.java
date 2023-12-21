package com.example.test.cases;

import com.example.test.Argument;
import com.example.test.TestCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TLSOutboundTest implements TestCase {

    @Override
    public void test(Argument argument) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("https://www.bing.com", String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Requesting to www.bing.com got unexpected status " + response.getStatusCode());
        }
    }
}
