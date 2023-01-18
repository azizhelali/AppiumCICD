package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.InputStream;
import java.lang.reflect.Method;


public class LoginTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    JSONObject loginUsers;
    TestUtils utils = new TestUtils();
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
        loginPage=new LoginPage();
//        productsPage=new ProductsPage();
        utils.log().info("\n"+"**********Starting Test:"+m.getName()+"****************"+"\n");
    }
    @AfterMethod
    public void afterMethod(){

    }
    @Test

    public void invalidUserName() throws InterruptedException {
            Thread.sleep(3000);
//            loginPage.clearUserName();
//            loginPage.clearPasswordName();
            loginPage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username"));
            loginPage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
            loginPage.pressLoginBtn();
            String actualErrTxt=loginPage.getErrTxt();
            String expectedErrTxt=getStrings().get("err_invalid_username_or_password");
            utils.log().info("Actual Error Text is : "+actualErrTxt+"\n"+"Expected Error Text is :"+expectedErrTxt);

            Assert.assertEquals(actualErrTxt,expectedErrTxt);
    }

    @Test
    public void invalidPassword() throws InterruptedException {

        Thread.sleep(3000);
//        loginPage.clearUserName();
//        loginPage.clearPasswordName();
        loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
        loginPage.pressLoginBtn();
        String actualErrTxt=loginPage.getErrTxt();
        String expectedErrTxt=getStrings().get("err_invalid_username_or_password");
        utils.log().info("Actual Error Text is : "+actualErrTxt+"\n"+"Expected Error Text is :"+expectedErrTxt);
        Assert.assertEquals(actualErrTxt,expectedErrTxt);
    }

    @Test
    public void successfullLogin() throws InterruptedException {
        Thread.sleep(3000);
        Thread.sleep(3000);
//        loginPage.clearUserName();
//        loginPage.clearPasswordName();
        loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
        productsPage =loginPage.pressLoginBtn();


        Boolean actualtEnalbeStatus =productsPage.checkPageLoad();
        String expectedEnabledStatus="true";
        String expectedTitle="Products";
        String actualTitle=productsPage.getTitle();
        utils.log().info("Le résultat affiché du statut d'affichage de la page produit est "+actualtEnalbeStatus.toString()+"Le résultat attendu du statut d'affichage de la page produit  est "+expectedEnabledStatus);
        Assert.assertEquals(actualtEnalbeStatus.toString(),expectedEnabledStatus);
        utils.log().info("Le titre affiché est "+ actualTitle+"et le tite attndu est "+expectedTitle);
        Assert.assertEquals(actualTitle,expectedTitle);
    }
}
