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

@DirtiesContext
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    String baseUrl;
    private String createOrderUrl;
    private String orderHistoryUrl;
    private String createProductUrl;

    @BeforeEach
    void setUpTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
        createOrderUrl = String.format("%s/order/create", baseUrl);
        orderHistoryUrl = String.format("%s/order/history", baseUrl);
        createProductUrl = String.format("%s/product/create", baseUrl);
    }

    @Test
    void createOrderPage_titleIsCorrect(ChromeDriver driver) {
        driver.get(createOrderUrl);
        String pageTitle = driver.getTitle();
        assertEquals("Create Order", pageTitle);
    }

    @Test
    void createOrderPage_hasCorrectElements(ChromeDriver driver) {
        driver.get(createOrderUrl);

        WebElement authorInput = driver.findElement(By.name("author"));
        assertNotNull(authorInput);

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        assertNotNull(submitButton);
    }

    @Test
    void orderHistoryPage_hasCorrectElements(ChromeDriver driver) {
        driver.get(orderHistoryUrl);

        String pageTitle = driver.getTitle();
        assertEquals("Order History", pageTitle);

        WebElement authorInput = driver.findElement(By.name("author"));
        assertNotNull(authorInput);

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        assertNotNull(submitButton);
    }

    @Test
    void createOrder_withValidData(ChromeDriver driver) {
        boolean stayInPage = true;

        driver.get(createOrderUrl);

        WebElement authorInput = driver.findElement(By.name("author"));
        authorInput.sendKeys("testUser");

        List<WebElement> productCheckboxes = driver.findElements(By.cssSelector("input[type='checkbox'][name='selectedProducts']"));
        if (!productCheckboxes.isEmpty()) {
            productCheckboxes.getFirst().click();
            stayInPage = false;
        }

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        if (!stayInPage) {
            assertTrue(driver.getCurrentUrl().contains("/order/pay"), "Should redirect to order payment page");
        } else {
            assertTrue(driver.getCurrentUrl().contains("/order/create"), "Should stay in the same page");
        }

    }

    @Test
    void orderHistory_withValidAuthor(ChromeDriver driver) {
        driver.get(createProductUrl);
        String expectedProductName = "Hatsune Miku";
        int expectedProductQuantity = 150;

        WebElement nameInputField = driver.findElement(By.id("nameInput"));
        nameInputField.sendKeys(expectedProductName);

        WebElement quantityInputField = driver.findElement(By.id("quantityInput"));
        quantityInputField.sendKeys(String.valueOf(expectedProductQuantity));

        WebElement submitButtonProduct = driver.findElement(By.tagName("button"));
        submitButtonProduct.click();

        driver.get(createOrderUrl);
        WebElement authorInput = driver.findElement(By.name("author"));
        authorInput.sendKeys("testUser");
        List<WebElement> productCheckboxes = driver.findElements(By.cssSelector("input[type='checkbox'][name='selectedProducts']"));
        if (!productCheckboxes.isEmpty()) {
            productCheckboxes.getFirst().click();
        }
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        driver.get(orderHistoryUrl);

        WebElement historyAuthorInput = driver.findElement(By.name("author"));
        historyAuthorInput.sendKeys("testUser");

        WebElement historySubmitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        historySubmitButton.click();

        List<WebElement> orderRows = driver.findElements(By.cssSelector("table tbody tr"));
        assertFalse(orderRows.isEmpty(), "Order history should display orders");
    }
}