package page;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class LoginPage {

    private final SelenideElement pageTitle = $("h2");
    private final SelenideElement pageImage = $("div.Order_cardPreview__47B2k");
    private final SelenideElement firstButton = $("button:nth-child(3)");
    private final SelenideElement secondButton = $("#root > div > button.button.button_view_extra.button_size_m.button_theme_alfa-on-white");
    private final SelenideElement formTitle = $("#root > div > h3");
    private final SelenideElement form = $("form");

    public final SelenideElement cardNumber = $("div:nth-child(1) > span > span > span.input__box > input");
    public final SelenideElement month = $("div:nth-child(2) > span > span:nth-child(1) > span > span > span.input__box > input");
    public final SelenideElement year = $("div:nth-child(2) > span > span:nth-child(2) > span > span > span.input__box > input");
    public final SelenideElement owner = $("div:nth-child(3) > span > span:nth-child(1) > span > span > span.input__box > input");
    public final SelenideElement codeCVC = $("div:nth-child(3) > span > span:nth-child(2) > span > span > span.input__box > input");
    public final SelenideElement loginButton = $("div:nth-child(4) > button");
    private final SelenideElement confirmationMessage = $("div.notification.notification_visible.notification_status_ok.notification_has-closer.notification_stick-to_right.notification_theme_alfa-on-white > div.notification__content");

    private final SelenideElement errorMessage = $("div.notification.notification_status_error.notification_has-closer.notification_stick-to_right.notification_theme_alfa-on-white > div.notification__content");
    private final SelenideElement errorCardNumber = $("div:nth-child(1) > span > span > span.input__sub");
    private final SelenideElement errorMonth = $("div:nth-child(2) > span > span:nth-child(1) > span > span > span.input__sub");
    private final SelenideElement errorYear = $("div:nth-child(2) > span > span:nth-child(2) > span > span > span.input__sub");
    private final SelenideElement errorOwner = $("div:nth-child(3) > span > span:nth-child(1) > span > span > span.input__sub");
    private final SelenideElement errorCodeCVC = $("div:nth-child(3) > span > span:nth-child(2) > span > span > span.input__sub");


    // Проверяет отображение элементов на странице и ничего не возвращает
    public void introductionForm() {
        assertTrue(pageTitle.isDisplayed(), "Путешествие дня");
        assertTrue(pageImage.isDisplayed());
        assertTrue(firstButton.isDisplayed(), "Купить");
        assertTrue(secondButton.isDisplayed(), "Купить в кредит");
    }

    // Проверяет наличие формы оплаты и ничего не возвращает
    public void paymentForm() {
        firstButton.click();
        assertTrue(formTitle.isDisplayed(), "Оплата по карте");
        assertTrue(form.isDisplayed());
    }

    // Проверяет наличие формы кредита и ничего не возвращает
    public void creditForm() {
        secondButton.click();
        assertTrue(formTitle.isDisplayed(), "Кредит по данным карты");
        assertTrue(form.isDisplayed());
    }

    // Заполняет форму валидными данными и ничего не возвращает
    public void validData() {
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
    }

    // Проверяет наличие сообщения об одобрении операции и ничего не возвращает
    public void getConfirmationMessage() {
        confirmationMessage.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Операция одобрена Банком."));
    }

    // Проверяет наличие сообщения об ошибке операции и ничего не возвращает
    public void getErrorMessage() {
        errorMessage.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
    }

    // Проверяет наличие сообщения об ошибке в номере карты и ничего не возвращает
    public void getErrorCardNumber() {
        errorCardNumber.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    // Проверяет наличие сообщения об ошибке в месяце и ничего не возвращает
    public void getErrorMonth() {
        errorMonth.shouldHave(exactText("Неверно указан срок действия карты")).shouldBe(visible);
    }

    public void getErrorMonth2() {
        errorMonth.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    // Проверяет наличие сообщения об ошибке в годе и ничего не возвращает
    public void getErrorYear1() {
        errorYear.shouldHave(exactText("Истёк срок действия карты")).shouldBe(visible);
    }

    public void getErrorYear2() {
        errorYear.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    // Проверяет наличие сообщения об ошибке в написании владельца и ничего не возвращает
    public void getErrorOwner1() {
        errorOwner.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    public void getErrorOwner2() {
        errorOwner.shouldHave(exactText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    // Проверяет наличие сообщения об ошибке в коде CVC и ничего не возвращает
    public void getErrorCVCCode() {
        errorCodeCVC.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

}
