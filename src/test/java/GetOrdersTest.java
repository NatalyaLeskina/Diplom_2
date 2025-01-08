import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.commons.lang3.RandomUtils.nextInt;

public class GetOrdersTest extends BaseTest {

    @Test
    @DisplayName("Check get orders without token")
    public void checkGetOrdersWithoutToken() {
        Response getOrdersResponse = Utils.getOrdersWithoutToken();
        Utils.checkStatusCode(getOrdersResponse, Utils.failedUnauthorizedStatusCode);
        Utils.checkResponseMessageField(getOrdersResponse, Utils.youShouldBeAuthorisedMsg);
    }

    @Test
    @DisplayName("Check get orders with token")
    public void checkGetOrdersWithToken() {
        String ingredient1 = Utils.getIngredient(0);
        String ingredient2 = Utils.getIngredient(1);
        String[] ingredients = {ingredient1, ingredient2};

        String email = "newemail" + nextInt(1000, 99999) + "@yandex.ru";
        String password = "password";
        String name = "name";

        User user = new User(email, password, name);

        Response registerResponse = Utils.sendPostRequestUserRegister(user);
        Utils.checkStatusCode(registerResponse, Utils.successStatusCode);

        String accessToken = Utils.getAccessTokenFromRegister(registerResponse);
        Response createOrderResponse = Utils.sendPostRequestCreateOrderWithToken(ingredients, accessToken);
        Utils.checkStatusCode(createOrderResponse, Utils.successStatusCode);

        Response getOrdersResponse = Utils.getOrdersWithToken(accessToken);
        Utils.checkStatusCode(getOrdersResponse, Utils.successStatusCode);
        Utils.checkSuccessField(getOrdersResponse, true);
        Utils.checkOrdersFieldExists(getOrdersResponse);
    }
}
