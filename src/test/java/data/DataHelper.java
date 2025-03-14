package data;

import com.github.javafaker.Faker;
import page.LoginPage;

import java.util.Locale;
import static page.LoginPage.*;


public class DataHelper {

    private static Faker FAKER = new Faker(new Locale("en"));

    private DataHelper() {
    }

    public static String getValidCardNumber() {

        return "4444444444444441";
    }

    public static String getInvalidCardNumber() {

        return "4444444444444442";
    }

    //Генерация валидных данных

    public static String getValidMonth() {
        int number = FAKER.random().nextInt(2, 12);
        String formattedNumber = String.format("%02d", number);
        return formattedNumber;
    }

    public static Integer getValidYear() {

        return FAKER.random().nextInt(25, 30);
    }

    public static String getValidOwner() {

        return FAKER.name().fullName();
    }

    public static String getValidCVCCode() {

        return FAKER.numerify("###");
    }

    //Генерация невалидных данных


    public static String get15FiguresCardNumber() {
        return FAKER.numerify("###############");
    }
    public static void get17FiguresCardNumber() {
        LoginPage loginPage = new LoginPage();
        loginPage.cardNumber.setValue(FAKER.finance().creditCard());
        loginPage.cardNumber.append("#");
        loginPage.cardNumber.getValue().replaceAll("\\s", "").trim();
    }

    public static void getNoLettersAndCharactersInCardNumber() {
        LoginPage loginPage = new LoginPage();
        loginPage.cardNumber.setValue(FAKER.letterify("A@B#C*D").replaceAll("\\s", "").trim());
    }

    public static String get00Month() {
        return "00";
    }

    public static int getInvalidMonth() {
        return FAKER.number().numberBetween(13, 99);
    }

    public static void getNoLettersAndCharactersInMonth() {
        LoginPage loginPage = new LoginPage();
        loginPage.month.setValue(FAKER.letterify("A@B#C*D").replaceAll("\\s", "").trim());
    }

    public static String getInvalidYear() {
        int number = FAKER.random().nextInt(0, 24);
        String formattedNumber = String.format("%02d", number);
        return formattedNumber;
    }
    public static void getNoLettersAndCharactersInYear() {
        LoginPage loginPage = new LoginPage();
        loginPage.year.setValue(FAKER.letterify("A@B#C*D").replaceAll("\\s", "").trim());
    }

    public static String getInvalidOwner() {
        FAKER = new Faker(new Locale("ru"));
        return FAKER.name().fullName();
    }

    public static String getNoLowerCaseLettersInOwner() {
        return "ivan petrov";
    }

    public static String getNoDashInOwner() {
        return FAKER.name().firstName();
    }

    public static void getNoFiguresAndCharactersInOwner() {
        LoginPage loginPage = new LoginPage();
        loginPage.owner.setValue(FAKER.letterify("1@2#3*4").replaceAll("\\s", "").trim());
    }

    public static String getInvalidCVCCodeWith2Figures() {
        return FAKER.numerify("##");
    }

    public static void get4FiguresInCVCCode() {
        LoginPage loginPage = new LoginPage();
        loginPage.codeCVC.setValue(FAKER.numerify("###"));
        loginPage.codeCVC.append("#");
        loginPage.codeCVC.getValue().replaceAll("\\s", "").trim();
    }

    public static void getNoLettersAndCharactersInCVCCode() {
        LoginPage loginPage = new LoginPage();
        loginPage.codeCVC.setValue(FAKER.letterify("A@B#C*D").replaceAll("\\s", "").trim());
    }

    public static String getMonth01_2025() {
        return "01";
    }

    public static String getYear01_2025() {
        return "25";
    }

}


