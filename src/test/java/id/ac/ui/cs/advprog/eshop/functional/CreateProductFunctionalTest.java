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

@DirtiesContext
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public class CreateProductFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setUpTest() {
        baseUrl = String.format("%s:%d/product/create", testBaseUrl, serverPort);
    }

    @Test
    void pageTitle_isCorrect(ChromeDriver driver) {
        driver.get(baseUrl);
        String pageTitle = driver.getTitle();
        assertEquals("Create New Product", pageTitle);
    }

    @Test
    void message_createProduct_isCorrect(ChromeDriver driver) {
        driver.get(baseUrl);
        String message = driver.findElement(By.tagName("h3")).getText();
        assertEquals("Create New Product", message);
    }

    @Test
    void createProduct_isCorrect(ChromeDriver driver) {
        driver.get(baseUrl);
        String expectedProductName = "Hatsune Miku";
        int expectedProductQuantity = 150;

        WebElement nameInputField = driver.findElement(By.id("nameInput"));
        nameInputField.sendKeys(expectedProductName);

        WebElement quantityInputField = driver.findElement(By.id("quantityInput"));
        quantityInputField.sendKeys(String.valueOf(expectedProductQuantity));

        WebElement submitButton = driver.findElement(By.tagName("button"));
        submitButton.click();

        String currentUrl = driver.getCurrentUrl();
        assertEquals(String.format("%s:%d/product/list", testBaseUrl, serverPort), currentUrl);

        boolean isProductNameDisplayed = !driver.findElements(By.xpath("//*[contains(text(), '" + expectedProductName + "')]")).isEmpty();
        boolean isProductQuantityDisplayed = !driver.findElements(By.xpath("//*[contains(text(), '" + expectedProductQuantity + "')]")).isEmpty();

        assertTrue(isProductNameDisplayed);
        assertTrue(isProductQuantityDisplayed);
    }
}