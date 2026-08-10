package driver;

import healer.GeminiHealer;
import healer.LlamaHealer;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;
import utils.XPathUtils;
import java.time.Duration;

public class SelfHealingDriver {

    private final WebDriver driver;
    private final boolean healingEnabled;
    private final WebDriverWait wait;
    
    private GeminiHealer geminiHealer;
    private LlamaHealer llamaHealer;
    private String aiType;

    public SelfHealingDriver(WebDriver driver) {
        this.driver = driver;
        this.healingEnabled = ConfigReader.isHealingEnabled();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Config se read karo: "gemini" ya "ollama"/"llama"
        this.aiType = ConfigReader.getProperty("ai.type", "gemini");

        // 🔥 FIX: "ollama" aur "llama" dono words ko support karo safely
        if (aiType.equalsIgnoreCase("ollama") || aiType.equalsIgnoreCase("llama")) {
            this.llamaHealer = new LlamaHealer();
            System.out.println("🦙 System Initialized with Local oLlama");
        } else {
            this.geminiHealer = new GeminiHealer(ConfigReader.getGeminiKey());
            System.out.println("♊ System Initialized with Google Gemini");
        }
    }

    public WebElement findElement(By locator) {
        String locatorString = locator.toString();

        // 1. Intent-Based Finding (DESCRIPTION:)
        if (locatorString.toUpperCase().contains("DESCRIPTION:")) {
            String description = locatorString.substring(locatorString.toUpperCase().indexOf("DESCRIPTION:") + 12).trim();
            System.out.println("🚀 AI Finding by Intent: " + description);
            
            // 🔥 PAUSE BUFFER: AI ko DOM bhejne se pehle 2 seconds wait karo taaki dynamic elements load ho jayein
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            By healed = callAI(description);
            
            if (healed == null) throw new NoSuchElementException("AI failed for: " + description);
            
            // 🔥 SMART WAITING: Button/Click entities ke liye clickable wait use karo, baaki ke liye visibility
            try {
                if (description.toLowerCase().contains("click") || description.toLowerCase().contains("button")) {
                    return wait.until(ExpectedConditions.elementToBeClickable(healed));
                } else {
                    return wait.until(ExpectedConditions.visibilityOfElementLocated(healed));
                }
            } catch (Exception e) {
                return wait.until(ExpectedConditions.presenceOfElementLocated(healed)); // Fallback
            }
        }

        // 2. Normal Finding with Self-Healing
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            if (!healingEnabled || !locatorString.contains("xpath")) throw e;

            System.out.println("🩹 Locator broken. Healing now...");
            String brokenXpath = XPathUtils.extractXPath(locator);
            By healed = callAI(brokenXpath);

            if (healed != null) {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(healed));
            }
            throw e;
        }
    }

    // Helper method jo decide karega kaunsa AI call karna hai
    private By callAI(String signal) {
        // Body uthao
        WebElement body = driver.findElement(By.tagName("body"));
        String cleanDOM = body.getAttribute("innerHTML");

        // Faltu ki cheezein hatao taaki AI confuse na ho aur limit bachi rahe
        cleanDOM = cleanDOM.replaceAll("<script[\\s\\S]*?</script>", "");
        cleanDOM = cleanDOM.replaceAll("<style[\\s\\S]*?</style>", "");

        // Limit range set kiya
        String truncatedDOM = cleanDOM.substring(0, Math.min(cleanDOM.length(), 15000));

        // 🔥 CRITICAL FIX: Constructor ki tarah yahan bhi "ollama" aur "llama" dono matching allow karo
        if (aiType.equalsIgnoreCase("ollama") || aiType.equalsIgnoreCase("llama")) {
            return llamaHealer.heal(signal, truncatedDOM);
        } else {
            return geminiHealer.heal(signal, truncatedDOM);
        }
    }
}