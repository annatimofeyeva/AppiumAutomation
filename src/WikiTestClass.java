import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;



public class WikiTestClass {

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

    @After
    public void tearDown() {
        driver.rotate(ScreenOrientation.PORTRAIT);
        //driver.quit();
    }

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
                "Cannot find 'Object-oriented programming' topic",
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

        //verification that our prompt text in search inputField equals the value of "text" attribute in XML
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' in input field",
                5
        );
        WebElement search_input_field_element = waitForElementPresent(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search input field",
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
        // Xpath = //tag_name[@attribute = 'value']. In Android "class" attribute or "package" can serves as a tag.
        String search_result_locator = "//android.widget.LinearLayout[@resource-id = 'org.wikipedia:id/page_list_item_container']";

        // Locator - using xpaths concatenation:
        //String search_result_locator = "//*[@class='android.widget.LinearLayout']/*[@resource-id='org.wikipedia:id/page_list_item_image']";

        String search_line = "Seattle";

        waitForElementPresent(By.xpath(search_result_locator),
                "Cannot find anything by the request" + search_line,
                15
        );
        //verification that we received more then 1 article in our search result
        int amount = getAmountOfWebElements(By.xpath(search_result_locator));
        System.out.println(amount);

        Assert.assertTrue("Number of articles more then 1", amount > 1);

        //delete search results (delete all searched articles from page)
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X button",
                15
        );

        assertElementNotPresent(
                By.xpath(search_result_locator),
                "We still can find some articles on page after deleting"
                );
    }


//        Assert.assertTrue("Articles still present after deletion", amount_after_deletion < 1);
//
//
//        /* verification of presence of the element with "text" attribute  =  "Search and read the free encyclopedia in your language" -
//         so we can mark new search page
//        */
//        WebElement screen_element = waitForElementPresent(
//                By.id("org.wikipedia:id/search_empty_message"),
//                "Cannot find search message",
//                15
//        );
//
//        String search_message_text_in_the_middle_of_the_page = screen_element.getAttribute("text");
//        System.out.println(search_message_text_in_the_middle_of_the_page);
//
//        Assert.assertEquals(
//                "Unexpected search message displays",
//                "Search and read the free encyclopedia in your language",
//                search_message_text_in_the_middle_of_the_page
//        );
//        //verification of prompt "Search…" text
//        WebElement search_input_field_element = waitForElementPresent(
//                By.id("org.wikipedia:id/search_src_text"),
//                " Cannot find Search input field",
//                15
//        );
//
//        String search_prompt_text = search_input_field_element.getAttribute("text");
//        System.out.println(search_prompt_text);
//
//        Assert.assertEquals(
//                "Unexpected prompt text in search input field ",
//                "Search…",
//                search_prompt_text
//        );
//        // starting new search with value "Java" and counting search result
//        waitForElementAndSendKeys(
//                By.id("org.wikipedia:id/search_src_text"),
//                "Java",
//                "Cannot find search input",
//                10
//        );
//
//        String input_text_string = search_input_field_element.getAttribute("text");
//        System.out.println(input_text_string);
//
//        Assert.assertEquals("Java", input_text_string
//        );


    // test mostly always failed - so not in all article's titles presents "Java"

//    @Test
//    public void testSearchForMatchingInArticlesHeaders() {
//
//        waitForElementAndClick(
//                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
//                "Cannot find element to init search",
//                5
//        );
//        waitForElementAndSendKeys(
//                By.id("org.wikipedia:id/search_src_text"),
//                "Java",
//                "Cannot find search input",
//                5
//        );
//        WebElement search_input = waitForElementPresent(
//                By.id("org.wikipedia:id/search_src_text"),
//                "Search input field not found",
//                15
//        );
//        String search_text_value = search_input.getAttribute("text").toLowerCase();
//        System.out.println(search_text_value);
//
//        boolean result = isArticleHeaderLineContainsSearchText(By.id("org.wikipedia:id/page_list_item_container"),
//                search_text_value,
//                "Does not found matches in article headers",
//                15);
//
//        Assert.assertTrue("Some searched articles does not have matching between header text and searched text value", result);
//    }

    @Test
    public void testSwipeArticle() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );
        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Appium",
                "Cannot find search input",
                5
        );

        //Xpath = //tag_name[@attribute = 'value']
        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Appium']"),
                "Cannot find Appium in Search",
                5
        );
        //long time swipe
        //swipeUP(2000);

        swipeUpToFindElement(
                By.xpath("//android.widget.TextView[@text = 'View page in browser']"),
                "Cannot find the end of the article" ,
                20
        );
    }

    @Test
    public void saveFirstArticleToMyListAndDeleteBySwipe() {
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
        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc ='More options']"),
                "Cannot find 'More options' button",
                15
                );

        waitForElementPresent(
                By.xpath("//android.widget.TextView[@resource-id='org.wikipedia:id/title']"),
                "'More options' menu was not downloaded completely",
                15
        );
        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Add to reading list']"),
                "Cannot find 'Add to list' button",
                5
        );
        waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find 'Cot it' button",
                5
        );
        waitForElementAndClear(
                By.xpath("//android.widget.EditText[@text='My reading list']"),
                "Cannot clear text in 'Name of List' field",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//android.widget.EditText[@NAF='true']"),
                "Learning programming",
                "Can not enter list name",
                5
        );
        waitForElementAndClick(
        By.xpath("//android.widget.Button[@text='OK']"),
        "Cannot find OK button, Can not close article",
                5
        );
        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot find X button",
                5
        );
        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find 'redirection to saved articles list' button",
                5
        );
        waitForElementAndClick(
                By.xpath("//android.support.v7.widget.RecyclerView[@resource-id='org.wikipedia:id/reading_list_list']//android.widget.ImageView[@resource-id='org.wikipedia:id/item_image_1']"),
                "Cannot tap on article title",
                5
        );
       swipeElementToLeft(
               By.xpath("//android.widget.TextView[@text='Java (programming language)']"),
               "Cannot find swipe article"
        );
        waitForElementNotPresent(
               By.xpath("//android.widget.TextView[@text='Java (programming language)']"),
                "Cannot delete saved article",
                15
        );
    }

    // выводим список статей, считаем размер и убеждаемся, что количество найденных статей больше 0
    @Test
    public void testAmountOfNotEmptySearch() {

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        String search_line = "Linkin Park Diskography";
        waitForElementAndSendKeys(
                By.xpath("//android.widget.EditText[@text='Search…']"),
                search_line,
                "Cannot find search input",
                5
        );
        waitForElementPresent(
                By.xpath("//android.widget.LinearLayout[@resource-id = 'org.wikipedia:id/page_list_item_container']"),
                "Cannot find anything by the request " + search_line,
                15
        );
        String search_result_locator = "//android.widget.LinearLayout[@resource-id = 'org.wikipedia:id/page_list_item_container']";

        int amount = getAmountOfWebElements(By.xpath(search_result_locator));
        System.out.println(amount);

        Assert.assertTrue("Number of articles less then 1", amount > 0);
    }

    @Test
    public void testAmountOfEmptySearch() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        String search_line =" xvtsftcccvf";
        waitForElementAndSendKeys(
                By.xpath("//android.widget.EditText[@text='Search…']"),
                search_line,
                "Cannot find search input",
                5
        );

        String search_result_locator = "//android.widget.LinearLayout[@resource-id = 'org.wikipedia:id/page_list_item_container']";
        String empty_result_label = "//android.widget.TextView[@text='No results found']";

        waitForElementPresent(
                By.xpath(empty_result_label ),
                "Cannot find empty result label by the request "+ search_line,
                15
        );
        assertElementNotPresent(
                By.xpath(search_result_locator),
                "We found some results by request " + search_line
        );
    }
    @Test
    public void testChangeScreenOrientationOnSearchResults() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        String search_line = "Java";
        waitForElementAndSendKeys(
                By.xpath("//android.widget.EditText[@text='Search…']"),
                search_line,
                "Cannot find search input",
                5
        );
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Object-oriented programming language')]"),
                "Cannot find 'Object-oriented programming' topic searching by " + search_line,
                15
        );

        String title_before_rotation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );

        driver.rotate(ScreenOrientation.LANDSCAPE);

        String title_after_rotation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );

        Assert.assertEquals(
                "Article title have been changed after rotation",
                title_before_rotation,
                title_after_rotation
        );

        driver.rotate(ScreenOrientation.PORTRAIT);

        String title_after_second_rotation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );

        Assert.assertEquals(
                "Article title have been changed after rotation",
                title_before_rotation,
                title_after_second_rotation
        );
    }

    @Test
    public void testSearchArticleInBackground(){

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
        waitForElementPresent(
                By.xpath("//*[contains(@text,'Object-oriented programming language')]"),
                "Object-oriented programming language line",
                5
        );

        // set 2 seconds for staying app in background
        driver.runAppInBackground(2);

        waitForElementPresent(
                By.xpath("//*[contains(@text,'Object-oriented programming language')]"),
                "Cannot find article after returning from background",
                5
        );
    }

    @Test
    public void testSaveTwoArticlesAndThenDeleteOneArticle() {
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
                "Cannot find 'Object-oriented programming language' line",
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
        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc ='More options']"),
                "Cannot find 'More options' button",
                15
        );

        waitForElementPresent(
                By.xpath("//android.widget.TextView[@resource-id='org.wikipedia:id/title']"),
                "'More options' menu was not downloaded completely",
                15
        );
        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Add to reading list']"),
                "Cannot find 'Add to list' button",
                5
        );
        waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find 'Cot it' button",
                5
        );
        waitForElementAndClear(
                By.xpath("//android.widget.EditText[@text='My reading list']"),
                "Cannot clear text in 'Name of List' field",
                5
        );
        waitForElementAndSendKeys(
                By.xpath("//android.widget.EditText[@NAF='true']"),
                "Learning programming",
                "Can not enter list name",
                5
        );
        waitForElementAndClick(
                By.xpath("//android.widget.Button[@text='OK']"),
                "Cannot find OK button, Can not close article",
                5
        );
        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot find X button",
                5
        );
        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find 'redirection to saved articles list' button",
                5
        );
        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Learning programming']"),
                "Cannot tap on article title",
                5
        );
        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Learning programming']"),
                "Cannot tap on article title",
                5
        );
        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot find 'Navigate up button'",
                10
        );
        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='Explore']"),
                "Cannot find 'Explore' button",
                10
        );
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
                By.xpath("//*[contains(@text,'Java (software platform)')]"),
                "Cannot find 'Object-oriented programming language' line",
                5
        );
//        WebElement title_element2 = waitForElementPresent(
//                By.id("org.wikipedia:id/view_page_title_text"),
//                "Cannot find article title",
//                15
//        );
        String article_title2 = title_element.getAttribute("text");
        Assert.assertEquals(
                "We see unexpected title",
                "Java (software platform)",
                article_title2
        );
        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc ='More options']"),
                "Cannot find 'More options' button",
                15
        );

        waitForElementPresent(
                By.xpath("//android.widget.TextView[@resource-id='org.wikipedia:id/title']"),
                "'More options' menu was not downloaded completely",
                15
        );
        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Add to reading list']"),
                "Cannot find 'Add to list' button",
                5
        );

        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Learning programming']"),
                "Cannot find 'Learning programming' list",
                5
        );
        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot find 'Navigate up' button'",
                10
        );
        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find 'My lists' button",
                10
        );

        System.out.println(driver.getPageSource());

        waitForElementAndClick(
                By.xpath("//android.support.v7.widget.RecyclerView[@resource-id='org.wikipedia:id/reading_list_list']//android.widget.TextView[@text='Learning programming']"),
                "Cannot find 'Learning programming' list",
                10
        );

        swipeElementToLeft(
                By.xpath("//android.widget.TextView[@text='Java (programming language)']"),
                "Cannot swipe the element to left"
        );

        waitForElementNotPresent(
                By.xpath("//android.widget.TextView[@text='Java (programming language)']"),
                "Cannot delete saved article",
                5
        );

        String title_of_the_not_deleted_article = waitForElementAndGetAttribute(
                By.xpath("//android.widget.TextView[@text='Java (software platform)']"),
                "text",
                "Cannot get attribute of second article title",
                15
        );

        System.out.println(title_of_the_not_deleted_article);
        String expected_title = "Java (software platform)";

        Assert.assertEquals(expected_title, title_of_the_not_deleted_article);
    }

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

    private boolean isArticleHeaderLineContainsSearchText(By by, String search_text_value, String error_message, long timeoutInSeconds) {

        List<WebElement> lists = driver.findElements(by);

        for (WebElement list : lists) {

            if (!list.getAttribute("text").matches("(.*)search_text_value(.*)"))
                return false;
        }
        return true;
    };

    //Press on screen middle point and then swipe UP
    protected void swipeUP(int timeOfSwipe) {

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
    protected void swipeUpQuick() {
        swipeUP(200);
    }

    protected void swipeUpToFindElement(By by, String error_message, int max_swipes) {

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

    protected void swipeElementToLeft( By by, String error_message) {

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

    private int getAmountOfWebElements(By by) {
        List<WebElement> elements = driver.findElements(by);
        int a = elements.size();
        return a;
    }

    private void assertElementNotPresent(By by, String error_message) {

        int amount_of_elements = getAmountOfWebElements(by);
        if(amount_of_elements > 0) {
            String default_message = "An element '" + by.toString() + "' suppose to be not present" ;
            throw new AssertionError(default_message + " " + error_message);
        }
    }

    private String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeOutInSeconds) {

        WebElement element = waitForElementPresent(by, error_message, timeOutInSeconds);

        return element.getAttribute(attribute);
    }
}




