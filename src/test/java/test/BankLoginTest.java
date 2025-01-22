package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataSQL;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.LoginPage;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataSQL.DatabaseCleaner.deleteAllDataFromMySQL;
import static data.DataSQL.DatabaseCleaner.deleteAllDataFromPostgres;
import static org.junit.jupiter.api.Assertions.*;
import static page.LoginPage.*;

public class BankLoginTest {


    @BeforeAll
    static void setUpAll() throws SQLException {
        deleteAllDataFromMySQL();
        deleteAllDataFromPostgres();
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() throws SQLException {
        deleteAllDataFromMySQL();
        deleteAllDataFromPostgres();
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {

        Configuration.browser = "chrome";
        open("http://localhost:8080");
        assertTrue($("h2").isDisplayed(), "Путешествие дня");
        assertTrue($("div.Order_cardPreview__47B2k").isDisplayed());
        assertTrue($("button:nth-child(3)").isDisplayed(), "Купить");
        assertTrue($("#root > div > button.button.button_view_extra.button_size_m.button_theme_alfa-on-white").isDisplayed(), "Купить в кредит");
    }

    //Проверка позитивных сценариев

    @Test
    void testLoginSuccessfulPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        confirmationMessage.shouldBe(visible,Duration.ofSeconds(15)).shouldHave(exactText("Операция одобрена Банком."));
    }
    @Test
    void testLoginSuccessfulCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        confirmationMessage.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Операция одобрена Банком."));
    }

    @Test
    void testLoginUnsuccessfulPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 4442");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorMessage.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
    }
    @Test
    void testLoginUnsuccessfulCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 4442");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorMessage.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
    }

    // Проверка записи статусов в базы данных

    @Test
    public void testCheckApprovedStatusInMySQLFromPaymentForm() {
        try {
            String retrievedStatus = DataSQL.MySQL.getStatusFromPaymentTable("APPROVED");
            assertNotNull(retrievedStatus);
            assertEquals("APPROVED", retrievedStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testCheckApprovedStatusInPostgresFromPaymentForm() {
        try {
            String retrievedStatus = DataSQL.PostgreSQL.getStatusFromPaymentTable("APPROVED");
            assertNotNull(retrievedStatus);
            assertEquals("APPROVED", retrievedStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckApprovedStatusInMySQLFromCreditForm() {
        try {
            String retrievedStatus = DataSQL.MySQL.getStatusFromCreditTable("APPROVED");
            assertNotNull(retrievedStatus);
            assertEquals("APPROVED", retrievedStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testCheckApprovedStatusInPostgresFromCreditForm() {
        try {
            String retrievedStatus = DataSQL.PostgreSQL.getStatusFromCreditTable("APPROVED");
            assertNotNull(retrievedStatus);
            assertEquals("APPROVED", retrievedStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckDeclinedStatusInMySQLFromPaymentForm() {
        try {
            String retrievedStatus = DataSQL.MySQL.getStatusFromPaymentTable("DECLINED");
            assertNotNull(retrievedStatus);
            assertEquals("DECLINED", retrievedStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testCheckDeclinedStatusInPostgresFromPaymentForm() {
        try {
            String retrievedStatus = DataSQL.PostgreSQL.getStatusFromPaymentTable("DECLINED");
            assertNotNull(retrievedStatus);
            assertEquals("DECLINED", retrievedStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckDeclinedStatusInMySQLFromCreditForm() {
        try {
            String retrievedStatus = DataSQL.MySQL.getStatusFromCreditTable("DECLINED");
            assertNotNull(retrievedStatus);
            assertEquals("DECLINED", retrievedStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testCheckDeclinedStatusInPostgresFromCreditForm() {
        try {
            String retrievedStatus = DataSQL.PostgreSQL.getStatusFromCreditTable("DECLINED");
            assertNotNull(retrievedStatus);
            assertEquals("DECLINED", retrievedStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    //Заполнение невалидными данными раздела "Оплата по карте"



    @Test
    void testNo15FiguresAllowedInCardNumberPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 444");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorCardNumber.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    @Test
    void testNo17FiguresAllowedInCardNumberPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("1111 1111 1111 1111");
        cardNumber.append("1");
        String enteredValue = cardNumber.getValue().replaceAll("\\s", "").trim();
        assert (enteredValue).length() == 16;
    }


    @Test
    void testNoBlankCardNumberPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorCardNumber.shouldHave(exactText("Неверный формат")).shouldBe(visible);

    }

    @Test
    void testNoLettersOrSpecialCharactersInCardNumberPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("1234ABCD@#$5678");
        cardNumber.shouldHave(Condition.value("1234 5678"));
        String enteredValue = cardNumber.getValue().replaceAll("\\s", "").trim();
        Assertions.assertTrue(enteredValue.matches("\\d{8}"));
    }

    @Test
    void testNo00MonthAllowedPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("00");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorMonth.shouldHave(exactText("Неверно указан срок действия карты")).shouldBe(visible);
    }

    @Test
    void testNo13MonthAllowedPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("13");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorMonth.shouldHave(exactText("Неверно указан срок действия карты")).shouldBe(visible);
    }

    @Test
    void testNoBlankMonthPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorMonth.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    @Test
    void testNoLettersOrSpecialCharactersInMonthPaymentForm() {
        LoginPage.paymentForm();
        month.setValue("1D@B$2");
        month.shouldHave(Condition.value("12"));
        String enteredValue = month.getValue();
        Assertions.assertTrue(enteredValue.matches("\\d{2}"));
    }

    @Test
    void testInvalidYearPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("24");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorYear.shouldHave(exactText("Истёк срок действия карты")).shouldBe(visible);
    }

    @Test
    void testNoBlankYearPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorYear.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    @Test
    void testNoLettersOrSpecialCharactersInYearPaymentForm() {
        LoginPage.paymentForm();
        year.setValue("2D@B$5");
        year.shouldHave(Condition.value("25"));
        String enteredValue = year.getValue();
        Assertions.assertTrue(enteredValue.matches("\\d{2}"));
    }

    @Test
    void testNoRussianLettersAllowedInOwnerPaymentForm() {
        LoginPage.paymentForm();
        owner.setValue("ИВАН ПЕТРОВ");
        String enteredText = owner.getValue();
        boolean containsRussianLetters = enteredText.matches(".*[а-яА-ЯёЁ].*");
        assertFalse(containsRussianLetters);
    }

    @Test
    void testUpperCaseInputInOwnerPaymentForm() {
        LoginPage.paymentForm();
        owner.setValue("ivan petrov");
        String enteredText = owner.getValue();
        assertEquals("IVAN PETROV", enteredText);
    }

    @Test
    void testInvalidOwnerWithoutDashPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVANPETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorOwner.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    @Test
    void testNoFiguresOrSpecialCharactersInOwnerPaymentForm() {
        LoginPage.paymentForm();
        owner.setValue("2D@B$5");
        owner.shouldHave(Condition.value("DB"));
        String enteredValue = owner.getValue();
        Assertions.assertTrue(enteredValue.matches("\\d{2}"));
    }

    @Test
    void testNoBlankOwnerPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("");
        codeCVC.setValue("123");
        loginButton.click();
        errorOwner.shouldHave(exactText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    void testInvalidCodeCVC2PaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("12");
        loginButton.click();
        errorCodeCVC.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    @Test
    void testNoInput4FiguresInCodeCVCPaymentForm() {
        LoginPage.paymentForm();
        codeCVC.setValue("111");
        codeCVC.append("1");
        String enteredValue = codeCVC.getValue().replaceAll("\\s", "").trim();
        assert (enteredValue).length() == 3;
    }

    @Test
    void testNoLettersOrSpecialCharactersInCodeCVCPaymentForm() {
        LoginPage.paymentForm();
        codeCVC.setValue("2D@B$53");
        codeCVC.shouldHave(Condition.value("253"));
        String enteredValue = codeCVC.getValue();
        Assertions.assertTrue(enteredValue.matches("\\d{3}"));
    }

    @Test
    void testNoBlankCodeCVCPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("");
        loginButton.click();
        errorCodeCVC.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }



    //Заполнение невалидными данными раздела "Кредит по данным карты"




    @Test
    void testNo15FiguresAllowedInCardNumberCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 444");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorCardNumber.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    @Test
    void testNo17FiguresAllowedInCardNumberCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("1111 1111 1111 1111");
        cardNumber.append("1");
        String enteredValue = cardNumber.getValue().replaceAll("\\s", "").trim();
        assert (enteredValue).length() == 16;
    }

    @Test
    void testNoBlankCardNumberCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorCardNumber.shouldHave(exactText("Неверный формат")).shouldBe(visible);

    }

    @Test
    void testNoLettersOrSpecialCharactersInCardNumberCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("1234ABCD@#$5678");
        cardNumber.shouldHave(Condition.value("1234 5678"));
        String enteredValue = cardNumber.getValue().replaceAll("\\s", "").trim();
        Assertions.assertTrue(enteredValue.matches("\\d{8}"));
    }

    @Test
    void testNo00MonthAllowedCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("00");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorMonth.shouldHave(exactText("Неверно указан срок действия карты")).shouldBe(visible);
    }

    @Test
    void testNo13MonthAllowedCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("13");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorMonth.shouldHave(exactText("Неверно указан срок действия карты")).shouldBe(visible);
    }

    @Test
    void testNoBlankMonthCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorMonth.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    @Test
    void testNoLettersOrSpecialCharactersInMonthCreditForm() {
        LoginPage.creditForm();
        month.setValue("1D@B$2");
        month.shouldHave(Condition.value("12"));
        String enteredValue = month.getValue();
        Assertions.assertTrue(enteredValue.matches("\\d{2}"));
    }

    @Test
    void testInvalidYearCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("24");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorYear.shouldHave(exactText("Истёк срок действия карты")).shouldBe(visible);
    }

    @Test
    void testNoBlankYearCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorYear.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    @Test
    void testNoLettersOrSpecialCharactersInYearCreditForm() {
        LoginPage.creditForm();
        year.setValue("2D@B$5");
        year.shouldHave(Condition.value("25"));
        String enteredValue = year.getValue();
        Assertions.assertTrue(enteredValue.matches("\\d{2}"));
    }

    @Test
    void testNoRussianLettersAllowedInOwnerCreditForm() {
        LoginPage.creditForm();
        owner.setValue("ИВАН ПЕТРОВ");
        String enteredText = owner.getValue();
        boolean containsRussianLetters = enteredText.matches(".*[а-яА-ЯёЁ].*");
        assertFalse(containsRussianLetters);
    }

    @Test
    void testUpperCaseInputInOwnerCreditForm() {
        LoginPage.creditForm();
        owner.setValue("ivan petrov");
        String enteredText = owner.getValue();
        assertEquals("IVAN PETROV", enteredText);
    }

    @Test
    void testInvalidOwnerWithoutDashCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVANPETROV");
        codeCVC.setValue("123");
        loginButton.click();
        errorOwner.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    @Test
    void testNoFiguresOrSpecialCharactersInOwnerCreditForm() {
        LoginPage.creditForm();
        owner.setValue("2D@B$5");
        owner.shouldHave(Condition.value("DB"));
        String enteredValue = owner.getValue();
        Assertions.assertTrue(enteredValue.matches("\\d{2}"));
    }


    @Test
    void testNoBlankOwnerCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("");
        codeCVC.setValue("123");
        loginButton.click();
        errorOwner.shouldHave(exactText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    void testInvalidCodeCVC2CreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("12");
        loginButton.click();
        errorCodeCVC.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    @Test
    void testNoInput4FiguresInCodeCVCCreditForm() {
        LoginPage.creditForm();
        codeCVC.setValue("111");
        codeCVC.append("1");
        String enteredValue = codeCVC.getValue().replaceAll("\\s", "").trim();
        assert (enteredValue).length() == 3;
    }

    @Test
    void testNoLettersOrSpecialCharactersInCodeCVCCreditForm() {
        LoginPage.creditForm();
        codeCVC.setValue("2D@B$53");
        codeCVC.shouldHave(Condition.value("253"));
        String enteredValue = codeCVC.getValue();
        Assertions.assertTrue(enteredValue.matches("\\d{3}"));
    }

    @Test
    void testNoBlankCodeCVCCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("4444 4444 4444 4441");
        month.setValue("12");
        year.setValue("25");
        owner.setValue("IVAN PETROV");
        codeCVC.setValue("");
        loginButton.click();
        errorCodeCVC.shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }


}
