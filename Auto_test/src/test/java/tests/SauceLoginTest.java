package tests;

import java.util.Base64;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SauceLoginTest {
    WebDriver driver;
    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void openLoginPage() {
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void testLoginSuccess() {
        login("standard_user", "secret_sauce");
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "Login không thành công!");
    }

    @Test
    public void testLoginWithInvalidPassword() {
        login("standard_user", "wrong_password");
        assertErrorMessage("Epic sadface: Username and password do not match any user in this service");
    }

    @Test
    public void testLoginWithInvalidUsername() {
        login("wrong_user", "secret_sauce");
        assertErrorMessage("Epic sadface: Username and password do not match any user in this service");
    }

    @Test
    public void testLoginWithEmptyUsername() {
        login("", "secret_sauce");
        assertErrorMessage("Epic sadface: Username is required");
    }

    @Test
    public void testLoginWithEmptyPassword() {
        login("standard_user", "");
        assertErrorMessage("Epic sadface: Password is required");
    }

    @Test
    public void testLoginWithEmptyCredentials() {
        login("", "");
        assertErrorMessage("Epic sadface: Username is required");
    }

    @Test
    public void testLoginWithLockedUser() {
        login("locked_out_user", "secret_sauce");
        assertErrorMessage("Epic sadface: Sorry, this user has been locked out.");
    }
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ------------------------------
    // ✅ Helper methods
    // ------------------------------

    private void login(String username, String password) {
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys(username);

        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("login-button")).click();
    }

    private void assertErrorMessage(String expectedMessage) {
        WebElement error = driver.findElement(By.cssSelector("h3[data-test='error']"));
        Assert.assertEquals(error.getText(), expectedMessage, "Thông báo lỗi không đúng!");
    }
}
