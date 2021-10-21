package pl.qaaacademy.restasured.shop_api.customers;

import java.util.List;

public class Customer {
    private Address address;
    private CustomerStatus customerStatus;

    private String id;
    private String password;

    private Person person;
    private ShoppingCart shoppingCart;
    private List<Order> order;

    public Customer() {
    }
}
