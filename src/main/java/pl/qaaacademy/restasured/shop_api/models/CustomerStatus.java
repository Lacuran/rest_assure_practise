package pl.qaaacademy.restasured.shop_api.models;

public enum CustomerStatus {
    NEW(0),
    ACTIVE(1),
    BLOCKER(2),
    RETIRED(3);

    private final int customerStatus;

    CustomerStatus(int customerStatus) {
        this.customerStatus = customerStatus;
    }
}
