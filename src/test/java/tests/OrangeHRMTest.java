package tests;

import java.time.Duration;
import java.util.Collections;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert; // Assertion ke liye important import
import org.testng.annotations.Test;

import driver.SelfHealingDriver;

public class OrangeHRMTest {

    @Test
    public void loginTest() {
   
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--remote-allow-origins=*");

        // 1. WebDriver Initialization
        WebDriver baseDriver = new ChromeDriver(options);
        
        // 2. Wrapping with AI Self-Healing Driver
        SelfHealingDriver driver = new SelfHealingDriver(baseDriver);

        try {
            // 3. Navigation (Bhai, local file ke liye file:/// use karna better hai)
            baseDriver.get("http://127.0.0.1:5500/Testing.html");
            baseDriver.manage().window().maximize();

            // 4. Click Action (Idhar AI apna kamaal dikhayega agar ID change hui hai)
            System.out.println("🚀 AI is finding and healing the element...");
          driver.findElement(By.xpath("DESCRIPTION:find Login Now ")).click(); 

            // 5. ASSERTION: Yahan hum check karenge ki success message aaya ya nahi
            System.out.println("🧐 Validating the functional result...");
            
            WebDriverWait wait = new WebDriverWait(baseDriver, Duration.ofSeconds(5));
            
            // Hum check kar rahe hain ki 'success-msg' wala element visible hai ya nahi
            WebElement successMsg = baseDriver.findElement(By.id("success-msg"));
            
            // ASSERTION LOGIC:
            // Agar message visible hai -> Test Pass
            // Agar message visible nahi hai -> Test Fail (Asli Bug caught!)
            Assert.assertTrue(successMsg.isDisplayed(), 
                "❌ FAILURE: Button was clicked (Healed), but the Login Success message did not appear. Functional Bug detected!");

            System.out.println("✅ TEST PASSED: Feature is working fine.");

        } catch (Exception e) {
            System.err.println("💥 TEST FAILED: " + e.getMessage());
            Assert.fail("Test execution failed due to: " + e.getMessage());
        } finally {
            // baseDriver.quit(); // Test khatam hone ke baad browser band karne ke liye
        }
    }
}