package com.qa;

import com.qa.pages.SettingsPage;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class MenuPage extends BaseTest{
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"open menu\"]/android.widget.ImageView")
    private WebElement settingsBtn;

    public  SettingsPage pressSettingsBtn(){
        click(settingsBtn);
        return new SettingsPage();
    }
}