import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.commons.lang3.RandomUtils.nextInt;

@RunWith(Parameterized.class)
public class ChangeUserDataUnsuccessParameterizedTest extends BaseTest {

    private final User user;
    private final User changeUser;

    public ChangeUserDataUnsuccessParameterizedTest(User user, User changeUser) {
        this.user = user;
        this.changeUser = changeUser;
    }

    @Parameterized.Parameters
    public static Object[][] getColors() {
        return new Object[][] {
                { new User("mail100" + nextInt(1000, 99999) + "@abc.ru", "password","name"), new User("mail100" + nextInt(1000, 99999) + "@abc.ru", "password","newname") },
                { new User("mail100" + nextInt(1000, 99999) + "@abc.ru", "password","name"), new User("mail100" + nextInt(1000, 99999) + "@abc.ru", "newpass","name") },
                { new User("mail100" + nextInt(1000, 99999) + "@abc.ru", "password","name"), new User("newmail100" + nextInt(1000, 99999) + "@abc.ru", "password","name") }
        };
    }

    @Test
    @DisplayName("Check unsuccessful change user data")
    public void checkUnsuccessfulChangeUserData() {
        Response registerResponse = Utils.sendPostRequestUserRegister(user);
        Utils.checkStatusCode(registerResponse, Utils.successStatusCode);

        Response changeUserDataResponse = Utils.sendPatchRequestChangeUserDataWithoutToken(changeUser);
        Utils.checkStatusCode(changeUserDataResponse, Utils.failedUnauthorizedStatusCode);
        Utils.checkSuccessField(changeUserDataResponse, false);

        Utils.deleteUser(user.getEmail(), user.getPassword());
    }
}
