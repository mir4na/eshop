package validator;

import java.util.Map;

public class VoucherPayment implements PaymentValidator {
    @Override
    public boolean validate(Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");
        return validateVoucherFormat(voucherCode);
    }

    private boolean validateVoucherFormat(String voucherCode) {
        return voucherCode != null && voucherCode.length() == 16 && voucherCode.startsWith("ESHOP") && countDigits(voucherCode) == 8;
    }

    private int countDigits(String str) {
        int numCount = 0;
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                numCount++;
            }
        }
        return numCount;
    }
}
