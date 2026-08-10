package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import driver.SelfHealingDriver;

public class SelfHealingDemo {

	
	

		
		private WebDriver baseDriver;
	    private SelfHealingDriver driver; // Tumhara self-healing engine

	    // Constants
	    private final String BASE_URL = "http://127.0.0.1:5500/index.html";


	    @BeforeMethod
	    public void setup() {
	        baseDriver = new ChromeDriver();
	        baseDriver.manage().window().maximize();

	        // 🚀 Base driver ko apne AI wrapper ke sath wrap kiya
	        driver = new SelfHealingDriver(baseDriver);
	    }

	    @Test
	    public void testSelfHealing() {
	    	try {
	            // 1. Open Login Page
	            System.out.println("🚀 Opening Enterprise Login Demo...");
	            baseDriver.get(BASE_URL);

	            // 2. Enter Email
	            System.out.println("🤖 AI Action: Finding Email field...");
	            driver.findElement(By.xpath("DESCRIPTION: find the email address input field"))
	                  .sendKeys("admin@company.com");

	            // 3. Enter Password
	            System.out.println("🤖 AI Action: Finding Password field...");
	            driver.findElement(By.xpath("DESCRIPTION: find the password input field"))
	                  .sendKeys("SecurePass123!");

	            // 4. Click Login Button 
	            System.out.println("🤖 AI Action: Clicking the Secure Login button...");
	            driver.findElement(By.xpath("DESCRIPTION: find the main Secure Login button with the orange background"))
	                  .click();
	            
	            System.out.println("✅ AI successfully found and clicked the button!");

	            // Mock API delay (1 second) ke liye thoda wait karte hain
	            Thread.sleep(1500); 

	            // 5. Verify Application State (Christian ke doubt ka asli test)
	            System.out.println("🤖 AI Action: Verifying successful login alert...");
	            
	            // Agar developer ne JS tod diya hoga, toh yeh alert aayega hi nahi, aur test fail ho jayega!
	            WebElement successAlert = driver.findElement(By.xpath("DESCRIPTION: find the success alert message box showing token generated"));
	            
	            Assert.assertTrue(successAlert.isDisplayed(), "CRITICAL BUG CAUGHT: Button was clicked, but application event did not fire!");
	            
	            System.out.println("🏆 SUCCESS: Test passed! The framework healed locators AND validated true application state.");
	      
	        } catch (Exception e) {
	            // Jab aap jaan-bujh kar JS todoge video mein, tab test yahan aakar fail hoga.
	            System.out.println("❌ Test Failed (Expected if JS event binding is broken): " + e.getMessage());
	            Assert.fail("AI Test caught a functional bug: " + e.getMessage());
	        }
	    }

	    @AfterMethod
	    public void tearDown() {
	        if (baseDriver != null) {
	            baseDriver.quit();
	            System.out.println("🔒 Browser closed.");
	        }
	    }

}
