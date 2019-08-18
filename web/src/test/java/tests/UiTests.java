package tests;

import base.UiBaseTest;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;
import pageobject.GooglePage;
import pageobject.SearchResultsPage;

import java.time.LocalDateTime;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;
import static io.qala.datagen.RandomDate.*;
import static io.qala.datagen.RandomValue.between;
import static io.qala.datagen.RandomValue.length;
import static io.qala.datagen.StringModifier.Impls.spaces;
import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class UiTests extends UiBaseTest {

    @Test
    public void userCanSearch(){
        open("https://google.com/ncr");
        new GooglePage().searchFor("selenide");

        SearchResultsPage results = new SearchResultsPage();
        results.getResults().shouldHave(sizeGreaterThan(1));
        results.getResult(0).shouldHave(text("Selenide: concise UI tests in Java"));
    }

    @Test
    public void userCantSearch(){
        open("https://google.com/ncr");
        new GooglePage().searchFor("selenide");

        SearchResultsPage results = new SearchResultsPage();
        results.getResults().shouldHave(sizeGreaterThan(1));
        results.getResult(0).shouldHave(text("That is not what i would expect"));
    }

    @Test
    public void userCantSearchTheSecond(){
        open("https://google.com/ncr");
        new GooglePage().searchFor("failing selenide test");

        SearchResultsPage results = new SearchResultsPage();
        results.getResults().shouldHave(sizeGreaterThan(1));
        results.getResult(0).shouldHave(text("a dummy text"));
    }

    @Test
    public void checkRandomDataGeneration(){
        String randomString = length(10).with(spaces()).english();
        int randomInt = between(0, 100).integer();
        LocalDateTime randomDate = beforeNow().localDateTime();

        log.debug("randomString is " + randomString);
        assertThat(randomString).containsWhitespaces();

        log.debug("randomInt is " + randomInt);
        assertThat(randomInt).isBetween(0,100);

        log.debug("randomDate is " + randomDate);
        assertThat(randomDate).isBefore(LocalDateTime.now());

    }


}
