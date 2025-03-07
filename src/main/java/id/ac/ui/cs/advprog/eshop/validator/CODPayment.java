package id.ac.ui.cs.advprog.eshop.validator;

import java.util.Map;

public class CODPayment implements PaymentValidator {
    @Override
    public boolean validate(Map<String, String> paymentData) {
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");
        return !(address == null) && !address.isEmpty() && !(deliveryFee == null) && !deliveryFee.isEmpty();
    }
}