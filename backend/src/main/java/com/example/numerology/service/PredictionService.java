package com.example.numerology.service;

import com.example.numerology.model.Prediction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class PredictionService {

    private static final String FASTAPI_URL = "http://127.0.0.1:8000/predict";
    private static final String COUCHDB_URL = "http://admin:couchdb@localhost:5984/numerology";
    private static final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper mapper;

    @Autowired
    public PredictionService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public int predict(String text) {
        // Prepare JSON payload
        String jsonPayload = "{\"text\":\"" + text.replace("\"", "\\\"") + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(FASTAPI_URL, request, String.class);
            JsonNode root = mapper.readTree(response.getBody());
            return root.get("number").asInt();
        } catch (Exception e) {
            System.err.println("FastAPI prediction failed: " + e.getMessage());
            throw new RuntimeException("Model server unreachable. Is FastAPI running on port 8000?", e);
        }
    }

public void savePrediction(String text, int number) throws Exception {
    Prediction p = new Prediction();
    p.setText(text);
    p.setNumber(number);
    p.setTimestamp(LocalDateTime.now());

    String json = mapper.writeValueAsString(p);

    // THIS IS THE FIX: embed credentials directly in the URL
    URL url = new URL("http://admin:couchdb@127.0.0.1:5984/numerology");

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setDoOutput(true);

    try (var os = conn.getOutputStream()) {
        os.write(json.getBytes("UTF-8"));
    }

    int responseCode = conn.getResponseCode();
    if (responseCode < 200 || responseCode >= 300) {
        String errorBody = conn.getErrorStream() != null 
            ? new String(conn.getErrorStream().readAllBytes()) 
            : "No error body";
        throw new RuntimeException("CouchDB save failed (HTTP " + responseCode + "): " + errorBody);
    }

    System.out.println("Saved to CouchDB: " + responseCode); // ← you’ll see 201
}

   public List<Prediction> getHistory() throws Exception {
    List<Prediction> list = new ArrayList<>();
    URL url = new URL("http://admin:couchdb@127.0.0.1:5984/numerology/_all_docs?include_docs=true");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");

    int responseCode = conn.getResponseCode();
    if (responseCode != 200) {
        throw new RuntimeException("Failed to fetch history: HTTP " + responseCode);
    }

    try (Scanner sc = new Scanner(conn.getInputStream(), "UTF-8")) {
        String resp = sc.useDelimiter("\\A").next();
        JsonNode root = mapper.readTree(resp);
        for (JsonNode row : root.path("rows")) {
            JsonNode doc = row.path("doc");
            if (doc.has("text") && !doc.path("_id").asText().startsWith("_design")) {
                Prediction p = new Prediction();
                p.setId(doc.path("_id").asText());
                p.setText(doc.path("text").asText());
                p.setNumber(doc.path("number").asInt());
                String ts = doc.path("timestamp").asText();
                if (!ts.isEmpty()) p.setTimestamp(LocalDateTime.parse(ts));
                list.add(p);
            }
        }
    }
    return list;
}
}