package tests;



import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import driver.SelfHealingDriver;
public class ShopifyAIHealingTest {

	
	private WebDriver baseDriver;
    private SelfHealingDriver driver; // Tumhara self-healing engine

    // Constants
    private final String BASE_URL = "https://adnabu-store-assignment1.myshopify.com";
    private final String PASSWORD = "AdNabuQA";
    private final String PRODUCT_NAME = "The Collection Snowboard: Liquid";

    @BeforeMethod
    public void setup() {
        baseDriver = new ChromeDriver();
        baseDriver.manage().window().maximize();

        // 🚀 Base driver ko apne AI wrapper ke sath wrap kiya
        driver = new SelfHealingDriver(baseDriver);
    }

    @Test
    public void testShopifyWithAIIntents() {
        try {
            // 1. Open Store
            System.out.println("🚀 Opening store...");
            baseDriver.get(BASE_URL);

            // 2. Login Using Store Password
            System.out.println("🤖 AI Action: Finding Store Password Input...");
            // AI ko context diya ki password page par field dhunde
            driver.findElement(By.xpath("DESCRIPTION: find the password text input field on the store landing page"))
                  .sendKeys(PASSWORD + Keys.ENTER);
            System.out.println("✅ Login successful!");

            // 3. Search Product
            System.out.println("🤖 AI Action: Opening search box icon...");
            // Summary header toggle wrapper dhundega AI
            driver.findElement(By.xpath("DESCRIPTION: find the search icon button with aria-label Search in the header"))
                  .click();

            System.out.println("🤖 AI Action: Typing product name inside Search Modal...");
            // 🔥 Yahan tumhara modal wala concept kaam aayega! Humne AI ko specify kar diya 'inside the search modal popup'
            driver.findElement(By.xpath("DESCRIPTION: find the visible search text input field inside the active search modal popup or dialog box"))
                  .sendKeys(PRODUCT_NAME);

            System.out.println("🤖 AI Action: Selecting product by clicking its predictive search image...");
         // 🔥 FIX: AI ko clear instruction di hai ki 'predictive-search__image' class wali image ko hi target kare
         driver.findElement(By.xpath("DESCRIPTION: find the product image element inside predictive search results containing class predictive-search__image"))
               .click();
         System.out.println("✅ Product page opened successfully via image click!");

            // 4. Add Product To Cart
            System.out.println("🤖 AI Action: Clicking Add to Cart button...");
            driver.findElement(By.xpath("DESCRIPTION: find the main Add to cart submit button on the product details page"))
                  .click();
            System.out.println("✅ Product added to cart!");

         // 5. Verify Product In Cart Drawer
            System.out.println("🤖 AI Action: Verifying cart drawer header...");
            // Drawer slide-in hone par header read karega
            WebElement cartHeader = driver.findElement(By.xpath("DESCRIPTION: find the visible Your cart heading inside the opened cart slide-out drawer"));
            
            System.out.println("🤖 AI Action: Finding the product title inside the cart...");
            // Dynamic name handle karne ke liye description mein product name append kar diya
            WebElement productInCart = driver.findElement(By.xpath("DESCRIPTION: find the link containing text '" + PRODUCT_NAME + "' inside the cart drawer list"));

            // TestNG Assertions to evaluate if AI actually fetched the right displayed elements
            Assert.assertTrue(cartHeader.isDisplayed(), "FAIL: Cart drawer header is not visible!");
            Assert.assertTrue(productInCart.isDisplayed(), "FAIL: Product is not visible in the cart!");
            
            System.out.println("🏆 SUCCESS: '" + PRODUCT_NAME + "' successfully added to cart using pure AI Intent-Based Finding!");
      
        } catch (Exception e) {
            System.out.println("❌ Test Failed: " + e.getMessage());
            Assert.fail("AI Intent Test failed due to: " + e.getMessage());
        }
    }
//    @AfterMethod
//    public void tearDown() {
//        if (baseDriver != null) {
//            baseDriver.quit();
//            System.out.println("🔒 Browser closed.");
//        }
//    }
}
