package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MainPageObject {

    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver) {

        this.driver = driver;
    };

    public WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds) {

        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by));
    }
    public WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }
    public WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }
    public boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }
    public WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }
    public boolean isArticleHeaderLineContainsSearchText(By by, String search_text_value, String error_message, long timeoutInSeconds) {

        List<WebElement> lists = driver.findElements(by);

        for (WebElement list : lists) {

            if (!list.getAttribute("text").matches("(.*)search_text_value(.*)"))
                return false;
        }
        return true;
    };
    //Press on screen middle point and then swipe UP
    public void swipeUP(int timeOfSwipe) {

        TouchAction action = new TouchAction(driver);

        //from Selenium:
        Dimension size = driver.manage().window().getSize();

        // x coordinate  is not changing, when we swipe up; we began swiping from the screen middle point, so:
        int x = size.width / 2;
        // y coordinate: we receive point that located in 80% of screen (in the bottom)
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);

        action
                .press(x, start_y)
                .waitAction(timeOfSwipe)
                .moveTo(x, end_y)
                .release().perform();
    }
    //press on screen middle point and then swipe UP quickly
    public void swipeUpQuick() {
        swipeUP(200);
    }
    public void swipeUpToFindElement(By by, String error_message, int max_swipes) {

        int already_swiped = 0;

        while (driver.findElements(by).size() == 0) {

            if (already_swiped > max_swipes) {
                waitForElementPresent(by, "Cannot find element by swiping up \n" + error_message, 0) ;
                return;
            }
            swipeUpQuick();
            ++already_swiped;
        }
    }
    public void swipeElementToLeft( By by, String error_message) {

        WebElement element = waitForElementPresent(
                by,
                error_message,
                10);

        // функция запишет в переменную самую левую точку по оси Х
        int left_x = element.getLocation().getX();

        int right_x = left_x + element.getSize().getWidth();

        int upper_y = element.getLocation().getY();

        int lower_y = upper_y + element.getSize().getHeight();

        int middle_y = (upper_y + lower_y) / 2;

        // инициализируем драйвер
        TouchAction action = new TouchAction(driver);

        action
                .press(right_x, middle_y)
                // really important to do long swipe - > 150
                .waitAction(500)
                .moveTo(left_x, middle_y)
                .release()
                .perform();
    }
    public int getAmountOfWebElements(By by) {
        List<WebElement> elements = driver.findElements(by);
        int a = elements.size();
        return a;
    }
    public void assertElementNotPresent(By by, String error_message) {

        int amount_of_elements = getAmountOfWebElements(by);
        if(amount_of_elements > 0) {
            String default_message = "An element '" + by.toString() + "' suppose to be not present" ;
            throw new AssertionError(default_message + " " + error_message);
        }
    }
    public void assertElementPresent(By by, String error_message) {

        int amount_of_elements = getAmountOfWebElements(by);
        if (amount_of_elements == 0) {
            String default_message = "An element " + by.toString() + " supposed to be present";
            throw new AssertionError(default_message + " " + error_message);
        }
    }
    public String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeOutInSeconds) {

        WebElement element = waitForElementPresent(by, error_message, timeOutInSeconds);

        return element.getAttribute(attribute);
    }
} //end of class
