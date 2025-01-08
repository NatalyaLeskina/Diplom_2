import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.commons.lang3.RandomUtils.nextInt;

@RunWith(Parameterized.class)
public class ChangeUserDataSuccessParameterizedTest extends BaseTest {

    private final User user;
    private final User changeUser;

    public ChangeUserDataSuccessParameterizedTest(User user, User changeUser) {
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
    @DisplayName("Check success change user data")
    public void checkSuccessChangeUserData() {
        Response registerResponse = Utils.sendPostRequestUserRegister(user);
        Utils.checkStatusCode(registerResponse, Utils.successStatusCode);

        String accessToken = Utils.getAccessTokenFromAuth(user.getEmail(), user.getPassword());
        Response changeUserDataResponse = Utils.sendPatchRequestChangeUserDataWithToken(changeUser, accessToken);
        Utils.checkStatusCode(changeUserDataResponse, Utils.successStatusCode);
        Utils.checkSuccessField(changeUserDataResponse, true);

        Utils.deleteUser(changeUser.getEmail(), changeUser.getPassword());
    }
}
