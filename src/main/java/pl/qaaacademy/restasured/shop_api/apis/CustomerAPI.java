package pl.qaaacademy.restasured.shop_api.apis;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import pl.qaaacademy.restasured.shop_api.enviroments.Environment;
import pl.qaaacademy.restasured.shop_api.models.Customer;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class CustomerAPI {

    private final String HOST;
    private final Environment env;
    private RequestSpecification reqSpec;

    private final String CUSTOMERS = "/customers";
    private final String CHANGE_EMAIL_ENDPOINT = "/email";
    private final String ADD_TO_CART = "/cart";
    private final String SEPARATOR = "/";

    public CustomerAPI(Environment env) {
        this.env = env;
        HOST = env.getHost();
    }

    public static CustomerAPI get(Environment env){
        return new CustomerAPI(env);
    }

    public List<Customer> getAllCustomers(){
        String query = HOST + CUSTOMERS;
        return when().get(query)
                .then().log().body()
                .extract().body().jsonPath().getList("", Customer.class);
    }

    private void requestSetUp(){
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setAccept(ContentType.JSON);
        builder.addCookie("cookie1", "key1=value1");
        builder.addHeader("usefulHeader", "veryUsefulValue");

        reqSpec = builder.build();
    }

    public Customer updateEmailAddressForCustomer(String customerId, String newEmail) {
        String address = HOST + CUSTOMERS + SEPARATOR + customerId + CHANGE_EMAIL_ENDPOINT;
        Customer alex = given()
                .queryParam("email", newEmail)
                .when().patch(address)
                .then().log().all()
                .extract().body().as(Customer.class);
        return alex;
    }

    public void addItemToCartForCustomer(String customerId, String itemId, String quantity) {
        String address = HOST + CUSTOMERS + SEPARATOR + customerId + ADD_TO_CART;
        given().queryParam("quantity", quantity)
                .queryParam("productId", itemId)
                .when().put(address)
                .then().log().all();
    }

    public Customer getCustomerById(String customerId) {
        return when().get(HOST + CUSTOMERS + SEPARATOR + customerId)
                .then().log().all()
                .extract().body().as(Customer.class);
    }
}
