package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailsPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import java.io.InputStream;
import java.lang.reflect.Method;


public class ProductTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    JSONObject loginUsers;
    SettingsPage settingsPage;
    ProductDetailsPage productDetailsPage;
    TestUtils utils=new TestUtils();

    @BeforeClass
    public void beforeClass() throws Exception {
        InputStream datais = null;
        try {
            String dataFileName="data/loginUsers.json";
            datais = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener tokener=new JSONTokener(datais);
            loginUsers = new JSONObject(tokener);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if(datais != null){
                datais.close();
            }
        }

    }
    @AfterClass
    public void afterClass(){

    }
    @BeforeMethod
    public void beforeMethod(Method m){
        utils.log().info("ProductTest before method");
        loginPage=new LoginPage();
//        productsPage=new ProductsPage();
        utils.log().info("\n"+"**********Starting Test:"+m.getName()+"****************"+"\n");
    }
    @AfterMethod
    public void afterMethod(){
        utils.log().info("ProductTest after method");
    }

    @Test
    public void validateProductOnProductPage() throws InterruptedException {
            SoftAssert sa=new SoftAssert();
            Thread.sleep(3000);
            productsPage =loginPage.login(loginUsers.getJSONObject("validUser").getString("username"),loginUsers.getJSONObject("validUser").getString("password"));
            String SLBTitle=productsPage.getSLBTitle();
            sa.assertEquals(SLBTitle,getStrings().get("products_page_slb_title"));
            String SLBPrice=productsPage.getSLBPrice();
            sa.assertEquals(SLBPrice,getStrings().get("products_page_slb_price"));
            settingsPage =productsPage.pressSettingsBtn();
            loginPage=settingsPage.pressLogOutBtn();
            sa.assertAll();
    }
    @Test
    public void validateProductOnProductDetailsPage() throws InterruptedException {
        SoftAssert sa=new SoftAssert();
        Thread.sleep(3000);
        productsPage =loginPage.login(loginUsers.getJSONObject("validUser").getString("username"),loginUsers.getJSONObject("validUser").getString("password"));
        productDetailsPage=productsPage.pressSLBTitle();
        Thread.sleep(3000);

        String SLBTitle=productDetailsPage.getSLBTitle();
        sa.assertEquals(SLBTitle,getStrings().get("products_details_page_slb_title"));
        String SLBPrice=productsPage.getSLBPrice();
        sa.assertEquals(SLBPrice,getStrings().get("products_details-page_slb_price"));

        productDetailsPage.scrollToSLBDescription();
        String actualSLBDescription=productDetailsPage.getDescriptionProduct();
        Assert.assertEquals(actualSLBDescription,getStrings().get("products_details-page_slb_description"));
        settingsPage =productsPage.pressSettingsBtn();
        productsPage=settingsPage.pressCatalogBtn();
        Thread.sleep(2000);
        settingsPage =productDetailsPage.pressSettingsBtn();
        loginPage=settingsPage.pressLogOutBtn();
        sa.assertAll();
    }

}
