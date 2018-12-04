package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class SearchPageObject extends MainPageObject {

    // ----- задаем константы с Xpath - ами для search filed
    private static final String
            SEARCH_INIT_ELEMENT = "//*[contains(@text,'Search Wikipedia')]",
            SEARCH_INPUT = "//*[contains(@text, 'Search…')]",
            SEARCH_CANCEL_BUTTON = "org.wikipedia:id/search_close_btn",
            SEARCH_RESULT_BY_SUBSTRING_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']";

    // ------берем driver из MainPageObject

    public SearchPageObject(AppiumDriver driver) {

        super(driver);
    }

    /* TEMPLATES METHODS */

    // ----- метод, который заменяет строки : {SUBSTRING} to substring. Этот метод называется методом шаблонов - заменяем значение по щаблону на другое

    private static String getResultSearchElement(String substring) {

        return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
    }
    /* TEMPLATES METHODS */


    // ---- метод, который инициализирует процесс поиска
    public void initSearchInput() {

        this.waitForElementAndClick(By.xpath(SEARCH_INIT_ELEMENT), "Cannot find and click search init element", 5);
        this.waitForElementPresent(By.xpath(SEARCH_INIT_ELEMENT), "Cannot find search input after clocking search init element", 5);
    }

    public void waitForCancelButtonToAppear() {

        this.waitForElementPresent(By.id(SEARCH_CANCEL_BUTTON ), "Cannot find search cancel button", 5);

    }

    public void waitForCancelButtonToDisappear() {

        this.waitForElementNotPresent(By.id(SEARCH_CANCEL_BUTTON ), "Search cancel button is still present", 5);

    }

    public void clickCancelSearch() {

        this.waitForElementAndClick(By.id(SEARCH_CANCEL_BUTTON), "Cannot find and click search cancel button", 5);
    }

    // ---- метод, который осуществляет поиск по заданной SEARCH_LINE
    public void typeSearchLine(String search_line) {

        this.waitForElementAndSendKeys(By.xpath(SEARCH_INPUT), search_line, "Cannot find and type into search input", 5);
    }

    // ------метод, который убеждается, что поиск прошел правильно
    public void waitForSearchResult(String substring) {

        String search_result_xpath = getResultSearchElement(substring);

        this.waitForElementPresent(By.xpath(search_result_xpath), "Cannot find search result with substring " + substring, 15);
    }
}
