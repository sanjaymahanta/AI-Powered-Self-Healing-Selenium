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

public class Testing {

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

    
    //http://uitestingplayground.com/
    @Test
    public void testDynamicID() {
    	  baseDriver.get("http://uitestingplayground.com/");
    	  driver.findElement(By.xpath("DESCRIPTION: click the link that contains the text 'Dynamic ID'")).click();
    	  
    	  System.out.println(" AI Task 2: Clicking the dynamic button...");
          WebElement dynamicButton = driver.findElement(By.xpath("DESCRIPTION: find the button that says 'Button with Dynamic ID'"));
          
          // Click action
          dynamicButton.click();
          
          System.out.println(" Success! AI bypassed the dynamic ID completely.");
    }
}
