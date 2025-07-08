package com.example.ecommercebackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class OneSignalNotificationService {

    @Value("${onesignal.app-id}")
    private String appId;

    @Value("${onesignal.api-key}")
    private String apiKey;

    private final String REST_API_URL = "https://onesignal.com/api/v1/notifications";

    public void sendNotification(String title, String message, String url) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("app_id", appId);
        body.put("included_segments", Collections.singletonList("All"));
        body.put("headings", Collections.singletonMap("en", title));
        body.put("contents", Collections.singletonMap("en", message));
        if (url != null && !url.isEmpty()) {
            body.put("url", url);
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(REST_API_URL, request, String.class);

        System.out.println("Notification envoyée : " + response.getStatusCode() + " - " + response.getBody());
    }
}