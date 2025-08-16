package tests;

import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;


public class CheckoutTest {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        ChromeOptions options = new ChromeOptions();
         Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_leak_detection", false);
         options.setExperimentalOption("prefs", prefs);
 options.addArguments("--disable-features=PasswordCheck");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-popup-blocking");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

        // Login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
    }

    @Test
    public void testCheckoutFlow() throws InterruptedException {
        // Thêm 1 sản phẩm vào giỏ
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        // Bấm giỏ hàng
        driver.findElement(By.className("shopping_cart_link")).click();

        // Bấm Checkout
        driver.findElement(By.id("checkout")).click();

        // Nhập thông tin khách hàng
        driver.findElement(By.id("first-name")).sendKeys("Nguyen");
        driver.findElement(By.id("last-name")).sendKeys("Tester");
        driver.findElement(By.id("postal-code")).sendKeys("700000");

        // Bấm Continue
        driver.findElement(By.id("continue")).click();

        Thread.sleep(2000); // chờ trang load (demo)

        // Verify đã tới trang "Checkout: Overview"
        WebElement title = driver.findElement(By.className("title"));
        Assert.assertEquals(title.getText(), "Checkout: Overview", "❌ Không vào được trang Checkout Overview!");
        driver.findElement(By.id("finish")).click();

        Thread.sleep(2000); // chờ trang load (demo)

        // Verify đã tới trang Checkout Complete
        WebElement completeHeader = driver.findElement(By.className("complete-header"));
        Assert.assertEquals(completeHeader.getText(), "Thank you for your order!", "❌ Đơn hàng chưa hoàn tất!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
