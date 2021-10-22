package pl.qaaacademy.restassured.shop_api;

import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pl.qaaacademy.restasured.shop_api.customers.Product;

import java.util.HashMap;
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
//                .path("id").toString();

        when().delete(baseURI + basePath + SEPARATOR + coffeeID)
                .then()
                .statusCode(200)
                .body(equalTo(String.valueOf(true)));

        /*String allID = given()
                .when().get(baseURI + basePath)
                .then().contentType(ContentType.JSON)
                .extract().path("id").toString();
        System.out.println(allID);

        String[] arrayOFAllID = allID.split(", ");

        String coffeeID = "";

        for (String iDs : arrayOFAllID) {
            if (iDs.contains("]") && iDs.length() > 10){
                coffeeID = iDs.replace("]","");
            } else if (iDs.length() > 10){
                coffeeID = iDs;
            }
        }
        System.out.println(coffeeID);

        when().delete(baseURI + basePath + "/" + coffeeID)
                .then().log().all()
                .assertThat()
                .statusCode(200);*/
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
}
