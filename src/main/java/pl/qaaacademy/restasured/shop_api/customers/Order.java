package pl.qaaacademy.restasured.shop_api.customers;

import java.util.List;

public class Order {
    private String id;
    private String orderDate;

    private Bill bill;
    private OrderStatus orderStatus;
    private Customer customer;
    private List<OrderItem> orderItems;

    private Address billingAddress;
    private Address deliveryAddress;
}
