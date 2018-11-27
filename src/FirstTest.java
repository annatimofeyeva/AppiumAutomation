import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.net.URL;
import java.util.List;


public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "/home/anya/AppiumAutomation/apks/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

//    @After
//    public void tearDown() {
//        driver.quit();
//    }

    @Test
    public void firstTest() {

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementPresent(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Island of Indonesia']"),
                "Cannot find 'Island of Indonesia' topic searching by Java",
                15

        );
    }

    @Test
    public void testCancelSearch() {

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search wikipedia' input",
                5
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field",
                5
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find 'X' to cancel search",
                5
        );

        waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X is still present on page",
                5
        );
    }

    @Test
    public void testCompareArticleTitle() {

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Object-oriented programming language')]"),
                "Object-oriented programming language line",
                5
        );

        WebElement title_element = waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15
        );
        String article_title = title_element.getAttribute("text");
        Assert.assertEquals(
                "We see unexpected title",
                "Java (programming language)",
                article_title
        );
    }

    @Test
    public void testSearchForPromptWordInSearchInputField() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' in input field",
                5
        );

        WebElement search_input_field_element = waitForElementPresent(
                By.id("org.wikipedia:id/search_src_text"),
                "Search input field is not found",
                15
        );

        String search_prompt_text = search_input_field_element.getAttribute("text");
        Assert.assertEquals(
                "Unexpected prompt text in search input field ",
                "Search…",
                search_prompt_text
        );
    }

    @Test
    public void testSearchAndDeleteSearchResults() {

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Seattle",
                "Cannot find search input",
                5
        );


        String search_result_locator = "//*[@class='android.widget.LinearLayout']/*[@resource-id='org.wikipedia:id/page_list_item_image']";
        String search_line = "Seattle";


        waitForElementPresent(By.xpath(search_result_locator),
                "Cannot find anything by the request" + search_line,
                15
        );

        int amount = getAmountOfWebElements(By.xpath(search_result_locator));
        System.out.println(amount);

        Assert.assertTrue("Number of articles more then 1", amount > 1);

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X button",
                15
        );

        WebElement screen_element = waitForElementPresent(
                By.id("org.wikipedia:id/search_empty_message"),
                "Cannot find search message",
                15
        );

        // variable with search message text

        String search_message_text_in_the_middle_of_the_page = screen_element.getAttribute("text");

        // assert for search message text

        Assert.assertEquals(
                "Unexpected search message displays",
                "Search and read the free encyclopedia in your language",
                search_message_text_in_the_middle_of_the_page
        );

        // printing search message text to console

        System.out.println(search_message_text_in_the_middle_of_the_page);


        WebElement search_input_field_element = waitForElementPresent(
                By.id("org.wikipedia:id/search_src_text"),
                " is not found",
                15
        );

        String search_prompt_text = search_input_field_element.getAttribute("text");
        System.out.println(search_prompt_text);

        Assert.assertEquals(
                "Unexpected prompt text in search input field ",
                "Search…",
                search_prompt_text
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                10
        );

        String input_text_string = search_input_field_element.getAttribute("text");
        System.out.println(input_text_string);

        Assert.assertEquals("Java", input_text_string
        );
    }

    //List<WebElement> allArticles = driver.findElementsByXPath("//*[@class='android.widget.LinearLayout']/*[@resource-id='org.wikipedia:id/page_list_item_image']");

    //findElementsByXPath("//*[@class='android.widget.LinearLayout']/*[@resource-id='org.wikipedia:id/page_list_item_image']");


//        int n = allArticles.size();
//        System.out.println("Number of all articles " + n);
//        for (int i = 0; i < n; i++) {
//            allArticles.get(i).click();
//        }


//        for (WebElement webElement : allArticles) {
//            System.out.println(webElement.getSize());
//            System.out.println(webElement.getTagName());
//        }


    // create variable with the locator:
    //String article_search_result_locator =


//
//    @Test
//    public void testAmountOfNotEmptySearch()
//    {
//        waitForElementAndClick(
//                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
//                "Cannot find 'Search Wikipedia' input.",
//                5
//        );
//
//        String search_line = "Linkin Park Diskography"; // we need it to reuse now and to make it a dataProvider param in future
//        waitForElementAndSendKeys(
//                By.xpath("//*[contains(@text,'Search…')]"),
//                search_line,
//                "Cannot find search input",
//                5
//        );
//
//        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
//        waitForElementPresent(
//                By.xpath(search_result_locator),
//                "Cannot find anything by the request " + search_line,
//                15
//        );
//
//        int amount_of_search_results = getAmountOfElements(
//                By.xpath(search_result_locator)
//        );
//
//
//        /*
//         * А вот нам и понадобился hamcrest-либа
//         * Знакомься, это аналог assertGreaterThan :)
//         * Я считаю, это надо показать в курсе и рассказать об этом.
//         * Но смотри сам - ниже есть просто assertTrue.
//         * */
//        Assert.assertThat(
//                "We found too few results!",
//                amount_of_search_results,
//                //Matchers.greaterThan(50) // to fail
//                Matchers.greaterThan(0) // to pass
//        );
//
//        /*Assert.assertTrue(
//                "We found too few results!",
//                amount_of_search_results > 0
//        );*/
//    }

    /**
     * We need this one and testAmountOfNotEmptySearch to show how "find elements" method works
     * Also we'll use assertGreaterThen and assertLessThen functions
     * Also we'll refactor this two tests in one with dataProvider
     */


    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds) {

        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by));
    }


    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }


    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }


    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    private int getAmountOfWebElements(By by) {
        List elements = driver.findElements(by);
        int a = elements.size();
        return a;
    }

}
