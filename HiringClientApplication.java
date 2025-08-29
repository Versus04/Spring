package com.example.hiringclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class HiringClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HiringClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Step 1: Generate webhook
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        Map<String, String> request = new HashMap<>();
        request.put("name", "John Doe");
        request.put("regNo", "REG12347");
        request.put("email", "john@example.com");

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        String webhook = (String) response.getBody().get("webhook");
        String accessToken = (String) response.getBody().get("accessToken");

        // Step 2: Prepare SQL query answer
        String finalQuery = "SELECT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME, " +
                "COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT " +
                "FROM EMPLOYEE e " +
                "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "LEFT JOIN EMPLOYEE e2 ON e.DEPARTMENT = e2.DEPARTMENT " +
                "AND e2.DOB > e.DOB " +
                "GROUP BY e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME " +
                "ORDER BY e.EMP_ID DESC;";

        Map<String, String> answer = new HashMap<>();
        answer.put("finalQuery", finalQuery);

        // Step 3: Submit query
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(answer, headers);
        restTemplate.postForEntity(webhook, entity, String.class);
    }
}
