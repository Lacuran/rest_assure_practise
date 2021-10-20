package pl.qaaacademy.restassured.shop_api;

import io.restassured.http.ContentType;
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
        when().get(baseURI + basePath)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void shouldReturnCustomerInfoForIDEquals2(){
        when().get("http://localhost:3000/customers/2")
                .then()
                .statusCode(200)
                .body("person.email", is(equalTo("john.doe@customDomain.com")));
    }

    @Test
    public void shouldGetPhoneVerifyForCustomer2(){
        when().get("http://localhost:3000/customers/2")
                .then()
                .statusCode(200)
                .body("address.phone", is(equalTo("33 55 789 123")));
    }
}
