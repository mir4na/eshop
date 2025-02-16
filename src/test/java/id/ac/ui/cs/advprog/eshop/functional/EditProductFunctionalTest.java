package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EditProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;
    private String createUrl;
    private String listUrl;

    @BeforeEach
    void setUp() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
        createUrl = baseUrl + "/product/create";
        listUrl = baseUrl + "/product/list";
    }

    private void createTestProduct(ChromeDriver driver, String name, int quantity) {
        driver.get(createUrl);
        driver.findElement(By.id("nameInput")).sendKeys(name);
        driver.findElement(By.id("quantityInput")).sendKeys(String.valueOf(quantity));
        driver.findElement(By.tagName("button")).click();
    }

    @Test
    void editProduct_pageTitle_isCorrect(ChromeDriver driver) {
        createTestProduct(driver, "Test Product", 10);
        driver.findElement(By.className("btn-warning")).click();
        assertEquals("Edit Product", driver.getTitle());
    }

    @Test
    void editProduct_successful(ChromeDriver driver) {

        createTestProduct(driver, "Initial Name", 10);
        driver.findElement(By.className("btn-warning")).click();

        WebElement nameInput = driver.findElement(By.id("productName"));
        WebElement quantityInput = driver.findElement(By.id("productQuantity"));

        nameInput.clear();
        quantityInput.clear();

        String updatedName = "Updated Name";
        int updatedQuantity = 20;

        nameInput.sendKeys(updatedName);
        quantityInput.sendKeys(String.valueOf(updatedQuantity));

        driver.findElement(By.className("btn-primary")).click();

        assertEquals(listUrl, driver.getCurrentUrl());
        assertTrue(driver.findElement(By.tagName("body"))
                .getText().contains(updatedName));
        assertTrue(driver.findElement(By.tagName("body"))
                .getText().contains(String.valueOf(updatedQuantity)));
    }

    @Test
    void editProduct_emptyName_staysOnEditPage(ChromeDriver driver) {

        createTestProduct(driver, "Test Product", 10);
        driver.findElement(By.className("btn-warning")).click();

        WebElement nameInput = driver.findElement(By.id("productName"));
        nameInput.clear();

        driver.findElement(By.className("btn-primary")).click();

        assertTrue(driver.getCurrentUrl().contains("/product/edit"));
    }

    @Test
    void editProduct_invalidQuantity_staysOnEditPage(ChromeDriver driver) {
        createTestProduct(driver, "Test Product", 10);

        driver.findElement(By.className("btn-warning")).click();

        WebElement quantityInput = driver.findElement(By.id("productQuantity"));
        quantityInput.clear();
        quantityInput.sendKeys("-1");

        driver.findElement(By.className("btn-primary")).click();

        assertTrue(driver.getCurrentUrl().contains("/product/list"));
        assertTrue(driver.findElement(By.tagName("body")).getText().contains("10"));
    }

    @Test
    void editProduct_cancelButton_returnsToList(ChromeDriver driver) {

        createTestProduct(driver, "Test Product", 10);
        driver.findElement(By.className("btn-warning")).click();
        driver.findElement(By.className("btn-danger")).click();

        assertEquals(listUrl, driver.getCurrentUrl());
    }

    @Test
    void editProduct_preservesOtherProducts(ChromeDriver driver) {

        createTestProduct(driver, "Product 1", 10);
        createTestProduct(driver, "Product 2", 20);

        driver.findElements(By.className("btn-warning")).getFirst().click();

        WebElement nameInput = driver.findElement(By.id("productName"));
        nameInput.clear();
        nameInput.sendKeys("Updated Product 1");

        driver.findElement(By.className("btn-primary")).click();

        assertTrue(driver.findElement(By.tagName("body")).getText().contains("Product 2"));
        assertTrue(driver.findElement(By.tagName("body")).getText().contains("20"));
    }
}