package com.qa.pages;

import com.google.common.collect.ImmutableMap;
import com.qa.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

public class SettingsPage extends BaseTest{
    TestUtils utils=new TestUtils();

        @AndroidFindBy(accessibility = "menu item log out")
        private WebElement logOutBtn;

        @AndroidFindBy(accessibility = "android:id/button1")
        private WebElement confirmLogOutBtn;

        @AndroidFindBy(xpath="//android.view.ViewGroup[@content-desc=\"open menu\"]")
        private WebElement CatalogBtn;

        public LoginPage pressLogOutBtn() throws InterruptedException {
            click(logOutBtn,"press log out button");
            return new LoginPage();
        }
        public ProductsPage pressCatalogBtn(){
            click(CatalogBtn,"press Catalog button");
            return new ProductsPage();
        }
    }

