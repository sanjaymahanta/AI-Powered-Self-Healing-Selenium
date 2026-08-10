package healer;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import utils.XPathUtils;
import java.util.concurrent.TimeUnit;

public class GeminiHealer {

    // ✅ FIXED URL: Isme model name 'gemini-1.5-flash' already embedded hai
    private static final String BASE_URL = 
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=";

    private final OkHttpClient client;
    private final String apiKey;

    public GeminiHealer(String apiKey) {
        this.apiKey = apiKey != null ? apiKey.trim() : "";
        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public By heal(String inputSignal, String pageSource) {
        try {
            // Safety: DOM bada ho sakta hai, isliye limit kar rahe hain
            String dom = pageSource.substring(0, Math.min(pageSource.length(), 100000));

            // ✅ Exact Gemini JSON Structure
            JSONObject body = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject part = new JSONObject().put("text", 
                "You are a Selenium expert. Return ONLY the XPath for this element: '" + inputSignal + 
                "'.\n\nHTML:\n" + dom);
            
            contents.put(new JSONObject().put("parts", new JSONArray().put(part)));
            body.put("contents", contents); 

            Request request = new Request.Builder()
                    .url(BASE_URL + apiKey) // Key appending correctly here
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                
                if (!response.isSuccessful()) {
                    // Agar abhi bhi 404 aaye, toh yahan print hoga ki exact issue kya hai
                    System.err.println("❌ API Error (" + response.code() + "): " + responseBody);
                    return null;
                }

                JSONObject jsonResponse = new JSONObject(responseBody);
                String rawXpath = jsonResponse.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text");

                return By.xpath(XPathUtils.sanitizeXPath(rawXpath));
            }
        } catch (Exception e) {
            System.err.println("Critical Failure: " + e.getMessage());
            return null;
        }
    }
}