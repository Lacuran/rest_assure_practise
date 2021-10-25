package pl.qaaacademy.restassured.shop_api;

import org.testng.Assert;
import org.testng.annotations.Test;
import pl.qaaacademy.restasured.shop_api.models.Customer;
import pl.qaaacademy.restasured.shop_api.models.OrderItem;
import pl.qaaacademy.restasured.shop_api.models.Product;

import java.util.Arrays;

import static io.restassured.RestAssured.*;

public class CustomerComplexTest {
    private final String baseURL = "http://localhost:3000/customers";
    private final String addItemToCartPath = "http://localhost:3000/customers/{customerId}/cart" +
            "?quantity={quantity}&productId={productId}";
    private final String SEPARATOR = "/";

    @Test
    public void shouldGetCustomerAddressFromExtractedObject(){
        Customer customer = when().get(baseURL + SEPARATOR + "3").as(Customer.class);
        Assert.assertEquals(customer.getShoppingCart().getItems().length, 0);
    }

    @Test
    public void shouldVerifyItemsInShoppingCart(){
        when().put(baseURL + "/2/cart?quantity=5&productId=2")
                .then()
                .statusCode(200);

        Customer customer = when().get(baseURL + SEPARATOR + "2").as(Customer.class);

        String actualItem = Arrays.stream(customer.getShoppingCart().getItems())
                .map(OrderItem::getProduct)
                .map(Product::getDescription)
                .findAny().get();


        Assert.assertEquals(actualItem, "Banana");
    }

    @Test
    public void customerShoppingCartHaveExpectedLength(){
        String customerId = "3";
        emptyCartForCustomer(customerId);
        putProductToCartForCustomers(customerId,2,2);
        Customer customer = when().get(baseURL + SEPARATOR + customerId).as(Customer.class);

        Assert.assertEquals(customer.getShoppingCart().getItems().length, 1);
    }

    @Test
    public void shouldVerifyEmptyCartAfterAddingItem(){
        String customerId = "2";
        putProductToCartForCustomersForCustomerPathPatch(customerId, 2, 100);
        emptyCartForCustomer(customerId);
        Customer customer = when().get(baseURL + SEPARATOR + customerId).as(Customer.class);

        Assert.assertEquals(customer.getShoppingCart().getItems().length, 0);
    }

    private void emptyCartForCustomer(String customerId){
        when().delete(baseURL + SEPARATOR + customerId + SEPARATOR +"cart/empty")
                .then()
                .statusCode(200);
    }

    private void putProductToCartForCustomers(String customerId, int productId, int productQuantity){
        String query = baseURL + SEPARATOR + customerId + SEPARATOR + "cart";
        given().queryParam("quantity", productQuantity)
                .queryParam("productId",productId)
                .when().put(query)
                .then().log().all();
    }

    private void putProductToCartForCustomersForCustomerPathPatch(String customerId, int productId, int productQuantity){
        given().pathParam("customerId", customerId)
                .pathParam("quantity", productQuantity)
                .pathParam("productId", productId)
                .when().put(addItemToCartPath)
                .then().log().all();
    }
}
