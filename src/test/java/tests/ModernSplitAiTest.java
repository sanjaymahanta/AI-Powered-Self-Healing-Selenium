package tests;



import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import driver.SelfHealingDriver;
import java.time.Duration;

public class ModernSplitAiTest {

    private WebDriver baseDriver;
    private SelfHealingDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        // Driver Initialize
        baseDriver = new ChromeDriver();
        baseDriver.manage().window().maximize();
        
        // Wrap with AI Self-Healing
        driver = new SelfHealingDriver(baseDriver);
        
        // Explicit Wait for Assertions
        wait = new WebDriverWait(baseDriver, Duration.ofSeconds(10));
    }

    @Test(description = "Verify AI can distinguish between Guest and Admin sections without IDs")
    public void verifySplitScreenIntentLogin() throws InterruptedException {
        // 1. Open the Modern Split Page
        // Make sure your local server is running (VS Code Live Server recommended)
        baseDriver.get("http://127.0.0.1:5500/ModernTesting.html");
        System.out.println(" Modern Split-Screen Experiment Started...");

        // --- STEP 1: GUEST LOGIN ---
        System.out.println(" AI Task: Finding Guest Login...");
        driver.findElement(By.xpath("DESCRIPTION: Find  LOGIN AS GUEST inside the GUEST ")).click();

        // Assertion 1: Check if Guest Overlay appears
        WebElement overlay = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("overlay")));
        String guestMsg = overlay.getText();
        System.out.println(" Overlay Message: " + guestMsg);
        
        Assert.assertTrue(guestMsg.contains("Guest Access"), 
            "❌ FAILED: AI did not click the Guest button correctly!");
        System.out.println(" Assertion Passed: Guest Login Successful.");

        // Wait for overlay to disappear
        Thread.sleep(3500); 

        // --- STEP 2: ADMIN LOGIN ---
        System.out.println(" AI Task: Finding Admin Login...");
        driver.findElement(By.xpath("DESCRIPTION: click the login button inside the ADMIN section")).click();

        // Assertion 2: Check if Admin Overlay appears
        // Re-finding overlay as it might have hidden/re-appeared
        WebElement adminOverlay = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("overlay")));
        String adminMsg = adminOverlay.getText();
        System.out.println(" Overlay Message: " + adminMsg);

        Assert.assertTrue(adminMsg.contains("Admin Access"), 
            " FAILED: AI did not click the Admin button correctly!");
        System.out.println(" Assertion Passed: Admin Login Successful.");
    }

//    @AfterMethod
//    public void tearDown() {
//        if (baseDriver != null) {
//            baseDriver.quit();
//        }
    
}
