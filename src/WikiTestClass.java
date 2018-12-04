import lib.CoreTestCase;
import lib.ui.MainPageObject;
import lib.ui.SearchPageObject;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

public class WikiTestClass extends CoreTestCase {

    private MainPageObject MainPageObject;
    protected void setUp() throws Exception{

        // обращение к setUP() в JUnit
        super.setUp();

        MainPageObject = new MainPageObject(driver);
    }
//  -----testSearch name is for the JUnit; All test need to start with "test". JUnit is now able to see this test and start it

    @Test
    public void testSearch() {

        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.waitForSearchResult("Object-oriented programming language");

    }

    @Test
    public void testCancelSearch() {

        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.waitForCancelButtonToAppear();
        searchPageObject.clickCancelSearch();
        searchPageObject.waitForCancelButtonToDisappear();

    }

    @Test
    public void testCompareArticleTitle() {

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        String search_line = "Java";
        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_line,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Object-oriented programming language')]"),
                "Cannot find 'Object-oriented programming' topic",
                5
        );
        WebElement title_element = MainPageObject.waitForElementPresent(
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
//  ------verification of prompt text in search filed
    @Test
    public void testPlaceholderSearchPresent() {
        //verification that our prompt text in search inputField equals the value of "text" attribute in XML
        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' in input field",
                5
        );

        WebElement search_input_field_element = MainPageObject.waitForElementPresent(
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
//  ------print "Java" in Wiki search field and delete searched articles
    @Test
    public void testSearchAndDeleteSearchResults() {

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Seattle",
                "Cannot find search input",
                5
        );

        String search_line = "Java";
        String search_result_locator = "//*[@class='android.widget.LinearLayout']/*[@resource-id='org.wikipedia:id/page_list_item_image']";
        MainPageObject.waitForElementPresent(By.xpath(search_result_locator),
                "Cannot find anything by the request" + search_line,
                15
        );

        int amount = MainPageObject.getAmountOfWebElements(By.xpath(search_result_locator));
        System.out.println(amount);

        Assert.assertTrue("Number of articles more then 1", amount > 1);
        //delete search results (delete all searched articles from page)
        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X button",
                15
        );
    }
//  -----just for practice: different assertions:

//        Assert.assertTrue("Articles still present after deletion", amount_after_deletion < 1);
//        /* verification of presence of the element with "text" attribute  =  "Search and read the free encyclopedia in your language" -
//         so we can mark new search page
//        */
//        WebElement screen_element = waitForElementPresent(
//                By.id("org.wikipedia:id/search_empty_message"),
//                "Cannot find search message",
//                15
//        );
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
//        String search_prompt_text = search_input_field_element.getAttribute("text");
//        System.out.println(search_prompt_text);
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
//        String input_text_string = search_input_field_element.getAttribute("text");
//        System.out.println(input_text_string);
//
//        Assert.assertEquals("Java", input_text_string
//        );
//    ------Test mostly always failed - so not in all article's titles presents "Java"
//    @Test
//    public void testSearchForMatchingInArticlesHeaders() {
//       waitForElementAndClick(
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
//        Assert.assertTrue("Some searched articles does not have matching between header text and searched text value", result);
//    }
//    ---------swiping
    @Test
    public void testSwipeArticle() {

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Appium",
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Appium']"),
                "Cannot find Appium in Search",
                5
        );
        //long time swipe
        //swipeUP(2000);
        MainPageObject.swipeUpToFindElement(
                By.xpath("//android.widget.TextView[@text = 'View page in browser']"),
                "Cannot find the end of the article" ,
                20
        );
    }
//  -------- save article to list, then delete from list by swipe, then verification - waitForElementNotPresent()
    @Test
    public void testSaveFirstArticleToMyListAndDeleteBySwipe() {

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        String search_line = "Java";
        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_line,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Object-oriented programming language')]"),
                "Object-oriented programming language line",
                5
        );

        WebElement title_element = MainPageObject.waitForElementPresent(
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

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc ='More options']"),
                "Cannot find 'More options' button",
                15
                );

        MainPageObject.waitForElementPresent(
                By.xpath("//android.widget.TextView[@resource-id='org.wikipedia:id/title']"),
                "'More options' menu was not downloaded completely",
                15
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Add to reading list']"),
                "Cannot find 'Add to list' button",
                5
        );
        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find 'Cot it' button",
                5
        );

        MainPageObject.waitForElementAndClear(
                By.xpath("//android.widget.EditText[@text='My reading list']"),
                "Cannot clear text in 'Name of List' field",
                5
        );


        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//android.widget.EditText[@NAF='true']"),
                "Learning programming",
                "Can not enter list name",
                5
        );

        MainPageObject.waitForElementAndClick(
        By.xpath("//android.widget.Button[@text='OK']"),
        "Cannot find OK button, Can not close article",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot find X button",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find 'redirection to saved articles list' button",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.support.v7.widget.RecyclerView[@resource-id='org.wikipedia:id/reading_list_list']//android.widget.ImageView[@resource-id='org.wikipedia:id/item_image_1']"),
                "Cannot tap on article title",
                5
        );

        MainPageObject.swipeElementToLeft(
               By.xpath("//android.widget.TextView[@text='Java (programming language)']"),
               "Cannot find swipe article"
        );

        MainPageObject.waitForElementNotPresent(
               By.xpath("//android.widget.TextView[@text='Java (programming language)']"),
                "Cannot delete saved article",
                15
        );
    }
// ------выводим список статей, считаем размер и убеждаемся, что количество найденных статей больше 0
    @Test
    public void testAmountOfNotEmptySearch() {

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        String search_line = "Linkin Park Diskography";
        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//android.widget.EditText[@text='Search…']"),
                search_line,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementPresent(
                By.xpath("//android.widget.LinearLayout[@resource-id = 'org.wikipedia:id/page_list_item_container']"),
                "Cannot find anything by the request " + search_line,
                15
        );

        String search_result_locator = "//android.widget.LinearLayout[@resource-id = 'org.wikipedia:id/page_list_item_container']";
        int amount = MainPageObject.getAmountOfWebElements(By.xpath(search_result_locator));
        System.out.println(amount);

        Assert.assertTrue("Number of articles less then 1", amount > 0);
    }

    @Test
    public void testAmountOfEmptySearch() {

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        String search_line =" xvtsftcccvf";
        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//android.widget.EditText[@text='Search…']"),
                search_line,
                "Cannot find search input",
                5
        );

        String search_result_locator = "//android.widget.LinearLayout[@resource-id = 'org.wikipedia:id/page_list_item_container']";
        String empty_result_label = "//android.widget.TextView[@text='No results found']";
        MainPageObject.waitForElementPresent(
                By.xpath(empty_result_label ),
                "Cannot find empty result label by the request "+ search_line,
                15
        );

        MainPageObject.assertElementNotPresent(
                By.xpath(search_result_locator),
                "We found some results by request " + search_line
        );
    }
//  --------- change screen rotation - video Wiki.mp3 in src
    @Test
    public void testChangeScreenOrientationOnSearchResults() {

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        String search_line = "Java";
        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//android.widget.EditText[@text='Search…']"),
                search_line,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Object-oriented programming language')]"),
                "Cannot find 'Object-oriented programming' topic searching by " + search_line,
                15
        );

        String title_before_rotation = MainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );

        driver.rotate(ScreenOrientation.LANDSCAPE);

        String title_after_rotation = MainPageObject.waitForElementAndGetAttribute(
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

        String title_after_second_rotation = MainPageObject.waitForElementAndGetAttribute(
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

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementPresent(
                By.xpath("//*[contains(@text,'Object-oriented programming language')]"),
                "Object-oriented programming language line",
                5
        );

        // set 2 seconds for staying app in background
        driver.runAppInBackground(2);

        MainPageObject.waitForElementPresent(
                By.xpath("//*[contains(@text,'Object-oriented programming language')]"),
                "Cannot find article after returning from background",
                5
        );
    }
//  -------- save first article to list, return to search, then save second article to list, then delete first article from list by swipe,
//  -------- then verification that second article still in the list - waitForElementNotPresent() - video Wiki.mp4 in src
    @Test
    public void testSaveTwoArticlesAndThenDeleteOneArticle() {

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        String search_line = "java";
        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_line,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Object-oriented programming language')]"),
                "Cannot find 'Object-oriented programming language' line",
                5
        );

        WebElement title_element = MainPageObject.waitForElementPresent(
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

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc ='More options']"),
                "Cannot find 'More options' button",
                15
        );

        MainPageObject.waitForElementPresent(
                By.xpath("//android.widget.TextView[@resource-id='org.wikipedia:id/title']"),
                "'More options' menu was not downloaded completely",
                15
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Add to reading list']"),
                "Cannot find 'Add to list' button",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find 'Cot it' button",
                5
        );

        MainPageObject.waitForElementAndClear(
                By.xpath("//android.widget.EditText[@text='My reading list']"),
                "Cannot clear text in 'Name of List' field",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//android.widget.EditText[@NAF='true']"),
                "Learning programming",
                "Can not enter list name",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.Button[@text='OK']"),
                "Cannot find OK button, Can not close article",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot find X button",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find 'redirection to saved articles list' button",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Learning programming']"),
                "Cannot tap on article title",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Learning programming']"),
                "Cannot tap on article title",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot find 'Navigate up button'",
                10
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='Explore']"),
                "Cannot find 'Explore' button",
                10
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find element to init search",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_line,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Java (software platform)')]"),
                "Cannot find 'Object-oriented programming language' line",
                5
        );

        String article_title2 = title_element.getAttribute("text");
        Assert.assertEquals(
                "We see unexpected title",
                "Java (software platform)",
                article_title2
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc ='More options']"),
                "Cannot find 'More options' button",
                15
        );

        MainPageObject.waitForElementPresent(
                By.xpath("//android.widget.TextView[@resource-id='org.wikipedia:id/title']"),
                "'More options' menu was not downloaded completely",
                15
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Add to reading list']"),
                "Cannot find 'Add to list' button",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Learning programming']"),
                "Cannot find 'Learning programming' list",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot find 'Navigate up' button'",
                10
        );
        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find 'My lists' button",
                10
        );

        System.out.println(driver.getPageSource());

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.support.v7.widget.RecyclerView[@resource-id='org.wikipedia:id/reading_list_list']//android.widget.TextView[@text='Learning programming']"),
                "Cannot find 'Learning programming' list",
                10
        );

        MainPageObject.swipeElementToLeft(
                By.xpath("//android.widget.TextView[@text='Java (programming language)']"),
                "Cannot swipe the element to left"
        );

        MainPageObject.waitForElementNotPresent(
                By.xpath("//android.widget.TextView[@text='Java (programming language)']"),
                "Cannot delete saved article",
                5
        );

        String title_of_the_not_deleted_article = MainPageObject.waitForElementAndGetAttribute(
                By.xpath("//android.widget.TextView[@text='Java (software platform)']"),
                "text",
                "Cannot get attribute of second article title",
                15
        );
        System.out.println(title_of_the_not_deleted_article);
        String expected_title = "Java (software platform)";

        Assert.assertEquals(expected_title, title_of_the_not_deleted_article);
    }
// -Tест, который открывает статью и убеждается, что у нее есть элемент title.
// -Важно: тест не должен дожидаться появления title, проверка должна производиться сразу.
// -Если title не найден - тест падает с ошибкой.
    @Test
    public void testCheckArticleTitlePresent() {

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id ='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by ",
                5
        );
//        assertElementPresent(
//                By.id("org.wikipedia:id/view_page_title_text"),
//                "Cannot find article title immediately "
//        );
    }
}




