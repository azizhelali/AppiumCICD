package com.qa;

import com.aventstack.extentreports.Status;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class BaseTest {
    protected static ThreadLocal<AppiumDriver> driver=new ThreadLocal<AppiumDriver>();
    protected ThreadLocal<Properties> props=new ThreadLocal<Properties>();
    protected static ThreadLocal<String> platform=new ThreadLocal<String>();
    protected static ThreadLocal<String> deviceName=new ThreadLocal<String>();
    protected static ThreadLocal<String> dateTime=new ThreadLocal<String>();
    protected static ThreadLocal<HashMap<String,String>> strings=new ThreadLocal<HashMap<String, String>>();
    private static AppiumDriverLocalService server;
    TestUtils utils = new TestUtils();

    public static AppiumDriver getDriver(){

        return driver.get();
    }
    public void setDriver(AppiumDriver driver2){

        driver.set(driver2);
    }
    public HashMap<String,String> getStrings(){

        return strings.get();
    }
    public void setStrings(HashMap<String,String> strings2){

        strings.set(strings2);
    }
    public Properties getProps(){

        return props.get();
    }
    public void setProps(Properties props2){

        props.set(props2);
    }
    public String getDeviceName(){

        return deviceName.get();
    }
    public void setDeviceName(String deviceName2){

        deviceName.set(deviceName2);
    }
    public String getPlatform(){

        return platform.get();
    }
    public void setPlatform(String platform2){

        platform.set(platform2);
    }
    public String getDateTime(){

        return dateTime.get();
    }
    public void setDateTime(String dateTime2){

        dateTime.set(dateTime2);
    }

    public BaseTest(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()),this);
    }
    @BeforeMethod
    public void beforeMethod( ){
        utils.log().info("super before method");
        //ExtentReport.getTest().log(Status.INFO,"super before method");
        ((CanRecordScreen)getDriver()).startRecordingScreen();
    }
    @AfterMethod
    public synchronized void afterMethod(ITestResult result){
        utils.log().info("super after method");
        //ExtentReport.getTest().log(Status.INFO,"super after method");
        String media=((CanRecordScreen)getDriver()).stopRecordingScreen();
        if(result.getStatus()==2){
            Map<String,String> params=result.getTestContext().getCurrentXmlTest().getAllParameters();
            String dir="Videos"+ File.separator+params.get("platformName")
                    + "_" + params.get("deviceName") + File.separator +getDateTime()+File.separator+result.getTestClass().getRealClass().getSimpleName();
            File videoDir=new File(dir);
            synchronized (videoDir){
                if(! videoDir.exists()){
                    videoDir.mkdirs();
                }
            }
            try {
                FileOutputStream stream=new FileOutputStream(videoDir+File.separator+result.getName()+".mp4");
                stream.write(Base64.getDecoder().decode(media));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @BeforeSuite
    public void beforeSuite() throws Exception, Exception {
        ThreadContext.put("ROUTINGKEY", "ServerLogs");
        server = getAppiumServerDefault();
        if(!checkIfAppiumServerIsRunnning(4723)) {
            server.start();
            server.clearOutPutStreams();
            utils.log().info("Appium server started");
            //ExtentReport.getTest().log(Status.INFO,"Appium server started");
        } else {
            utils.log().info("Appium server already running");
            //ExtentReport.getTest().log(Status.INFO,"Appium server already running");
        }
    }

    public boolean checkIfAppiumServerIsRunnning(int port) throws Exception {
        boolean isAppiumServerRunning = false;
        ServerSocket socket;
        try {
            socket = new ServerSocket(port);
            socket.close();
        } catch (IOException e) {
            System.out.println("1");
            isAppiumServerRunning = true;
        } finally {
            socket = null;
        }
        return isAppiumServerRunning;
    }
    @AfterSuite(alwaysRun = true)
    public void afterSuite(){
        if(server.isRunning()){
            server.stop();
            utils.log().info("Appium server stopped");
            //ExtentReport.getTest().log(Status.INFO,"Appium server stopped");
        }
    }
    public AppiumDriverLocalService getAppiumServerDefault(){
        return AppiumDriverLocalService.buildDefaultService();
    }
    // for Mac. Update the paths as per your Mac setup
    public AppiumDriverLocalService getAppiumService() {
        HashMap<String, String> environment = new HashMap<String, String>();
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
                .withAppiumJS(new File("C:\\Users\\medaz\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
                .usingPort(4723)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
				//.withArgument(() -> "--base-path", "/wd/hub")
                .withLogFile(new File("ServerLogs/server.log")));
    }

    @Parameters({"platformName","udid","deviceName","systemPort"})
    @BeforeTest
    public void beforeTest(String platformName,String udid,String deviceName,String systemPort) throws Exception {
        setDeviceName(deviceName);
        setPlatform(platformName);
        setDateTime(utils.dateTime());
        InputStream inputStream = null;
        InputStream stringis = null;
        URL url;
        Properties props =new Properties();
        AppiumDriver driver;
        String strFile="logs" + File.separator + platformName + "_" + deviceName;
        File logFile=new File(strFile);
        if (!logFile.exists()){
            logFile.mkdirs();
        }
        //route logs to separate file for each thread
        ThreadContext.put("ROUTINGKEY", strFile);
        utils.log().info("log path: " + strFile);
        //ExtentReport.getTest().log(Status.INFO,"log path: " + strFile);
        try {
            props =new Properties();
            String propFileName= "config.properties";
            String xmlFileName="strings/strings.xml";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);
            setProps(props);
            stringis=getClass().getClassLoader().getResourceAsStream(xmlFileName);
            setStrings(utils.parseStringXML(stringis));


            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME,platformName);
            caps.setCapability(MobileCapabilityType.DEVICE_NAME,deviceName);
            caps.setCapability(MobileCapabilityType.UDID,udid);
            url= new URL(props.getProperty("appiumURL"));
            switch (platformName){
                case "Android":
                    caps.setCapability(MobileCapabilityType.AUTOMATION_NAME,props.getProperty("androidAutomationName"));
                    caps.setCapability("systemPort",systemPort);
//                    caps.setCapability("appPackage", props.getProperty("androidAppPackage"));
//                    caps.setCapability("appActivity", props.getProperty("androidAppActivity"));
//                    if(emulator.equalsIgnoreCase("true")) {
//                        caps.setCapability("avd", deviceName);
//                        caps.setCapability("avdLaunchTimeout", 120000);
//                    }
                    String androidAppUrl=System.getProperty("user.dir") +"\\src\\test\\resources\\app\\"+"Android-MyDemoAppRN.1.3.0.build-244.apk";
                    //File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator +" app" + File.separator + "Android-MyDemoAppRN.1.3.0.build-244.apk";
                    caps.setCapability(MobileCapabilityType.APP,androidAppUrl);

                    driver=new AndroidDriver(url,caps);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
                    driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"open menu\"]")).click();
                    driver.findElement(AppiumBy.accessibilityId("menu item log in")).click();
                    break;
                case "iOS":
                    caps.setCapability(MobileCapabilityType.AUTOMATION_NAME,props.getProperty("iOSAutomationName"));
//                  caps.setCapability(MobileCapabilityType.AUTOMATION_NAME,"UiAutomator2");
                    String iOSAppUrl=System.getProperty("user.dir")+"\\src\\test\\resources\\app\\"+"iOS.Simulator.SauceLabs.Mobile.Sample.app.2.7.1.app";
                    caps.setCapability(MobileCapabilityType.APP,iOSAppUrl);
                    url= new URL(props.getProperty("appiumURL"));
                    driver=new IOSDriver(url,caps);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
                    driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"open menu\"]")).click();
                    driver.findElement(AppiumBy.accessibilityId("menu item log in")).click();
                    break;
                default:
                    throw  new Exception("invalid platform! _"+ platformName);
            }
            setDriver(driver);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            if(inputStream!= null){
                inputStream.close();
            }
            if(stringis != null){
                stringis.close();
            }
        }

    }
    public void waitForVisibility(WebElement e){
        WebDriverWait wait=new WebDriverWait(getDriver(),Duration.ofSeconds(TestUtils.WAIT));
        wait.until(ExpectedConditions.visibilityOf(e));
    }
    public void click(WebElement e){
        waitForVisibility(e);
        e.click();
    }
    public void click(WebElement e,String msg){
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO,msg);
        e.click();
    }
    public void sendKeys(WebElement e,String txt){

        waitForVisibility(e);
        e.sendKeys(txt);
    }
    public void sendKeys(WebElement e,String txt,String msg){
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO,msg);
        e.sendKeys(txt);
    }
    public String getAttribute(WebElement e, String attribute,String msg){
        String Txt;
        waitForVisibility(e);
        Txt= e.getAttribute(attribute);
        utils.log().info(msg+Txt);
        ExtentReport.getTest().log(Status.INFO,msg+Txt);
        return Txt;
    }
    public String getText(WebElement e,String msg){
        switch (getPlatform()){
            case "Android":
                getAttribute(e,"text",msg);
            case "iOS":
                getAttribute(e,"label",msg);
        }
        return null;
    }
    public void clearElement(WebElement e,String msg){
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO,msg);
        e.clear();
    }
    public Boolean checkEnable(WebElement e){
        waitForVisibility(e);
        return (e.isDisplayed());
    }
    public void closeApp() {
        ((InteractsWithApps) getDriver()).terminateApp(getProps().getProperty("C:\\Workspace\\appiumFramework\\src\\test\\resources\\Android-MyDemoAppRN.1.3.0.build-244.apk"));
    }
    public void launchApp(){
        ((InteractsWithApps) driver).installApp("C:\\Workspace\\appiumFramework\\src\\test\\resources\\Android-MyDemoAppRN.1.3.0.build-244.apk");
    }
    public WebElement scrollToElement() {
        return getDriver().findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
                        + "new UiSelector().description(\"product description\"));"));
    }

    @AfterTest
    public void afterTest(){

        getDriver().quit();
    }
}
