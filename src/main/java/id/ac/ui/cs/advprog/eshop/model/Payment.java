package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.validator.CODPayment;
import id.ac.ui.cs.advprog.eshop.validator.VoucherPayment;
import lombok.Getter;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

@Getter
public class Payment {
    private final String id;
    private final String method;
    private final Map<String, String> paymentData;
    private final Order order;
    private String status;
    private final VoucherPayment voucherPaymentValidator = new VoucherPayment();
    private final CODPayment codPaymentValidator = new CODPayment();

    public Payment(String method, Map<String, String> paymentData, Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        if (paymentData == null) {
            throw new IllegalArgumentException("Payment data cannot be null");
        }

        this.id = UUID.randomUUID().toString();
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
        if (voucherPaymentValidator.validate(this.paymentData)) {
            this.status = PaymentStatus.SUCCESS.getValue();
        } else {
            this.status = PaymentStatus.REJECTED.getValue();
        }
    }

    private void validateCODPayment() {
        if (codPaymentValidator.validate(this.paymentData)) {
            this.status = PaymentStatus.SUCCESS.getValue();
        } else {
            this.status = PaymentStatus.REJECTED.getValue();
        }
    }

    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid payment status");
        }
    }
}
