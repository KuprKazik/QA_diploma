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
import static page.LoginPage.*;

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
        assertTrue(pageTitle.isDisplayed(), "Путешествие дня");
        assertTrue(pageImage.isDisplayed());
        assertTrue(firstButton.isDisplayed(), "Купить");
        assertTrue(secondButton.isDisplayed(), "Купить в кредит");
    }

    //Проверка позитивных сценариев и записи статусов в базы данных

    @Test
    void testLoginSuccessfulPaymentFormMySQL() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getConfirmationMessage();

        String retrievedStatus = DataSQL.MySQL.getStatusFromPaymentTable("APPROVED");
        assertNotNull(retrievedStatus);
        assertEquals("APPROVED", retrievedStatus);
        }
    @Test
    void testLoginSuccessfulPaymentFormPostgres() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getConfirmationMessage();

        String retrievedStatus = DataSQL.PostgreSQL.getStatusFromPaymentTable("APPROVED");
        assertNotNull(retrievedStatus);
        assertEquals("APPROVED", retrievedStatus);
    }


    @Test
    void testLoginSuccessfulCreditFormMySQL() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getConfirmationMessage();

        String retrievedStatus = DataSQL.MySQL.getStatusFromCreditTable("APPROVED");
        assertNotNull(retrievedStatus);
        assertEquals("APPROVED", retrievedStatus);
    }
    @Test
    void testLoginSuccessfulCreditFormPostgres() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getConfirmationMessage();

        String retrievedStatus = DataSQL.PostgreSQL.getStatusFromCreditTable("APPROVED");
        assertNotNull(retrievedStatus);
        assertEquals("APPROVED", retrievedStatus);
    }

    @Test
    void testLoginUnsuccessfulPaymentFormMySQL() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getInvalidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMessage();

        String retrievedStatus = DataSQL.MySQL.getStatusFromPaymentTable("DECLINED");
        assertNotNull(retrievedStatus);
        assertEquals("DECLINED", retrievedStatus);
    }
    @Test
    void testLoginUnsuccessfulPaymentFormPostgres() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getInvalidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMessage();

        String retrievedStatus = DataSQL.PostgreSQL.getStatusFromPaymentTable("DECLINED");
        assertNotNull(retrievedStatus);
        assertEquals("DECLINED", retrievedStatus);
    }

    @Test
    void testLoginUnsuccessfulCreditFormMySQL() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getInvalidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMessage();

        String retrievedStatus = DataSQL.MySQL.getStatusFromCreditTable("DECLINED");
        assertNotNull(retrievedStatus);
        assertEquals("DECLINED", retrievedStatus);
    }
    @Test
    void testLoginUnsuccessfulCreditFormPostgres() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getInvalidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMessage();

        String retrievedStatus = DataSQL.PostgreSQL.getStatusFromCreditTable("DECLINED");
        assertNotNull(retrievedStatus);
        assertEquals("DECLINED", retrievedStatus);
    }


    //Заполнение невалидными данными раздела "Оплата по карте"



    @Test
    void testNo15FiguresAllowedInCardNumberPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.get15FiguresCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorCardNumber();
    }

    @Test
    void testNo17FiguresAllowedInCardNumberPaymentForm() {
        LoginPage.paymentForm();
        DataHelper.get17FiguresCardNumber();
        String enteredValue = cardNumber.getValue();
        Assertions.assertTrue(enteredValue.length() == 19);
    }


    @Test
    void testNoBlankCardNumberPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue("");
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorCardNumber();
    }

    @Test
    void testNoLettersOrSpecialCharactersInCardNumberPaymentForm() {
        LoginPage.paymentForm();
        DataHelper.getNoLettersAndCharactersInCardNumber();
        String enteredValue = cardNumber.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }
    @Test
    void testNotAccepted01_2025PaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getMonth01_2025());
        year.setValue(DataHelper.getYear01_2025());
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMonth();
    }

    @Test
    void testNo00MonthAllowedPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.get00Month());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMonth();
    }

    @Test
    void testNo13MonthAllowedPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(String.valueOf(DataHelper.getInvalidMonth()));
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMonth();
    }

    @Test
    void testNoBlankMonthPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue("");
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMonth2();
    }

    @Test
    void testNoLettersOrSpecialCharactersInMonthPaymentForm() {
        LoginPage.paymentForm();
        DataHelper.getNoLettersAndCharactersInMonth();
        String enteredValue = month.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testInvalidYearPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(DataHelper.getInvalidYear());
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorYear1();
    }

    @Test
    void testNoBlankYearPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue("");
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorYear2();
    }

    @Test
    void testNoLettersOrSpecialCharactersInYearPaymentForm() {
        LoginPage.paymentForm();
        DataHelper.getNoLettersAndCharactersInYear();
        String enteredValue = year.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoRussianLettersAllowedInOwnerPaymentForm() {
        LoginPage.paymentForm();
        owner.setValue(DataHelper.getInvalidOwner());
        String enteredText = owner.getValue();
        boolean containsRussianLetters = enteredText.matches(".*[а-яА-ЯёЁ].*");
        assertFalse(containsRussianLetters);
    }

    @Test
    void testUpperCaseInputInOwnerPaymentForm() {
        LoginPage.paymentForm();
        owner.setValue(DataHelper.getNoLowerCaseLettersInOwner());
        String enteredText = owner.getValue();
        assertEquals("IVAN PETROV", enteredText);
    }

    @Test
    void testInvalidOwnerWithoutDashPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getNoDashInOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorOwner1();
    }

    @Test
    void testNoFiguresOrSpecialCharactersInOwnerPaymentForm() {
        LoginPage.paymentForm();
        DataHelper.getNoFiguresAndCharactersInOwner();
        String enteredValue = owner.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoBlankOwnerPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue("");
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorOwner2();
    }

    @Test
    void testInvalidCodeCVC2PaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getInvalidCVCCodeWith2Figures());
        loginButton.click();
        LoginPage.getErrorCVCCode();
    }

    @Test
    void testNoInput4FiguresInCodeCVCPaymentForm() {
        LoginPage.paymentForm();
        DataHelper.get4FiguresInCVCCode();
        String enteredValue = codeCVC.getValue();
        assert enteredValue.length() == 3;
    }

    @Test
    void testNoLettersOrSpecialCharactersInCodeCVCPaymentForm() {
        LoginPage.paymentForm();
        DataHelper.getNoLettersAndCharactersInCVCCode();
        String enteredValue = codeCVC.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoBlankCodeCVCPaymentForm() {
        LoginPage.paymentForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue("");
        loginButton.click();
        LoginPage.getErrorCVCCode();
    }



    //Заполнение невалидными данными раздела "Кредит по данным карты"

    @Test
    void testNo15FiguresAllowedInCardNumberCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.get15FiguresCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorCardNumber();
    }

    @Test
    void testNo17FiguresAllowedInCardNumberCreditForm() {
        LoginPage.creditForm();
        DataHelper.get17FiguresCardNumber();
        String enteredValue = cardNumber.getValue();
        Assertions.assertTrue(enteredValue.length() == 19);
    }


    @Test
    void testNoBlankCardNumberCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue("");
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorCardNumber();
    }

    @Test
    void testNoLettersOrSpecialCharactersInCardNumberCreditForm() {
        LoginPage.creditForm();
        DataHelper.getNoLettersAndCharactersInCardNumber();
        String enteredValue = cardNumber.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }
    @Test
    void testNotAccepted01_2025CreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getMonth01_2025());
        year.setValue(DataHelper.getYear01_2025());
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMonth();
    }

    @Test
    void testNo00MonthAllowedCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.get00Month());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMonth();
    }

    @Test
    void testNo13MonthAllowedCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(String.valueOf(DataHelper.getInvalidMonth()));
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMonth();
    }

    @Test
    void testNoBlankMonthCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue("");
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorMonth2();
    }

    @Test
    void testNoLettersOrSpecialCharactersInMonthCreditForm() {
        LoginPage.creditForm();
        DataHelper.getNoLettersAndCharactersInMonth();
        String enteredValue = month.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testInvalidYearCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(DataHelper.getInvalidYear());
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorYear1();
    }

    @Test
    void testNoBlankYearCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue("");
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorYear2();
    }

    @Test
    void testNoLettersOrSpecialCharactersInYearCreditForm() {
        LoginPage.creditForm();
        DataHelper.getNoLettersAndCharactersInYear();
        String enteredValue = year.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoRussianLettersAllowedInOwnerCreditForm() {
        LoginPage.creditForm();
        owner.setValue(DataHelper.getInvalidOwner());
        String enteredText = owner.getValue();
        boolean containsRussianLetters = enteredText.matches(".*[а-яА-ЯёЁ].*");
        assertFalse(containsRussianLetters);
    }

    @Test
    void testUpperCaseInputInOwnerCreditForm() {
        LoginPage.creditForm();
        owner.setValue(DataHelper.getNoLowerCaseLettersInOwner());
        String enteredText = owner.getValue();
        assertEquals("IVAN PETROV", enteredText);
    }

    @Test
    void testInvalidOwnerWithoutDashCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getNoDashInOwner());
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorOwner1();
    }

    @Test
    void testNoFiguresOrSpecialCharactersInOwnerCreditForm() {
        LoginPage.creditForm();
        DataHelper.getNoFiguresAndCharactersInOwner();
        String enteredValue = owner.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoBlankOwnerCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue("");
        codeCVC.setValue(DataHelper.getValidCVCCode());
        loginButton.click();
        LoginPage.getErrorOwner2();
    }

    @Test
    void testInvalidCodeCVC2CreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue(DataHelper.getInvalidCVCCodeWith2Figures());
        loginButton.click();
        LoginPage.getErrorCVCCode();
    }

    @Test
    void testNoInput4FiguresInCodeCVCCreditForm() {
        LoginPage.creditForm();
        DataHelper.get4FiguresInCVCCode();
        String enteredValue = codeCVC.getValue();
        assert enteredValue.length() == 3;
    }

    @Test
    void testNoLettersOrSpecialCharactersInCodeCVCCreditForm() {
        LoginPage.creditForm();
        DataHelper.getNoLettersAndCharactersInCVCCode();
        String enteredValue = codeCVC.getValue();
        Assertions.assertTrue(enteredValue.isEmpty());
    }

    @Test
    void testNoBlankCodeCVCCreditForm() {
        LoginPage.creditForm();
        cardNumber.setValue(DataHelper.getValidCardNumber());
        month.setValue(DataHelper.getValidMonth());
        year.setValue(String.valueOf(DataHelper.getValidYear()));
        owner.setValue(DataHelper.getValidOwner());
        codeCVC.setValue("");
        loginButton.click();
        LoginPage.getErrorCVCCode();
    }


}
