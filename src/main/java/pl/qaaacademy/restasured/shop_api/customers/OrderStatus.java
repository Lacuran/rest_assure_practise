package pl.qaaacademy.restasured.shop_api.customers;

public enum OrderStatus {
    NEW(0),
    PROCESSING(1),
    SHIPPING(2),
    DELIVERED(4),
    CANCELED(8);

    private final int statusOrder;

    OrderStatus(int statusOrder) {
        this.statusOrder = statusOrder;
    }
}
