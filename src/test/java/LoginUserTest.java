import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.commons.lang3.RandomUtils.nextInt;

public class LoginUserTest extends BaseTest {

    @Test
    @DisplayName("Check login user")
    public void checkLoginUser() {
        String email = "newemail" + nextInt(1000, 99999) + "@yandex.ru";
        String password = "password";
        String name = "name";

        User user = new User(email, password, name);

        Response registerResponse = Utils.sendPostRequestUserRegister(user);
        Utils.checkStatusCode(registerResponse, Utils.successStatusCode);

        Response loginResponse = Utils.sendPostRequestUserLogin(email, password);
        Utils.checkStatusCode(loginResponse, Utils.successStatusCode);
        Utils.checkSuccessField(loginResponse, true);

        Utils.deleteUser(email, password);
    }

    @Test
    @DisplayName("Check unsuccessful login with incorrect pass")
    public void checkUnsuccessfulLoginWithIncorrectPass() {
        String email = "newemail" + nextInt(1000, 99999) + "@yandex.ru";
        String password = "password";
        String incorrectPass = "incorrect";
        String name = "name";

        User user = new User(email, password, name);

        Response registerResponse = Utils.sendPostRequestUserRegister(user);
        Utils.checkStatusCode(registerResponse, Utils.successStatusCode);

        Response loginResponse = Utils.sendPostRequestUserLogin(email, incorrectPass);
        Utils.checkStatusCode(loginResponse, Utils.failedUnauthorizedStatusCode);
        Utils.checkSuccessField(loginResponse, false);
        Utils.checkResponseMessageField(loginResponse, Utils.incorrectEmailOrPassMsg);

        Utils.deleteUser(email, password);
    }
}
