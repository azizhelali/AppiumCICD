package com.qa.pages;

import com.qa.MenuPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class ProductDetailsPage extends MenuPage {

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Sauce Labs Backpack']")
    private WebElement SLBTitle;

    @AndroidFindBy (accessibility = "product price")
    private WebElement SLBPrice;

    @AndroidFindBy(accessibility = "product description")
            private WebElement SLBDescription;
   SettingsPage settingsPage;
   TestUtils utils=new TestUtils();

    public String getSLBTitle(){
        String SLBTitleFromProductDetails=getAttribute(SLBTitle,"text","SLB Title From Product Details Page is");
        return SLBTitleFromProductDetails;
    }
    public String getSLBPrice(){
        String SLBPriceFromProductDetails=getAttribute(SLBPrice,"text","SLB Price From Product Details is");
        return SLBPriceFromProductDetails;
    }
    public ProductsPage backToProductPage(){
        utils.log().info("Press Catalog Button From Settings Page to come back to Product Page");
        settingsPage.pressCatalogBtn();
        return new ProductsPage();
    }
    public String getDescriptionProduct(){

        return  getAttribute(SLBDescription,"text","SLB Description From Product Details is");
    }
    public ProductDetailsPage scrollToSLBDescription(){
        scrollToElement();
        return this;
    }
}
