package validator;

import java.util.Map;

public interface PaymentValidator {
    boolean validate(Map<String, String> paymentData);
}