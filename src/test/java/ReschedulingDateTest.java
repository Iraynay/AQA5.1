import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.conditions.Visible;
import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
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
    private static Faker faker;

    @BeforeAll
    static void setUpAll() {
        faker = new Faker(new Locale("ru"));
    }


    private void printTestData(String name, String phone, String city) {
        System.out.println(StringUtils.repeat("=", 30));
        System.out.println(name + "\n" + phone + "\n" + city);
        System.out.println(StringUtils.repeat("=", 30));
    }

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    String planningDate1 = generateDate(5);
    String planningDate2 = generateDate(6);

    @Test
    void shouldReturnOkMessage() {
        String name1 = DataGenerator.RequestForDelivery.generateInfo("ru").getFirstName() + " " +
                DataGenerator.RequestForDelivery.generateInfo("ru").getLastName();
        String phone1 = DataGenerator.RequestForDelivery.generateInfo("ru").getPhone();
        String city1 = DataGenerator.RequestForDelivery.generateInfo("ru").getCity();

        printTestData(name1, phone1, city1);

        open("http://localhost:9999");
        $x("//input[@placeholder='Город']").setValue(city1);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//input[@placeholder='Дата встречи']").setValue(planningDate1);
        $x("//input[@name='name']").setValue(name1);
        $x("//input[@name='phone']").setValue(phone1);
        $("[data-test-id='agreement']").click();
        $x("//span[contains(text(),'планировать')]").click();
        $x("//div[@class='notification__content']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate1));


        open("http://localhost:9999");
        $x("//input[@placeholder='Город']").setValue(city1);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//input[@placeholder='Дата встречи']").setValue(planningDate2);
        $x("//input[@name='name']").setValue(name1);
        $x("//input[@name='phone']").setValue(phone1);
        $("[data-test-id='agreement']").click();
        $x("//span[contains(text(),'планировать')]").click();
        $x("//span[contains(text(),'Перепланировать')]").click();
        $x("//div[@class='notification__content']")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(Condition.text("Встреча успешно запланирована на "));
    }
}