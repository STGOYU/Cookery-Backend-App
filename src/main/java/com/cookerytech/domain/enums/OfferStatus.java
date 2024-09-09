package com.cookerytech.domain.enums;

public enum OfferStatus {

    CREATED(0),
    WAITING_FOR_APPROVAL(1),
    APPROVED(2),
    REJECTED(3),
    PAID(4);

    private final int value;

    OfferStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OfferStatus fromValue(int value) {
        for (OfferStatus status : OfferStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
