package id.ac.ui.cs.advprog.eshop.validator;

import java.util.Map;

public interface PaymentValidator {
    boolean validate(Map<String, String> paymentData);
}