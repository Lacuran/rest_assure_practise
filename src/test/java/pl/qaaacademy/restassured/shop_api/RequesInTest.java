package pl.qaaacademy.restassured.shop_api;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasItems;

public class RequesInTest {

    private static String targetGetPathWithParams = "https://reqres.in/api/users?page=2";

    @BeforeTest
    public void setup(){
        baseURI = "https://reqres.in";
    }

    @Test
    public void shouldGetUsersIds(){
        when().get(baseURI + "/api/users?page=2")
                .then().log().all().body("data*.id", hasItems(7,8));
    }

    @Test
    public void shouldReturnUsersWithRequestSpecification(){
        /*ResponseSpecBuilder builder  = new ResponseSpecBuilder();
        builder.expectContentType(ContentType.JSON);
        builder.expectHeader("Connection","keep-alive");
        builder.expectBody("data[2].email", equalTo("tobias.funke@reqres.in"));
        ResponseSpecification spec = builder.build();
        when().get(targetGetPathWithParams)
                .then()
                .spec(spec); // may be in given()*/
    }
    //TODO
    // how to pass query paramiters in rest assured
}
