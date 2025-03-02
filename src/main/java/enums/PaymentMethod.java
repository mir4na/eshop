package enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    COD("COD"),
    VOUCHER("VOUCHER");

    private final String value;
    PaymentMethod(String value) {
        this.value = value;
    }

    public static boolean contains(String param) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.value.equals(param)) {
                return true;
            }
        }
        return false;
    }
}