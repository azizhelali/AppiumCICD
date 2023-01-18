package com.qa.pages;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class LoginPage extends BaseTest {

    @AndroidFindBy(accessibility = "Username input field")
    @iOSXCUITFindBy(id="Username input")
    private WebElement user_name_fld;

    @AndroidFindBy(accessibility = "Password input field")
    @iOSXCUITFindBy(id="Password input")
    private WebElement password_fld;

    @AndroidFindBy(accessibility = "Login button")
    @iOSXCUITFindBy(id="Login")
    private WebElement loginButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"generic-error-message\"]/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Error message\"]/child::XCUIElementTypeStaticText")
    private WebElement errTxt;
    TestUtils utils=new TestUtils();

    public  LoginPage enterUserName(String username){
        clearElement(user_name_fld,"clear user name field");
        sendKeys(user_name_fld,username,"login with"+username);
        return this;
    }
    public  LoginPage enterPassword(String password){
        clearElement(password_fld,"clear password field");
        sendKeys(password_fld,password,"password is "+password);
        return this;
    }
    public LoginPage clearUserName(){
        clearElement(user_name_fld,"clear user name field");
        return this;
    }
    public LoginPage clearPasswordName(){
        clearElement(password_fld,"clear password field");
        return this;
    }
    public  ProductsPage pressLoginBtn(){
        click(loginButton,"press login button");
        return new ProductsPage();
    }
    public String getErrTxt(){
        String errorTxt=getAttribute(errTxt,"text","err Text is ");
        return errorTxt;
    }
    public ProductsPage login(String username,String password){
        enterUserName(username);
        enterPassword(password);
        return pressLoginBtn();
    }

}
