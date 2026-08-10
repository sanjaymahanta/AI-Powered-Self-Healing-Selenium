package healer;

import okhttp3.*;
import org.json.JSONObject;
import org.openqa.selenium.By;
import utils.XPathUtils;
import java.util.concurrent.TimeUnit;

public class LlamaHealer {

    // Ollama local endpoint (Default local URL)
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private final OkHttpClient client;

    public LlamaHealer() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS) // Local processing ke liye safe backup timeout
                .readTimeout(180, TimeUnit.SECONDS)    
                .writeTimeout(180, TimeUnit.SECONDS)
                .build();
    }

    public By heal(String description, String pageSource) {
        try {
            // 🔥 Prompt ko strictly XPath response ke liye lock kiya hai
            String prompt = "You are an expert SDET. Find the single best valid XPath for this element description: '" + description + "'.\n" +
                            "Rules:\n" +
                            "1. Return ONLY the raw valid XPath string starting with '//' (e.g., //button[@name='add'] or //img[@class='predictive-search__image']).\n" +
                            "2. Absolutely NO CSS selectors allowed.\n" +
                            "3. Absolutely NO markdown, NO backticks (```), NO explanations, NO intro text.\n" +
                            "4. Do not include 'xpath=' or any prefix text.\n\n" +
                            "HTML Source:\n" + 
                            pageSource.substring(0, Math.min(pageSource.length(), 100000));

            JSONObject body = new JSONObject();
            body.put("model", "qwen2.5:3b"); // 👈 Tumhara exact local running model name
            body.put("prompt", prompt);
            body.put("stream", false);
            
            // Temperature 0.0 kiya hai taaki Qwen strictly rules follow kare aur extra text na bune
            JSONObject options = new JSONObject();
            options.put("temperature", 0.0); 
            body.put("options", options);

            Request request = new Request.Builder()
                    .url(OLLAMA_URL)
                    .post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseString);
                    String rawXpath = jsonResponse.getString("response").trim();
                    
                    System.out.println("🦙 Qwen Local Suggested (Raw): " + rawXpath);
                    
                    // 🔥 FIX: Java syntax ke hisab se safe and single-line regex clean up
                    rawXpath = rawXpath.replace("```xpath", "")
                                       .replace("```", "")
                                       .replace("\n", "")
                                       .replace("\r", "")
                                       .trim();
                    
                    System.out.println("🦙 Qwen Local Cleaned XPath: " + rawXpath);
                    
                    // Direct tumhare framework ke XPath Utility handler ke paas safe send hoga
                    return By.xpath(XPathUtils.sanitizeXPath(rawXpath));
                } else {
                    System.err.println("❌ Ollama API Error [" + response.code() + "]: " + response.message());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Llama Healing Error: " + e.getMessage());
        }
        return null;
    }
}