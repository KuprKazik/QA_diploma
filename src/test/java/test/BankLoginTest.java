package test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.DataSQL;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static data.DataSQL.DatabaseCleaner.deleteAllDataFromMySQL;
import static data.DataSQL.DatabaseCleaner.deleteAllDataFromPostgres;
import static org.junit.jupiter.api.Assertions.*;

public class BankLoginTest {


    @BeforeAll
    static void setUpAll() {
        deleteAllDataFromMySQL();
        deleteAllDataFromPostgres();
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        deleteAllDataFromMySQL();
        deleteAllDataFromPostgres();
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {

        Configuration.browser = "chrome";
        open("http://localhost:8080");
        LoginPage loginPage = new LoginPage();
        loginPage.introductionForm();
    }

    //Проверка позитивных сценариев и записи статусов в базы данных

    @Test
    void testLoginSuccessfulPaymentFormMySQL() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.validData();
        loginPage.getConfirmationMessage();

        String retrievedStatus = DataSQL.MySQL.getStatusFromPaymentTable("APPROVED");
        assertNotNull(retrievedStatus);
        assertEquals("APPROVED", retrievedStatus);
    }

    @Test
    void testLoginSuccessfulPaymentFormPostgres() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.validData();
        loginPage.getConfirmationMessage();

        String retrievedStatus = DataSQL.PostgreSQL.getStatusFromPaymentTable("APPROVED");
        assertNotNull(retrievedStatus);
        assertEquals("APPROVED", retrievedStatus);
    }


    @Test
    void testLoginSuccessfulCreditFormMySQL() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.validData();
        loginPage.getConfirmationMessage();

        String retrievedStatus = DataSQL.MySQL.getStatusFromCreditTable("APPROVED");
        assertNotNull(retrievedStatus);
        assertEquals("APPROVED", retrievedStatus);
    }

    @Test
    void testLoginSuccessfulCreditFormPostgres() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.validData();
        loginPage.getConfirmationMessage();

        String retrievedStatus = DataSQL.PostgreSQL.getStatusFromCreditTable("APPROVED");
        assertNotNull(retrievedStatus);
        assertEquals("APPROVED", retrievedStatus);
    }

    @Test
    void testLoginUnsuccessfulPaymentFormMySQL() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getInvalidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMessage();

        String retrievedStatus = DataSQL.MySQL.getStatusFromPaymentTable("DECLINED");
        assertNotNull(retrievedStatus);
        assertEquals("DECLINED", retrievedStatus);
    }

    @Test
    void testLoginUnsuccessfulPaymentFormPostgres() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getInvalidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMessage();

        String retrievedStatus = DataSQL.PostgreSQL.getStatusFromPaymentTable("DECLINED");
        assertNotNull(retrievedStatus);
        assertEquals("DECLINED", retrievedStatus);
    }

    @Test
    void testLoginUnsuccessfulCreditFormMySQL() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getInvalidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMessage();

        String retrievedStatus = DataSQL.MySQL.getStatusFromCreditTable("DECLINED");
        assertNotNull(retrievedStatus);
        assertEquals("DECLINED", retrievedStatus);
    }

    @Test
    void testLoginUnsuccessfulCreditFormPostgres() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getInvalidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMessage();

        String retrievedStatus = DataSQL.PostgreSQL.getStatusFromCreditTable("DECLINED");
        assertNotNull(retrievedStatus);
        assertEquals("DECLINED", retrievedStatus);
    }


    //Заполнение невалидными данными раздела "Оплата по карте"


    @Test
    void testNo15FiguresAllowedInCardNumberPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.get15FiguresCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorCardNumber();
    }

    @Test
    void testNo17FiguresAllowedInCardNumberPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        DataHelper.get17FiguresCardNumber();
        String enteredValue = loginPage.cardNumber.getValue();
        Assertions.assertTrue(enteredValue.length() == 19);
    }


    @Test
    void testNoBlankCardNumberPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue("");
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorCardNumber();
    }

    @Test
    void testNoLettersOrSpecialCharactersInCardNumberPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        DataHelper.getNoLettersAndCharactersInCardNumber();
        String enteredValue = loginPage.cardNumber.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNo00MonthAllowedPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.get00Month());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMonth();
    }

    @Test
    void testNo13MonthAllowedPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(String.valueOf(DataHelper.getInvalidMonth()));
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMonth();
    }

    @Test
    void testNoBlankMonthPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue("");
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMonth2();
    }

    @Test
    void testNoLettersOrSpecialCharactersInMonthPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        DataHelper.getNoLettersAndCharactersInMonth();
        String enteredValue = loginPage.month.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNotAccepted01_2025PaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getMonth01_2025());
        loginPage.year.setValue(DataHelper.getYear01_2025());
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMonth();
    }

    @Test
    void testInvalidYearPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(DataHelper.getInvalidYear());
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorYear1();
    }

    @Test
    void testNoBlankYearPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue("");
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorYear2();
    }

    @Test
    void testNoLettersOrSpecialCharactersInYearPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        DataHelper.getNoLettersAndCharactersInYear();
        String enteredValue = loginPage.year.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoRussianLettersAllowedInOwnerPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.owner.setValue(DataHelper.getInvalidOwner());
        String enteredText = loginPage.owner.getValue();
        boolean containsRussianLetters = enteredText.matches(".*[а-яА-ЯёЁ].*");
        assertFalse(containsRussianLetters);
    }

    @Test
    void testUpperCaseInputInOwnerPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.owner.setValue(DataHelper.getNoLowerCaseLettersInOwner());
        String enteredText = loginPage.owner.getValue();
        assertEquals("IVAN PETROV", enteredText);
    }

    @Test
    void testInvalidOwnerWithoutDashPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getNoDashInOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorOwner1();
    }

    @Test
    void testNoFiguresOrSpecialCharactersInOwnerPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        DataHelper.getNoFiguresAndCharactersInOwner();
        String enteredValue = loginPage.owner.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoBlankOwnerPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue("");
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorOwner2();
    }

    @Test
    void testInvalidCodeCVC2PaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getInvalidCVCCodeWith2Figures());
        loginPage.loginButton.click();
        loginPage.getErrorCVCCode();
    }

    @Test
    void testNoInput4FiguresInCodeCVCPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        DataHelper.get4FiguresInCVCCode();
        String enteredValue = loginPage.codeCVC.getValue();
        assert enteredValue.length() == 3;
    }

    @Test
    void testNoLettersOrSpecialCharactersInCodeCVCPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        DataHelper.getNoLettersAndCharactersInCVCCode();
        String enteredValue = loginPage.codeCVC.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoBlankCodeCVCPaymentForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.paymentForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue("");
        loginPage.loginButton.click();
        loginPage.getErrorCVCCode();
    }


    //Заполнение невалидными данными раздела "Кредит по данным карты"

    @Test
    void testNo15FiguresAllowedInCardNumberCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.get15FiguresCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorCardNumber();
    }

    @Test
    void testNo17FiguresAllowedInCardNumberCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        DataHelper.get17FiguresCardNumber();
        String enteredValue = loginPage.cardNumber.getValue();
        Assertions.assertTrue(enteredValue.length() == 19);
    }


    @Test
    void testNoBlankCardNumberCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue("");
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorCardNumber();
    }

    @Test
    void testNoLettersOrSpecialCharactersInCardNumberCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        DataHelper.getNoLettersAndCharactersInCardNumber();
        String enteredValue = loginPage.cardNumber.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNo00MonthAllowedCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.get00Month());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMonth();
    }

    @Test
    void testNo13MonthAllowedCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(String.valueOf(DataHelper.getInvalidMonth()));
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMonth();
    }

    @Test
    void testNoBlankMonthCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue("");
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMonth2();
    }

    @Test
    void testNoLettersOrSpecialCharactersInMonthCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        DataHelper.getNoLettersAndCharactersInMonth();
        String enteredValue = loginPage.month.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNotAccepted01_2025CreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getMonth01_2025());
        loginPage.year.setValue(DataHelper.getYear01_2025());
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorMonth();
    }

    @Test
    void testInvalidYearCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(DataHelper.getInvalidYear());
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorYear1();
    }

    @Test
    void testNoBlankYearCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue("");
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorYear2();
    }

    @Test
    void testNoLettersOrSpecialCharactersInYearCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        DataHelper.getNoLettersAndCharactersInYear();
        String enteredValue = loginPage.year.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoRussianLettersAllowedInOwnerCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.owner.setValue(DataHelper.getInvalidOwner());
        String enteredText = loginPage.owner.getValue();
        boolean containsRussianLetters = enteredText.matches(".*[а-яА-ЯёЁ].*");
        assertFalse(containsRussianLetters);
    }

    @Test
    void testUpperCaseInputInOwnerCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.owner.setValue(DataHelper.getNoLowerCaseLettersInOwner());
        String enteredText = loginPage.owner.getValue();
        assertEquals("IVAN PETROV", enteredText);
    }

    @Test
    void testInvalidOwnerWithoutDashCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getNoDashInOwner());
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorOwner1();
    }

    @Test
    void testNoFiguresOrSpecialCharactersInOwnerCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        DataHelper.getNoFiguresAndCharactersInOwner();
        String enteredValue = loginPage.owner.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoBlankOwnerCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue("");
        loginPage.codeCVC.setValue(DataHelper.getValidCVCCode());
        loginPage.loginButton.click();
        loginPage.getErrorOwner2();
    }

    @Test
    void testInvalidCodeCVC2CreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue(DataHelper.getInvalidCVCCodeWith2Figures());
        loginPage.loginButton.click();
        loginPage.getErrorCVCCode();
    }

    @Test
    void testNoInput4FiguresInCodeCVCCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        DataHelper.get4FiguresInCVCCode();
        String enteredValue = loginPage.codeCVC.getValue();
        assert enteredValue.length() == 3;
    }

    @Test
    void testNoLettersOrSpecialCharactersInCodeCVCCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        DataHelper.getNoLettersAndCharactersInCVCCode();
        String enteredValue = loginPage.codeCVC.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoBlankCodeCVCCreditForm() {
        LoginPage loginPage = new LoginPage();
        loginPage.creditForm();
        loginPage.cardNumber.setValue(DataHelper.getValidCardNumber());
        loginPage.month.setValue(DataHelper.getValidMonth());
        loginPage.year.setValue(String.valueOf(DataHelper.getValidYear()));
        loginPage.owner.setValue(DataHelper.getValidOwner());
        loginPage.codeCVC.setValue("");
        loginPage.loginButton.click();
        loginPage.getErrorCVCCode();
    }


}
