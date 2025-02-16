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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DeleteProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;
    private String createUrl;

    @BeforeEach
    void setUp() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
        createUrl = baseUrl + "/product/create";
    }

    private void createTestProduct(ChromeDriver driver, String name, int quantity) {
        driver.get(createUrl);
        driver.findElement(By.id("nameInput")).sendKeys(name);
        driver.findElement(By.id("quantityInput")).sendKeys(String.valueOf(quantity));
        driver.findElement(By.tagName("button")).click();
    }

    @Test
    void deleteProduct_successful(ChromeDriver driver) {
        String productName = "Product To Delete";

        createTestProduct(driver, productName, 10);
        assertTrue(driver.findElement(By.tagName("body"))
                .getText().contains(productName));
        driver.findElement(By.className("btn-danger")).click();

        assertFalse(driver.findElement(By.tagName("body"))
                .getText().contains(productName));
    }

    @Test
    void deleteProduct_multipleProducts_deletesCorrectProduct(ChromeDriver driver) {

        createTestProduct(driver, "Product 1", 10);
        createTestProduct(driver, "Product 2", 20);
        createTestProduct(driver, "Product 3", 30);

        List<WebElement> initialProducts = driver.findElements(By.xpath("//tr[position()>1]"));
        int initialCount = initialProducts.size();

        List<WebElement> deleteButtons = driver.findElements(By.className("btn-danger"));
        deleteButtons.get(1).click();

        List<WebElement> remainingProducts = driver.findElements(By.xpath("//tr[position()>1]"));
        assertEquals(initialCount - 1, remainingProducts.size());

        assertFalse(driver.findElement(By.tagName("body"))
                .getText().contains("Product 2"));
        assertTrue(driver.findElement(By.tagName("body"))
                .getText().contains("Product 1"));
        assertTrue(driver.findElement(By.tagName("body"))
                .getText().contains("Product 3"));
    }

    @Test
    void deleteProduct_lastProduct_showsEmptyList(ChromeDriver driver) {

        createTestProduct(driver, "Only Product", 10);
        driver.findElement(By.className("btn-danger")).click();

        List<WebElement> remainingProducts = driver.findElements(By.xpath("//tr[position()>1]"));
        assertEquals(0, remainingProducts.size());
    }

    @Test
    void deleteProduct_preservesRemainingProductsData(ChromeDriver driver) {

        createTestProduct(driver, "Product 1", 100);
        createTestProduct(driver, "Product 2", 200);

        List<WebElement> deleteButtons = driver.findElements(By.className("btn-danger"));
        deleteButtons.getFirst().click();

        assertTrue(driver.findElement(By.tagName("body")).getText().contains("Product 2"));
        assertTrue(driver.findElement(By.tagName("body")).getText().contains("200"));
    }
}