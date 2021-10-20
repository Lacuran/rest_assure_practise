package pl.qaaacademy.restassured.shop_api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BasicProductAPIVerificationTest {

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
        //TODO
        String hasPeach = "Peach";
        String hasStrawberry = "Strawberry";

        when().get(baseURI + basePath)
                .then().log().all()
                .assertThat()
                .body("description", hasItems(hasPeach, hasStrawberry));

    }

    @Test
    public void shouldVerifyStrawberryPrice(){
        //TODO
        Float expectedPrice = 18.3f;
        String strawberryID = "/5";

        when().get(baseURI + basePath + strawberryID)
                .then()
                .log().all()
                .assertThat()
                .body("price", equalTo(expectedPrice));


    }

    @Test
    public void shouldDeleteCoffeeProduct(){
        //TODO
        String coffeeID = given().contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"description\": \"Coffee\",\n" +
                        "    \"id\": \"\",\n" +
                        "    \"manufacturer\": 4,\n" +
                        "    \"price\": 31.2\n" +
                        "}").log().body()
                .when()
                .post(baseURI+basePath)
                .then()
                .extract()
                .path("id").toString();

        when().get(baseURI + basePath)
                .then().log().all()
                .assertThat()
                .statusCode(200);

        when().delete(baseURI + basePath + "/" +coffeeID)
                .then()
                .assertThat()
                .statusCode(200);

        when().get(baseURI + basePath)
                .then().log().all()
                .assertThat()
                .statusCode(200);

    }
}
