package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import enums.PaymentStatus;
import enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

@Getter
public class Payment {
    private final String id;
    private final String method;
    private final Map<String, String> paymentData;
    private final Order order;
    private String status;

    public Payment(String method, Map<String, String> paymentData, Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        if (paymentData == null) {
            throw new IllegalArgumentException("Payment data cannot be null");
        }

        this.id = UUID.randomUUID().toString(); // Generate UUID as String
        this.order = order;
        this.paymentData = paymentData;

        if (!PaymentMethod.contains(method)) {
            throw new IllegalArgumentException("Invalid payment method");
        }
        this.method = method;

        validatePaymentData();
    }

    private void validatePaymentData() {
        if (this.method.equals(PaymentMethod.VOUCHER.getValue())) {
            validateVoucherPayment();
        } else if (this.method.equals(PaymentMethod.COD.getValue())) {
            validateCODPayment();
        }
    }

    private void validateVoucherPayment() {
        String voucherCode = this.paymentData.get("voucherCode");

        if (voucherCode == null) {
            throw new IllegalArgumentException("Voucher code cannot be null");
        }

        if (isValidVoucher(voucherCode)) {
            this.status = PaymentStatus.SUCCESS.getValue();
        } else {
            this.status = PaymentStatus.REJECTED.getValue();
        }
    }

    private boolean isValidVoucher(String voucherCode) {
        if (voucherCode.length() != 16) {
            throw new IllegalArgumentException("Voucher code must be 16 characters long");
        }

        if (!voucherCode.startsWith("ESHOP")) {
            throw new IllegalArgumentException("Voucher code must start with 'ESHOP'");
        }

        int numCount = 0;
        for (char c : voucherCode.toCharArray()) {
            if (Character.isDigit(c)) {
                numCount++;
            }
        }

        if (numCount != 8) {
            throw new IllegalArgumentException("Voucher code must contain exactly 8 digits");
        }

        return true;
    }

    private void validateCODPayment() {
        String address = this.paymentData.get("address");
        String deliveryFee = this.paymentData.get("deliveryFee");

        if (address == null || address.isEmpty() || deliveryFee == null || deliveryFee.isEmpty()) {
            throw new IllegalArgumentException("Address and delivery fee cannot be null or empty");
        }

        this.status = PaymentStatus.SUCCESS.getValue();
    }

    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid payment status");
        }
    }
}