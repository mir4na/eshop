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
class PaymentFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;
    private String paymentDetailUrl;
    private String paymentAdminListUrl;

    @BeforeEach
    void setUpTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
        paymentDetailUrl = String.format("%s/payment/detail", baseUrl);
        paymentAdminListUrl = String.format("%s/payment/admin/list", baseUrl);
    }

    @Test
    void paymentDetailPage_titleIsCorrect(ChromeDriver driver) {
        driver.get(paymentDetailUrl);
        String pageTitle = driver.getTitle();
        assertEquals("Payment Detail", pageTitle);
    }

    @Test
    void paymentDetailPage_hasCorrectElements(ChromeDriver driver) {
        driver.get(paymentDetailUrl);

        WebElement paymentIdInput = driver.findElement(By.name("paymentId"));
        assertNotNull(paymentIdInput);

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        assertNotNull(submitButton);
    }

    @Test
    void paymentAdminList_displaysPayments(ChromeDriver driver) {
        driver.get(paymentAdminListUrl);

        String pageTitle = driver.getTitle();
        assertEquals("Payment Admin List", pageTitle);

        List<WebElement> paymentTableHeaders = driver.findElements(By.cssSelector("table thead th"));
        assertFalse(paymentTableHeaders.isEmpty(), "Payments table should have headers");

        assertTrue(paymentTableHeaders.stream().anyMatch(header ->
                header.getText().toLowerCase().contains("payment id") ||
                        header.getText().toLowerCase().contains("method") ||
                        header.getText().toLowerCase().contains("status")
        ), "Table should have expected headers");
    }

    @Test
    void paymentAdminList_hasViewDetailsButtons(ChromeDriver driver) {
        driver.get(paymentAdminListUrl);

        List<WebElement> viewDetailsButtons = driver.findElements(By.cssSelector("a.btn-primary"));
        assertFalse(viewDetailsButtons.isEmpty(), "Should have view details buttons");
    }

    @Test
    void paymentDetailSubmission_withValidId(ChromeDriver driver) {
        driver.get(paymentDetailUrl);

        createOrderAndPayment(driver);

        WebElement paymentIdInput = driver.findElement(By.name("paymentId"));
        paymentIdInput.sendKeys("test-payment-id"); // Replace with a valid payment ID

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        assertTrue(driver.getCurrentUrl().contains("/payment/detail/"), "Should show payment details");
    }

    private void createOrderAndPayment(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");

        WebElement authorInput = driver.findElement(By.name("author"));
        authorInput.sendKeys("testUser");

        List<WebElement> productCheckboxes = driver.findElements(By.cssSelector("input[type='checkbox'][name='selectedProducts']"));
        if (!productCheckboxes.isEmpty()) {
            productCheckboxes.get(0).click();
        }

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        WebElement paymentMethodRadio = driver.findElement(By.cssSelector("input[name='paymentMethod'][value='VOUCHER']"));
        paymentMethodRadio.click();

        WebElement paymentSubmitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        paymentSubmitButton.click();
    }
}