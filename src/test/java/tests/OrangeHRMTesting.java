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

public class OrangeHRMTesting {

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

@Test
public void addUser() throws InterruptedException{

	 baseDriver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
     System.out.println(" application is opening");
	System.out.println(" AI Action: Finding Username and entering 'Admin'...");
    driver.findElement(By.xpath("DESCRIPTION: find the Username input field"))
          .sendKeys("Admin");

    // 3. Password Enter Karna
    System.out.println(" AI Action: Finding Password and entering 'admin123'...");
    driver.findElement(By.xpath("DESCRIPTION: find the Password input field")).sendKeys("admin123");

    Thread.sleep(2000);
    System.out.println("🤖 AI Action: Clicking the Login button...");
    driver.findElement(By.xpath("DESCRIPTION: find the Login button")).click();

    // Login ke baad dashboard load hone mein thoda time lagta hai
    System.out.println("⏳ Waiting for Dashboard to load...");
    Thread.sleep(4000); // Wait for the left menu to appear

    // 5. Left side mein PIM link par click karna
    System.out.println("🤖 AI Action: Finding and clicking the PIM menu link...");
    driver.findElement(By.xpath("DESCRIPTION: find the 'PIM' link in the left side navigation menu"))
          .click();

    System.out.println("✅ Success! AI successfully logged in and navigated to PIM.");
    
    System.out.println("⏳ Waiting for PIM Employee List page to load...");
    Thread.sleep(3000); // Grid load hone ka wait

    // 6. Click 'Add' Button
    System.out.println("🤖 AI Action: Finding and clicking the 'Add' button...");
    driver.findElement(By.xpath("DESCRIPTION: find the 'Add' button on the page"))
          .click();

    System.out.println("⏳ Waiting for 'Add Employee' form to load...");
    Thread.sleep(3000); // Form aur input fields aane ka wait

    // 7. Enter First Name
    System.out.println("🤖 AI Action: Entering First Name...");
    driver.findElement(By.xpath("DESCRIPTION: find the First Name text input field"))
          .sendKeys("Vicky");

    // 8. Enter Middle Name
    System.out.println("🤖 AI Action: Entering Middle Name...");
    driver.findElement(By.xpath("DESCRIPTION: find the Middle Name text input field"))
          .sendKeys("kumar");

    // 9. Enter Last Name
    System.out.println("🤖 AI Action: Entering Last Name...");
    driver.findElement(By.xpath("DESCRIPTION: find the Last Name text input field"))
          .sendKeys("singh");

    // Note: Employee ID pehle se bhari hui hai, toh AI ko usko ignore karne denge.

    // 10. Click Save Button
    System.out.println("🤖 AI Action: Finding and clicking the Save button...");
    driver.findElement(By.xpath("DESCRIPTION: find the Save button at the bottom of the form"))
          .click();

    System.out.println("✅ Ultimate Success! Employee 'Vicky kumar singh' created using pure AI intents!");
}
}