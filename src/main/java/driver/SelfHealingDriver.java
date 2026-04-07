package driver;

import healer.GeminiHealer; // Updated Import
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;
import utils.CSSUtils;
import java.time.Duration;

public class SelfHealingDriver {

    private final WebDriver driver;
    private final boolean healingEnabled;
    private final GeminiHealer healer; // Updated Class
    private final WebDriverWait wait;

    public SelfHealingDriver(WebDriver driver) {
        this.driver = driver;
        this.healingEnabled = ConfigReader.isHealingEnabled();
        // ConfigReader mein bhi method name change kar lena (getGeminiKey)
        this.healer = new GeminiHealer(ConfigReader.getGeminiKey()); 
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public WebElement findElement(By locator) {
        String locatorString = locator.toString();
        
        // Logic same rahega...
        if (locatorString.toUpperCase().contains("DESCRIPTION:")) {
            int startIndex = locatorString.toUpperCase().indexOf("DESCRIPTION:") + 12;
            String description = locatorString.substring(startIndex).trim();
            
            By healed = healer.heal(description, driver.getPageSource());
            if (healed == null) throw new NoSuchElementException("AI failed for: " + description);
            return wait.until(ExpectedConditions.presenceOfElementLocated(healed));
        }

        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            if (!healingEnabled || !locatorString.contains("xpath")) throw e;

            String brokenXpath = CSSUtils.extractXPath(locator);
            By healed = healer.heal(brokenXpath, driver.getPageSource());

            if (healed != null) {
                return wait.until(ExpectedConditions.presenceOfElementLocated(healed));
            }
            throw e;
        }
    }
}