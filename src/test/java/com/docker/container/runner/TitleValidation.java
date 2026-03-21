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
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterMethod;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import java.time.LocalDateTime;

public class TitleValidation {
    private DesiredCapabilities capabilites; 
    //private static final String hostUrl = "http://localhost:4445/wd/hub"; 
    //hubservice from docker-compose.yml file
    private static String hostUrl = "http://HubService:4444/wd/hub"; 

    private WebDriver driver;
    private URL url;
    @BeforeTest
    @Parameters({"browserType"})
    public void setupDriver(@Optional("chrome")String browserType){
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
        driver.get("https://www.google.com");
        System.out.println("BrowserTYpe: "+capabilites.getBrowserName()+" HUB_URL: "+ System.getenv("HUB_URL"));
        Assert.assertTrue(driver.getCurrentUrl().contains("google"));
        //driver.quit();
    }
    @AfterMethod
    public void teardown(ITestResult result){
        System.out.println("OUTPUT env: "+System.getenv("OUTPUT_DIR"));
        String basePath = System.getenv("OUTPUT_DIR")!=null?
            System.getenv("OUTPUT_DIR")+"/screenshots/":
            "/app/output/screenshots/";
            File dir = new File(basePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
        String fileName = result.getTestContext().getName()+"_"+
                        result.getMethod().getMethodName()+LocalDateTime.now()+".png";
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
        System.out.println("Test name: "+result.getTestContext().getName());
        driver.quit();
    }
}
