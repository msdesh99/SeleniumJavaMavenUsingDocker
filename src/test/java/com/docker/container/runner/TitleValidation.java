package com.docker.container.runner;
import java.net.URL;
import java.net.URI;
import java.net.MalformedURLException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeTest;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.docker.container.utility.LoggerLoad;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterMethod;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import java.time.LocalDateTime;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
public class TitleValidation {
    private DesiredCapabilities capabilites; 
    //private static final String hostUrl = "http://localhost:4445/wd/hub"; 
    //hubservice from docker-compose.yml file
    private static String hostUrl = "http://HubService:4444/wd/hub"; 

    private WebDriver driver;
    private URL url;
    private String basePath;
    @BeforeSuite
    public void globalSetup(){
        System.out.println("GLobal Setup Before Suite");
    }
    @BeforeTest
    @Parameters({"browserType"})
    public void setupDriver(@Optional("chrome")String browserType){
        basePath = System.getenv("OUTPUT_DIR")!=null?
            System.getenv("OUTPUT_DIR")+"/screenshots/":
            "/app/output/screenshots/";
        capabilites = new DesiredCapabilities();
        capabilites.setBrowserName(browserType);
        try{
           // hostUrl = System.getenv("HUB_URL");
            url = URI.create(hostUrl).toURL();
            driver = new RemoteWebDriver(url,capabilites);
        }catch(MalformedURLException mfue){
            System.out.println(mfue.getMessage());
        }

    }
    @Test
    public void googleTitle(){
        System.out.println("Google Test");
        driver.get("https://www.google.com");
        System.out.println("BrowserTYpe: "+capabilites.getBrowserName()+" HUB_URL: "+ System.getenv("HUB_URL"));
        Assert.assertTrue(driver.getCurrentUrl().contains("google"));
    }
     @Test
    public void amazonTitle(){
        System.out.println("Amazon Test");
        driver.get("https://www.amazon.com");
        Assert.assertTrue(driver.getCurrentUrl().contains("amazon333"));
    }
    @AfterMethod
    public void teardown(ITestResult result){
                System.out.println("failed: "+ result.isSuccess()+" basePath: "+ basePath);

       if(!result.isSuccess()){
        System.out.println("failed");
       // globalSetup();
        String fileName = result.getTestContext().getName()+"_"+
                        result.getMethod().getMethodName()+LocalDateTime.now().toString()+".png";
            //Take a screenshot            
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            //save screenshot in a file
            File file = screenshot.getScreenshotAs(OutputType.FILE);
            //create empty destiantion file
            File dest = new File(basePath+fileName);
            //copy screenshot file into destination file
            try{
                FileUtils.copyFile(file,dest);
            }catch(IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }  
        System.out.println("Test name: "+result.getTestContext().getName());      
        LoggerLoad.info("TestCase: "+ result.getMethod().getMethodName()+" Tests: "+capabilites.getBrowserName()+" is"+ result.getStatus());
    }
@AfterTest
public void teardown(){
        System.out.println(capabilites.getBrowserName()+" Driver is Quiting");
       driver.quit();
}
}
