package tests;

import java.time.Duration;
import java.util.Collections;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import driver.SelfHealingDriver;

public class OrangeHRMTest {

    @Test
    public void loginTest() {
   
        ChromeOptions options = new ChromeOptions();
        
       
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--remote-allow-origins=*");

        // 2. WebDriver Initialization
        WebDriver baseDriver = new ChromeDriver(options);
        
        // Wrapping it with your AI Self-Healing Driver
        SelfHealingDriver driver = new SelfHealingDriver(baseDriver);

        try {
            // 3. Navigation to Nexus Automation Portal
            baseDriver.get("http://127.0.0.1:5500/index.html");
            
            // Bypass maximize() error by setting a manual large window size
            baseDriver.manage().window().maximize();

            // 4. Intent-Based Finding (Description Only)
            System.out.println("🚀 AI is finding the element using Description...");
            driver.findElement(By.xpath("DESCRIPTION: find Login under Guest Login")).click();
            
            // 5. Alert Handling & Message Capture
            WebDriverWait wait = new WebDriverWait(baseDriver, Duration.ofSeconds(10));
            if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
                Alert alert = baseDriver.switchTo().alert();
                
                String alertMessage = alert.getText(); 
                System.out.println("🔔 Alert Captured: " + alertMessage);
                
                alert.accept();
                System.out.println("✅ Alert Accepted successfully.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error occurred during execution: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 6. Cleanup
            System.out.println("🏁 Test Execution Completed.");
            baseDriver.quit();
        }
    }
}