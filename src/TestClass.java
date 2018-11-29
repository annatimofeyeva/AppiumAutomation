import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class TestClass {
    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("automationName", "Appium");
        //capabilities.setCapability("appPackage", "ru.yandex.disk");
        //capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability(MobileCapabilityType.APP, "/home/anya/AppiumAutomation/apks/app-arm-v27882-release-signed.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

//    @After
//    public void tearDown() {
//        driver.quit();
//    }

    @Test
    public void firstTest() {

        System.out.println("Demo test OK");


        waitForElementAndSendKeys(
                By.xpath("//android.widget.EditText[@text='Username']"),
                "Test",
                "",
                10
        );

    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

        private WebElement waitForElementAndSendKeys (By by, String value, String error_message,long timeoutInSeconds){
            WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
            element.sendKeys(value);
            return element;
        }

        private WebElement waitForElementPresent (By by, String error_message,long timeoutInSeconds){

            WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
            wait.withMessage(error_message + "\n");
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        }
    }

