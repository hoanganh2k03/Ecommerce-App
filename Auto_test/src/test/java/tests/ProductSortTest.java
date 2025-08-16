package tests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.*;

public class ProductSortTest {
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
    public void testSortProductByNameAZ() throws InterruptedException {
        selectSortOption("Name (A to Z)");

        List<String> actualNames = getProductNames();
        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames); // A-Z

        Assert.assertEquals(actualNames, expectedNames, "Sản phẩm chưa được sắp xếp đúng thứ tự A-Z");
        Thread.sleep(5000);
    }

    @Test
    public void testSortProductByNameZA() throws InterruptedException {
        selectSortOption("Name (Z to A)");

        List<String> actualNames = getProductNames();
        List<String> expectedNames = new ArrayList<>(actualNames);
        expectedNames.sort(Collections.reverseOrder()); // Z-A

        Assert.assertEquals(actualNames, expectedNames, "Sản phẩm chưa được sắp xếp đúng thứ tự Z-A");
        Thread.sleep(5000);
    }

    @Test
    public void testSortProductByPriceLowToHigh() throws InterruptedException {
        selectSortOption("Price (low to high)");

        List<Double> actualPrices = getProductPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices); // tăng dần

        Assert.assertEquals(actualPrices, expectedPrices, "Sản phẩm chưa được sắp xếp đúng theo giá tăng dần");
        Thread.sleep(5000);
    }

    @Test
    public void testSortProductByPriceHighToLow() throws InterruptedException {
        selectSortOption("Price (high to low)");

        List<Double> actualPrices = getProductPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        expectedPrices.sort(Collections.reverseOrder()); // giảm dần

        Assert.assertEquals(actualPrices, expectedPrices, "Sản phẩm chưa được sắp xếp đúng theo giá giảm dần");
        Thread.sleep(5000);
    }

    // --- Helper methods ---

    private void selectSortOption(String visibleText) {
        WebElement sortDropdown = driver.findElement(By.className("product_sort_container"));
        sortDropdown.click();
        sortDropdown.findElement(By.xpath("//option[text()='" + visibleText + "']")).click();
    }

    private List<String> getProductNames() {
        List<WebElement> productElements = driver.findElements(By.className("inventory_item_name"));
        List<String> names = new ArrayList<>();
        for (WebElement product : productElements) {
            names.add(product.getText().trim());
        }
        return names;
    }

    private List<Double> getProductPrices() {
        List<WebElement> priceElements = driver.findElements(By.className("inventory_item_price"));
        List<Double> prices = new ArrayList<>();
        for (WebElement price : priceElements) {
            String text = price.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(text));
        }
        return prices;
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
