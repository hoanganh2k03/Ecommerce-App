package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CartTest {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        
        driver.manage().window().maximize();

        // Truy cập trang login
        driver.get("https://www.saucedemo.com/");

        // Đăng nhập
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
    }

    @Test(priority = 1)
    public void testAddThenRemoveFirstItemOnSamePage() {
        // Thêm sản phẩm đầu tiên tại trang inventory
        WebElement firstAddToCartButton = driver.findElements(By.className("btn_inventory")).get(0);
        firstAddToCartButton.click();

        // Kiểm tra nút đã chuyển thành "Remove"
        WebElement removeButton = driver.findElements(By.className("btn_inventory")).get(0);
        Assert.assertEquals(removeButton.getText(), "Remove");

        // Bấm Remove ngay tại đó
        removeButton.click();

        // Kiểm tra nút đã chuyển lại thành "Add to cart"
        WebElement addButtonAgain = driver.findElements(By.className("btn_inventory")).get(0);
        Assert.assertEquals(addButtonAgain.getText(), "Add to cart");
    }

    @Test(priority = 2)
    public void testAddToCartThenGoToCart() {
        // Lưu tên sản phẩm đầu tiên
        String firstProductName = driver.findElements(By.className("inventory_item_name")).get(0).getText();

        // Thêm sản phẩm vào giỏ
        driver.findElements(By.className("btn_inventory")).get(0).click();

        // Mở giỏ hàng
        driver.findElement(By.className("shopping_cart_link")).click();

        // Kiểm tra tên sản phẩm trong giỏ
        WebElement cartItem = driver.findElement(By.className("inventory_item_name"));
        Assert.assertEquals(cartItem.getText(), firstProductName, "Sản phẩm trong giỏ không khớp!");
    }

    @Test(priority = 3)
    public void testRemoveFromCart() throws InterruptedException {
        // Remove sản phẩm trong giỏ
        driver.findElement(By.cssSelector("button.cart_button")).click();

        // Kiểm tra không còn item nào trong giỏ
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertEquals(cartItems.size(), 0, "Giỏ hàng vẫn còn sản phẩm!");

        // (Optional) Bấm nút 'Continue Shopping' để quay lại trang chính
        driver.findElement(By.id("continue-shopping")).click();
        Thread.sleep(1000);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
