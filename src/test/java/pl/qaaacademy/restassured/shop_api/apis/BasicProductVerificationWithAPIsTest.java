package pl.qaaacademy.restassured.shop_api.apis;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.qaaacademy.restasured.shop_api.apis.ProductAPI;
import pl.qaaacademy.restasured.shop_api.enviroments.Environment;
import pl.qaaacademy.restasured.shop_api.enviroments.EnvironmentManager;
import pl.qaaacademy.restasured.shop_api.models.Product;

import java.util.List;

public class BasicProductVerificationWithAPIsTest {
    // TODO: 25.10.2021
    //  refactor all test from basic product test

    private ProductAPI productAPI;

    @BeforeClass
    public void setUp(){
        String env = System.getProperty("env");
        Environment currentEnv = EnvironmentManager.getEnvironment(env);
        productAPI = ProductAPI.get(currentEnv);
    }

    @Test
    public void shouldAddNewProduct(){
        String description = "Coffee";
        String id = "";
        int manufacturer = 4;
        float price = 420.69f;
        int expectedSize = 15;

        productAPI.addNewProduct(description, id, manufacturer, price);

        List<Product> productList = productAPI.getAllProduct();

        Assert.assertEquals(productList.size(), expectedSize);
    }

    @Test
    public void shouldUpdateProductById(){
        Float newPrice = 60.43f;
        String description = "Coffee";
        String id = productAPI.getProductID(description);
        int manufacturer = 3;

        productAPI.updateProductByID(description, id, manufacturer, newPrice);

        Float productPrice = productAPI.getProductPrice(description);

        Assert.assertEquals(productPrice, newPrice);
    }

    @Test
    public void shouldUpdateProductByItsIdAndManufacturer() {
        Float newPrice = 46.43f;
        String description = "Coffee";
        int manufacturer = 4;
        String id = productAPI.getProductID(description, manufacturer);

        productAPI.updateProductByID(description, id, manufacturer, newPrice);
        Float productPrice = productAPI.getProductPrice(description, manufacturer);

        Assert.assertEquals(productPrice, newPrice);
    }
}
