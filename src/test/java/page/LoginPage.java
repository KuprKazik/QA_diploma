package page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPage {

    public static final SelenideElement pageTitle = $("h2");
    public static final SelenideElement pageImage = $("div.Order_cardPreview__47B2k");
    public static final SelenideElement firstButton = $("button:nth-child(3)");
    public static final SelenideElement secondButton = $("#root > div > button.button.button_view_extra.button_size_m.button_theme_alfa-on-white");
    public static final SelenideElement formTitle = $("#root > div > h3");
    public static final SelenideElement form = $("form");

    public static final SelenideElement cardNumber = $("div:nth-child(1) > span > span > span.input__box > input");
    public static final SelenideElement month = $("div:nth-child(2) > span > span:nth-child(1) > span > span > span.input__box > input");
    public static final SelenideElement year = $("div:nth-child(2) > span > span:nth-child(2) > span > span > span.input__box > input");
    public static final SelenideElement owner = $("div:nth-child(3) > span > span:nth-child(1) > span > span > span.input__box > input");
    public static final SelenideElement codeCVC = $("div:nth-child(3) > span > span:nth-child(2) > span > span > span.input__box > input");
    public static final SelenideElement loginButton = $("div:nth-child(4) > button");
    public static final SelenideElement confirmationMessage = $("div.notification.notification_visible.notification_status_ok.notification_has-closer.notification_stick-to_right.notification_theme_alfa-on-white > div.notification__content");

    public static final SelenideElement errorMessage = $("div.notification.notification_status_error.notification_has-closer.notification_stick-to_right.notification_theme_alfa-on-white > div.notification__content");
    public static final SelenideElement errorCardNumber = $("div:nth-child(1) > span > span > span.input__sub");
    public static final SelenideElement errorMonth = $("div:nth-child(2) > span > span:nth-child(1) > span > span > span.input__sub");
    public static final SelenideElement errorYear = $("div:nth-child(2) > span > span:nth-child(2) > span > span > span.input__sub");
    public static final SelenideElement errorOwner = $("div:nth-child(3) > span > span:nth-child(1) > span > span > span.input__sub");
    public static final SelenideElement errorCodeCVC = $("div:nth-child(3) > span > span:nth-child(2) > span > span > span.input__sub");


    public static void paymentForm() {
        firstButton.click();
        assertTrue(formTitle.isDisplayed(), "Оплата по карте");
        assertTrue(form.isDisplayed());
    }

    public static void creditForm() {
        secondButton.click();
        assertTrue(formTitle.isDisplayed(), "Кредит по данным карты");
        assertTrue(form.isDisplayed());
    }

    public static void getConfirmationMessage() {
        confirmationMessage.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Операция одобрена Банком."));
    }
    public static void getErrorMessage() {
        errorMessage.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
    }

    public static void getErrorCardNumber() {
        errorCardNumber.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }
    public static void getErrorMonth() {
        errorMonth.shouldHave(exactText("Неверно указан срок действия карты")).shouldBe(visible);
    }
    public static void getErrorMonth2() {
        errorMonth.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }
    public static void getErrorYear1() {
        errorYear.shouldHave(exactText("Истёк срок действия карты")).shouldBe(visible);
    }
    public static void getErrorYear2() {
        errorYear.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }
    public static void getErrorOwner1() {
        errorOwner.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }
    public static void getErrorOwner2() {
        errorOwner.shouldHave(exactText("Поле обязательно для заполнения")).shouldBe(visible);
    }
    public static void getErrorCVCCode() {
        errorCodeCVC.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

}
