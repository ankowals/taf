package tests;

import base.UiBaseTest;
import org.testng.annotations.Test;
import pageobject.GooglePage;
import pageobject.SearchResultsPage;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;

public class UiTests extends UiBaseTest {

    @Test
    public void userCanSearch(){
        open("https://google.com/ncr");
        new GooglePage().searchFor("selenide");

        SearchResultsPage results = new SearchResultsPage();
        results.getResults().shouldHave(sizeGreaterThan(1));
        results.getResult(0).shouldHave(text("Selenide: concise UI tests in Java"));
    }

}
