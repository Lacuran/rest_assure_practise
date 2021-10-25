package pl.qaaacademy.restasured.shop_api.apis;

import io.restassured.http.ContentType;
import pl.qaaacademy.restasured.shop_api.enviroments.Environment;
import pl.qaaacademy.restasured.shop_api.models.Product;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;

public class ProductAPI {
    
    private final String HOST;
    private final Environment env;
    
    private final String PRODUCTS = "/products";
    private final String SEPARATOR = "/";

    public ProductAPI(Environment env) {
        this.env = env;
        HOST = env.getHost();
    }

    public static ProductAPI get(Environment env) {
        return new ProductAPI(env);
    }
    
    public List<Product> getAllProduct(){
        String query = HOST + PRODUCTS;
        return when().get(query)
                .then().log().body()
                .extract().body().jsonPath().getList("", Product.class);
    }

    public void addNewProduct(String description, String id, int manufacturer, float price) {
        Product product = new Product(description, id, manufacturer, price);
        String query = HOST + PRODUCTS;
        given().contentType(ContentType.JSON)
                .body(product)
                .when().post(query)
                .then().log().body();
    }

    public String getProductID(String description) {
        String path = String.format("findAll {it.description == '%s'}", description);
        String query = HOST + PRODUCTS;
        List<Product> product = given()
                .when().get(query)
                .then()
                .extract().body().jsonPath().getList(path, Product.class);

        return product.stream()
                .map(Product::getId)
                .findFirst()
                .get();
    }

    public void updateProductByID(String description, String id, int manufacturer, Float newPrice) {
        Product product = new Product(description, id, manufacturer, newPrice);

        given().contentType(ContentType.JSON)
                .body(product)
                .when().put(HOST + PRODUCTS + SEPARATOR + id)
                .then().log().body();
    }


    public String getProductID(String description, int manufacturer) {
        String path = String.format("findAll {it.description == '%s'}", description);
        String query = HOST + PRODUCTS;

        List<Product> product = given()
                .when().get(query)
                .then()
                .extract().body().jsonPath().getList(path, Product.class);

        return product.stream()
                .filter(p -> p.getManufacturer() == manufacturer)
                .map(Product::getId)
                .findFirst()
                .get();
    }

    public float getProductPrice(String description) {
        String path = String.format("findAll {it.description == '%s'}", description);
        String query = HOST + PRODUCTS;

        List<Product> product = given()
                .when().get(query)
                .then()
                .extract().body().jsonPath().getList(path, Product.class);

        return product.stream()
                .map(Product::getPrice)
                .findFirst()
                .get();
    }

    public float getProductPrice(String description, int manufacturer) {
        String path = String.format("findAll {it.description == '%s'}", description);
        String query = HOST + PRODUCTS;

        List<Product> product = given()
                .when().get(query)
                .then()
                .extract().body().jsonPath().getList(path, Product.class);

        return product.stream()
                .filter(p -> p.getManufacturer() == manufacturer)
                .map(Product::getPrice)
                .findFirst()
                .get();
    }
}
