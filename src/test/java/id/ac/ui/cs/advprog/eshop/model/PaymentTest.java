package id.ac.ui.cs.advprog.eshop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentTest {
    private Map<String, String> voucherDetails;
    private Map<String, String> cashOnDeliveryDetails;
    private Order customerOrder;
    List<Product> itemList;

    @BeforeEach
    void setUp() {
        this.voucherDetails = new HashMap<>();
        this.voucherDetails.put("voucherCode", "ESHOP9999XYZ8888");
        this.cashOnDeliveryDetails = new HashMap<>();
        this.cashOnDeliveryDetails.put("address", "Jl. Anggrek Indah 7, No. 20");
        this.cashOnDeliveryDetails.put("deliveryFee", "20000");
        this.itemList = new ArrayList<>();
        Product item1 = new Product();
        item1.setProductId("xy123456-7890-1234-5678-abcdef654321");
        item1.setProductName("Conditioner Cap Rudi");
        item1.setProductQuantity(4);
        Product item2 = new Product();
        item2.setProductId("yz654321-0987-5432-1098-bcdefa123456");
        item2.setProductName("Body Wash Cap Andi");
        item2.setProductQuantity(3);
        this.itemList.add(item1);
        this.itemList.add(item2);
        this.customerOrder = new Order("98765432-10ab-cdef-1234-567890abcdef", this.itemList,
                1708560000L, "Andi Wijaya");
    }

    @Test
    void testPaymentCreationWithoutData() {
        assertThrows(IllegalArgumentException.class,
                () -> new Payment("VOUCHER", null, this.customerOrder));
    }

    @Test
    void testVoucherPaymentWithInvalidDetails() {
        Map<String, String> invalidVoucherMissingPrefix = new HashMap<>();
        Map<String, String> invalidVoucherMissingNumbers = new HashMap<>();
        Map<String, String> invalidVoucherTooShort = new HashMap<>();
        Map<String, String> invalidVoucherTooLong = new HashMap<>();
        invalidVoucherMissingPrefix.put("voucherCode", "9999XYZ8888");
        invalidVoucherMissingNumbers.put("voucherCode", "ESHOP9999XYZ");
        invalidVoucherTooShort.put("voucherCode", "ESHOP9999XYZ88");
        invalidVoucherTooLong.put("voucherCode", "ESHOP9999XYZ88889999");
        assertThrows(IllegalArgumentException.class,
                () -> new Payment("VOUCHER", invalidVoucherMissingPrefix, this.customerOrder));
        assertThrows(IllegalArgumentException.class,
                () -> new Payment("VOUCHER", invalidVoucherMissingNumbers, this.customerOrder));
        assertThrows(IllegalArgumentException.class,
                () -> new Payment("VOUCHER", invalidVoucherTooShort, this.customerOrder));
        assertThrows(IllegalArgumentException.class,
                () -> new Payment("VOUCHER", invalidVoucherTooLong, this.customerOrder));
    }

    @Test
    void testCashOnDeliveryWithInvalidDetails() {
        Map<String, String> paymentMissingAddress = new HashMap<>();
        Map<String, String> paymentMissingFee = new HashMap<>();
        Map<String, String> paymentEmptyAddress = new HashMap<>();
        Map<String, String> paymentEmptyFee = new HashMap<>();
        paymentMissingAddress.put("deliveryFee", "20000");
        paymentMissingFee.put("address", "Jl. Anggrek Indah 7, No. 20");
        paymentEmptyAddress.put("address", "");
        paymentEmptyAddress.put("deliveryFee", "20000");
        paymentEmptyFee.put("deliveryFee", "");
        paymentEmptyFee.put("address", "Jl. Anggrek Indah 7, No. 20");
        assertThrows(IllegalArgumentException.class,
                () -> new Payment("COD", paymentMissingAddress, this.customerOrder));
        assertThrows(IllegalArgumentException.class,
                () -> new Payment("COD", paymentMissingFee, this.customerOrder));
        assertThrows(IllegalArgumentException.class,
                () -> new Payment("COD", paymentEmptyAddress, this.customerOrder));
        assertThrows(IllegalArgumentException.class,
                () -> new Payment("COD", paymentEmptyFee, this.customerOrder));
    }

    @Test
    void testPaymentCreationWithoutOrder() {
        assertThrows(IllegalArgumentException.class,
                () -> new Payment("VOUCHER", this.voucherDetails, null));
    }

    @Test
    void testPaymentCreationWithInvalidMethod() {
        assertThrows(IllegalArgumentException.class,
                () -> new Payment("BARK", this.voucherDetails, this.customerOrder));
    }

    @Test
    void testIdIsGeneratedAutomatically() {
        Payment payment = new Payment("VOUCHER", this.voucherDetails, this.customerOrder);
        assertNotNull(payment.getId());
    }

    @Test
    void testSetInvalidPaymentStatus() {
        Payment payment = new Payment("VOUCHER", this.voucherDetails, this.customerOrder);
        assertThrows(IllegalArgumentException.class,
                () -> payment.setStatus("BARK"));
    }

    @Test
    void testSuccessfulVoucherPayment() {
        Payment payment = new Payment("VOUCHER", this.voucherDetails, this.customerOrder);
        assertEquals("SUCCESS", payment.getStatus());
        assertSame(this.voucherDetails, payment.getPaymentData());
        assertSame(this.customerOrder, payment.getOrder());
    }

    @Test
    void testSuccessfulCashOnDeliveryPayment() {
        Payment payment = new Payment("COD", this.cashOnDeliveryDetails, this.customerOrder);
        assertEquals("SUCCESS", payment.getStatus());
        assertSame(this.cashOnDeliveryDetails, payment.getPaymentData());
        assertSame(this.customerOrder, payment.getOrder());
    }
}