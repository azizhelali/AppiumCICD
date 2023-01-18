package com.qa.pages;

import com.qa.MenuPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class ProductsPage extends MenuPage {

    @AndroidFindBy (xpath ="(//android.view.ViewGroup[@content-desc=\"store item\"])[1]")
    @iOSXCUITFindBy(xpath ="//XCUIElementTypeOther[@name=\"test-Toggle\"]/parent::*[1]/preceding-sibling::*[1]")
    private WebElement productsPage;

    @AndroidFindBy (xpath ="//android.view.ViewGroup[@content-desc=\"container header\"]/android.widget.TextView")
    private WebElement productTitleTxt;

    @AndroidFindBy (xpath = "(//android.widget.TextView[@content-desc=\"store item text\"])[1]")
    private WebElement SLBTitle;

    @AndroidFindBy (xpath = "(//android.widget.TextView[@content-desc=\"store item price\"])[1]")
    private WebElement SLBPrice;
    TestUtils utils=new TestUtils();
    public Boolean checkPageLoad(){
        return checkEnable(productsPage);
    }
    public String getTitle(){
        String pageTitle=getAttribute(productTitleTxt,"text","page title is");
        return pageTitle;
    }
    public String getSLBTitle(){
        String productTitle=getAttribute(SLBTitle,"text","product Title is");
        return productTitle;
    }
    public String getSLBPrice(){
        String productPrice=getAttribute(SLBPrice,"text","product price is");
        return productPrice;
    }
    public ProductDetailsPage pressSLBTitle(){
        click(SLBTitle,"press SLB Title");
        return new ProductDetailsPage();
    }

}
