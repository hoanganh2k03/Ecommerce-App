package tests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailTest {
    WebDriver driver;
    @BeforeClass
    public void setup() {
    	ChromeOptions options = new ChromeOptions();

        // Tạo map prefs
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_leak_detection", false);

        // Gán prefs vào options
        options.setExperimentalOption("prefs", prefs);

        // Thêm một số flags để tắt popup mật khẩu
        options.addArguments("--disable-features=PasswordCheck");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-popup-blocking");
    	    // 3. Khởi tạo driver
    	    driver = new ChromeDriver(options);
    	    driver.manage().window().maximize();
    	    driver.get("https://www.saucedemo.com/");

    	    // 4. Đăng nhập
    	    driver.findElement(By.id("user-name")).sendKeys("standard_user");
    	    driver.findElement(By.id("password")).sendKeys("secret_sauce");
    	    driver.findElement(By.id("login-button")).click();
    }
    @Test
    public void testClickFirstProductOpensDetailPage() throws InterruptedException {
        // Giả sử driver đã đăng nhập và đang ở trang inventory.html

        // Lấy danh sách các sản phẩm
        List<WebElement> products = driver.findElements(By.className("inventory_item_name"));

        // Kiểm tra có ít nhất 1 sản phẩm
        Assert.assertTrue(products.size() > 0, "Không có sản phẩm nào hiển thị!");

        // Click vào sản phẩm đầu tiên
        WebElement firstProduct = products.get(0);
        String productName = firstProduct.getText();
        System.out.println("Click sản phẩm đầu tiên: " + productName);
        firstProduct.click();

        // Chờ trang chi tiết load (có thể dùng WebDriverWait nếu cần chắc chắn)
        Thread.sleep(1000);

        // Lấy URL hiện tại
        String currentUrl = driver.getCurrentUrl();

        // Kiểm tra định dạng URL đúng là trang chi tiết sản phẩm
        Assert.assertTrue(currentUrl.matches("https://www\\.saucedemo\\.com/inventory-item\\.html\\?id=\\d+"),
                "URL không hợp lệ: " + currentUrl);
    }
}
