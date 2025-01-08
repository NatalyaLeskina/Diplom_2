import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.commons.lang3.RandomUtils.nextInt;

public class RegisterUserTest extends BaseTest {

    @Test
    @DisplayName("Check create user")
    public void checkCreateUser() {
        String email = "newemail" + nextInt(1000, 99999) + "@yandex.ru";
        String password = "password";
        String name = "name";

        User user = new User(email, password, name);

        Response response = Utils.sendPostRequestUserRegister(user);

        Utils.checkStatusCode(response, Utils.successStatusCode);
        Utils.checkSuccessField(response, true);

        Utils.deleteUser(email, password);
    }

    @Test
    @DisplayName("Check impossible creating identical user")
    public void checkImpossibleCreatingIdenticalUser() {
        String email = "newemail" + nextInt(1000, 99999) + "@yandex.ru";
        String password = "password";
        String name = "name";

        User user = new User(email, password, name);

        Response response = Utils.sendPostRequestUserRegister(user);
        Utils.checkStatusCode(response, Utils.successStatusCode);

        Response unsuccessfulResponse = Utils.sendPostRequestUserRegister(user);
        Utils.checkStatusCode(unsuccessfulResponse, Utils.failedForbiddenStatusCode);
        Utils.checkSuccessField(unsuccessfulResponse, false);
        Utils.checkResponseMessageField(unsuccessfulResponse, Utils.userAlreadyExistsMsg);

        Utils.deleteUser(email, password);
    }

    @Test
    @DisplayName("Check impossible creating without required field")
    public void checkImpossibleCreatingWithoutRequiredField() {
        String email = "newemail" + nextInt(1000, 99999) + "@yandex.ru";
        String password = "password";

        User user = new User(email, password);

        Response unsuccessfulResponse = Utils.sendPostRequestUserRegister(user);
        Utils.checkStatusCode(unsuccessfulResponse, Utils.failedForbiddenStatusCode);
        Utils.checkSuccessField(unsuccessfulResponse, false);
        Utils.checkResponseMessageField(unsuccessfulResponse, Utils.requiredFieldsMsg);
    }
}
