import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.commons.lang3.RandomUtils.nextInt;

public class CreateOrderTest extends BaseTest {

    @Test
    @DisplayName("Check create order with token")
    public void checkCreateOrderWithToken() {
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
        Utils.checkSuccessField(createOrderResponse, true);
        Utils.checkStatusDoneCreateOrderResponse(createOrderResponse);

        Utils.deleteUser(email, password);
    }

    @Test
    @DisplayName("Check create order without token")
    public void checkCreateOrderWithoutToken() {
        String ingredient1 = Utils.getIngredient(0);
        String ingredient2 = Utils.getIngredient(1);
        String[] ingredients = {ingredient1, ingredient2};

        Response createOrderResponse = Utils.sendPostRequestCreateOrderWithoutToken(ingredients);
        Utils.checkStatusCode(createOrderResponse, Utils.successStatusCode);
        Utils.checkSuccessField(createOrderResponse, true);
        Utils.checkStatusCreateOrderResponseNotExists(createOrderResponse);
    }

    @Test
    @DisplayName("Check create order without ingredients")
    public void checkCreateOrderWithoutIngredients() {
        Response createOrderResponse = Utils.sendPostRequestCreateOrderWithoutIngredients();
        Utils.checkStatusCode(createOrderResponse, Utils.failedClientStatusCode);
        Utils.checkSuccessField(createOrderResponse, false);
        Utils.checkResponseMessageField(createOrderResponse, Utils.ingredientIdsMustBeProvidedMsg);
    }

    @Test
    @DisplayName("Check create order with wrong ingredient")
    public void checkCreateOrderWithWrongIngredient() {
        String[] ingredients = {"wrongingredient"};

        Response createOrderResponse = Utils.sendPostRequestCreateOrderWithoutToken(ingredients);
        Utils.checkStatusCode(createOrderResponse, Utils.failedInternalServerErrorStatusCode);
    }
}
