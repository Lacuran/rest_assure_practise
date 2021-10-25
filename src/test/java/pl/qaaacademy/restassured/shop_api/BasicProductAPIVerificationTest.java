package pl.qaaacademy.restassured.shop_api;

import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pl.qaaacademy.restasured.shop_api.models.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BasicProductAPIVerificationTest {

    private final String SEPARATOR = "/";

    @BeforeTest
    public void setup(){
        baseURI = "http://localhost:3000";
        basePath = "/products";
    }

    @Test
    public void shouldAddNewProduct(){
        given().contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"description\": \"Coffee\",\n" +
                        "    \"id\": \"\",\n" +
                        "    \"manufacturer\": 4,\n" +
                        "    \"price\": 31.2\n" +
                        "}").log().body()
                .when().post(baseURI + basePath)
                .then().log().all()
                .statusLine(containsString("OK"));

    }

    @Test
    public void shouldUpdateProductByID(){
        Float newPrice = 9.3f;
        HashMap<String, Object> productData = new HashMap<>();
        productData.put("description", "Banana");
        productData.put("id", "2");
        productData.put("manufacturer", 1);
        productData.put("price", newPrice);

        given().contentType(ContentType.JSON)
                .body(productData)
                .when().put(baseURI + basePath + "/2")
                .then().log().all()
                .body("price", equalTo(newPrice));
    }

    @Test
    public void shouldGetAllProducts(){
        when().get(baseURI + basePath)
                .then().log().all()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void shouldContainPeachAndStrawberry(){
        String hasPeach = "Peach";
        String hasStrawberry = "Strawberry";

        when().get(baseURI + basePath)
                .then().log().all()
                .assertThat()
                .body("description", hasItems(hasPeach, hasStrawberry));

    }

    @Test
    public void shouldVerifyStrawberryPrice(){
        Float expectedPrice = 18.3f;
        String strawberryID = "5";
        String pricePath = "price";

        when().get(baseURI + basePath + SEPARATOR + strawberryID)
                .then()
                .log().all()
                .assertThat()
                .body(pricePath, equalTo(expectedPrice));


    }

    @Test
    public void shouldDeleteCoffeeProduct(){
        String coffeeID = given().contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"description\": \"Coffee\",\n" +
                        "    \"id\": \"\",\n" +
                        "    \"manufacturer\": 4,\n" +
                        "    \"price\": 31.2\n" +
                        "}").log().body()
                .when()
                .post(baseURI+basePath)
                .then().log().all()
                .extract().body().jsonPath().get("id");


        when().delete(baseURI + basePath + SEPARATOR + coffeeID)
                .then()
                .statusCode(200)
                .body(equalTo(String.valueOf(true)));

    }

    @Test
    public void extractedProductShouldHaveExpectedDescription(){
        String productID = "3";
        String expectedDescription = "Grapes";

        Product grapes = given()
                .when().get(baseURI + basePath + SEPARATOR + productID)
                .then().extract().body().as(Product.class);

        Assert.assertEquals(grapes.getDescription(), expectedDescription);
    }

    @Test
    public void extractedProductShouldHaveExpectedPriceAndDescription(){
        String productID = "7";
        String expectedDescription = "Orange";
        float expectedPrice = 10.5f;

        Product orange = given()
                .when().get(baseURI + basePath + SEPARATOR + productID)
                .then().extract().body().as(Product.class);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(orange.getDescription(), expectedDescription);
        softAssert.assertEquals(orange.getPrice(), expectedPrice);
        softAssert.assertAll();

    }

    @Test
    public void shouldReturnProductAsAHashMap(){
        String productId = "6";
        Map<String, String> map = given()
                .when().get(baseURI + basePath + SEPARATOR + productId)
                .then().extract().body().jsonPath().getMap("", String.class, String.class);
        System.out.println(map.get("description"));
        System.out.println(map);

    }

    @Test
    public void shouldCreateProductUsingClassInstance(){
        Product tea = new Product("Tea", "",69, 420.69f);

        given().contentType(ContentType.JSON)
                .body(tea)
                .when().post()
                .then().log().all()
                .statusCode(200);
    }

    @Test
    public void shouldGetItemBySearchingItAndDelete(){
        List<Product> product = given()
                .when().get(baseURI + basePath)
                .then().extract().body().jsonPath().getList("findAll {it.description == 'Tea'}", Product.class);

        String teaId = product.stream()
                .map(Product::getId)
                .findFirst()
                .get();

        System.out.println(teaId);

        when().delete(SEPARATOR + teaId)
                .then().log().all()
                .statusCode(200)
                .body(equalTo(String.valueOf(true)));
    }

    @Test
    public void shouldGetListOfProducts(){
        int expectedSize = 15;
        List<Product> product = given()
                .when().get(baseURI + basePath)
                .then().extract().body().jsonPath().getList("",Product.class);

        product.forEach(System.out::println);

        Assert.assertEquals(product.size(),expectedSize);
    }
}
