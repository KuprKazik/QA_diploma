package page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPage {

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
        $("button:nth-child(3)").click();
        assertTrue($("#root > div > h3").isDisplayed(), "Оплата по карте");
        assertTrue($("form").isDisplayed());
    }

    public static void creditForm() {
        $("#root > div > button.button.button_view_extra.button_size_m.button_theme_alfa-on-white").click();
        assertTrue($("#root > div > h3").isDisplayed(), "Кредит по данным карты");
        assertTrue($("form").isDisplayed());
    }
}
