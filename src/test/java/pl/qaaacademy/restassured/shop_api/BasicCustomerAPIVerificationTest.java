package pl.qaaacademy.restassured.shop_api;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BasicCustomerAPIVerificationTest {

    @BeforeTest
    public void setup(){
        baseURI = "http://localhost:3000";
        basePath = "/customers";
    }

    @Test
    public void shouldReturn200StatusCodeWhenFetchingCustomersList(){
        Header h1 = new Header("h1","v1");
        given().header(h1).log().headers()
                .when().get(baseURI + basePath)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void shouldReturnCustomerInfoForIDEquals2(){
        Response response = given().cookie("cookie1","key1/value1,key2/value2").log().cookies()
                .when().get(baseURI + basePath + "/2")
                .then().extract().response();

        System.out.println(response.getBody().prettyPrint());
        System.out.println(response.getStatusCode());
        System.out.println(response.getStatusLine());
        System.out.println(response.getContentType());
        System.out.println(response.getHeaders().asList());
        System.out.println(response.getCookies());
//                .statusCode(200)
//                .body("person.email", is(equalTo("john.doe@customDomain.com")));
    }

    @Test
    public void shouldGetPhoneVerifyForCustomer2(){
        when().get("http://localhost:3000/customers/2")
                .then()
                .statusCode(200)
                .body("address.phone", is(equalTo("33 55 789 123")));
    }

    @Test
    public void shouldVerifyCityForCustomerId3(){
        String baseQuery = "find {it.id == '%s'}.%s";
        String query = String.format(baseQuery, "3", "address.city");
        String expectedCity = "Auckland";

        given()
                .when().get(baseURI + basePath)
                .then().log().all()
                .body(query,equalTo(expectedCity));
    }

}
