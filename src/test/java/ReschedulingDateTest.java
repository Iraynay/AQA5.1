import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.conditions.Visible;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.DataGenerator;
import utils.UsersData;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.open;

import org.apache.commons.lang3.StringUtils.*;


public class ReschedulingDateTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldReturnOkMessage() {

        UsersData user1 = DataGenerator.RequestForDelivery.generateInfo("ru");
        String planningDate1 = DataGenerator.RequestForDelivery.generateDate(4);
        String planningDate2 = DataGenerator.RequestForDelivery.generateDate(6);


        open("http://localhost:9999");
        $x("//input[@placeholder='Город']").setValue(user1.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//input[@placeholder='Дата встречи']").setValue(planningDate1);
        $x("//input[@name='name']").setValue(user1.getFirstName() + " " + user1.getLastName());
        $x("//input[@name='phone']").setValue(user1.getPhone());
        $("[data-test-id='agreement']").click();
        $x("//span[contains(text(),'планировать')]").click();
        $x("//div[@class='notification__content']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate1));


        open("http://localhost:9999");
        $x("//input[@placeholder='Город']").setValue(user1.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//input[@placeholder='Дата встречи']").setValue(planningDate2);
        $x("//input[@name='name']").setValue(user1.getFirstName() + " " + user1.getLastName());
        $x("//input[@name='phone']").setValue(user1.getPhone());
        $("[data-test-id='agreement']").click();
        $x("//span[contains(text(),'планировать')]").click();
        $x("//span[contains(text(),'Перепланировать')]").click();
        $x("//div[@class='notification__content']")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate2));
    }
}