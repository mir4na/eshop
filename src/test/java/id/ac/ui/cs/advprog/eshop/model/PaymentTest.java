package id.ac.ui.cs.advprog.eshop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    void testPaymentCreationWithNullData() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Payment("VOUCHER", null, this.customerOrder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Payment("COD", null, this.customerOrder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Payment("VOUCHER", this.voucherDetails, null)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Payment("COD", this.cashOnDeliveryDetails, null))
        );
    }

    @Test
    void testVoucherPaymentValidation() {
        assertAll(
                () -> {
                    Map<String, String> invalidVoucherCode = new HashMap<>();
                    invalidVoucherCode.put("voucherCode", null);
                    Payment payment = new Payment("VOUCHER", invalidVoucherCode, this.customerOrder);
                    assertEquals("REJECTED", payment.getStatus());
                },
                () -> {
                    Map<String, String> invalidVoucherPrefix = new HashMap<>();
                    invalidVoucherPrefix.put("voucherCode", "9999999999999999");
                    Payment payment = new Payment("VOUCHER", invalidVoucherPrefix, this.customerOrder);
                    assertEquals("REJECTED", payment.getStatus());
                },
                () -> {
                    Map<String, String> invalidVoucherNumbers = new HashMap<>();
                    invalidVoucherNumbers.put("voucherCode", "ESHOP9999XYZAXYZ");
                    Payment payment = new Payment("VOUCHER", invalidVoucherNumbers, this.customerOrder);
                    assertEquals("REJECTED", payment.getStatus());
                },
                () -> {
                    Map<String, String> invalidVoucherLength = new HashMap<>();
                    invalidVoucherLength.put("voucherCode", "ESHOP9999XYZ88");
                    Payment payment = new Payment("VOUCHER", invalidVoucherLength, this.customerOrder);
                    assertEquals("REJECTED", payment.getStatus());
                }
        );
    }

    @Test
    void testCODPaymentValidation() {
        assertAll(
                () -> {
                    Map<String, String> invalidCOD = new HashMap<>();
                    invalidCOD.put("address", null);
                    invalidCOD.put("deliveryFee", "20000");
                    Payment payment = new Payment("COD", invalidCOD, this.customerOrder);
                    assertEquals("REJECTED", payment.getStatus());
                },
                () -> {
                    Map<String, String> invalidCOD = new HashMap<>();
                    invalidCOD.put("address", "");
                    invalidCOD.put("deliveryFee", "20000");
                    Payment payment = new Payment("COD", invalidCOD, this.customerOrder);
                    assertEquals("REJECTED", payment.getStatus());
                },
                () -> {
                    Map<String, String> invalidCOD = new HashMap<>();
                    invalidCOD.put("address", "Jl. Anggrek Indah 7, No. 20");
                    invalidCOD.put("deliveryFee", null);
                    Payment payment = new Payment("COD", invalidCOD, this.customerOrder);
                    assertEquals("REJECTED", payment.getStatus());
                },
                () -> {
                    Map<String, String> invalidCOD = new HashMap<>();
                    invalidCOD.put("address", "Jl. Anggrek Indah 7, No. 20");
                    invalidCOD.put("deliveryFee", "");
                    Payment payment = new Payment("COD", invalidCOD, this.customerOrder);
                    assertEquals("REJECTED", payment.getStatus());
                }
        );
    }

    @Test
    void testPaymentMethodAndStatusValidation() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Payment("INVALID_METHOD", this.voucherDetails, this.customerOrder)),
                () -> {
                    Payment paymentVoucher = new Payment("VOUCHER", this.voucherDetails, this.customerOrder);
                    assertThrows(IllegalArgumentException.class,
                            () -> paymentVoucher.setStatus("BARK"));
                },
                () -> {
                    Payment paymentVoucher = new Payment("VOUCHER", this.voucherDetails, this.customerOrder);
                    paymentVoucher.setStatus("REJECTED");
                    assertEquals("REJECTED", paymentVoucher.getStatus());
                }
        );
    }

    @Test
    void testSuccessfulPaymentCreation() {
        assertAll(
                () -> {
                    Payment voucherPayment = new Payment("VOUCHER", this.voucherDetails, this.customerOrder);
                    assertEquals("SUCCESS", voucherPayment.getStatus());
                    assertSame(this.voucherDetails, voucherPayment.getPaymentData());
                    assertSame(this.customerOrder, voucherPayment.getOrder());
                    assertNotNull(voucherPayment.getId());
                },
                () -> {
                    Payment codPayment = new Payment("COD", this.cashOnDeliveryDetails, this.customerOrder);
                    assertEquals("SUCCESS", codPayment.getStatus());
                    assertSame(this.cashOnDeliveryDetails, codPayment.getPaymentData());
                    assertSame(this.customerOrder, codPayment.getOrder());
                    assertNotNull(codPayment.getId());
                }
        );
    }
}