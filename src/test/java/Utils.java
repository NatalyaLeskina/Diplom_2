import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

public class Utils {

    static int successCreateStatusCode = 201;
    static int successStatusCode = 200;
    static int successAcceptedStatusCode = 202;
    static int failedConflictStatusCode = 409;
    static int failedClientStatusCode = 400;
    static int failedNotFoundStatusCode = 404;
    static int failedInternalServerErrorStatusCode = 500;
    static int failedForbiddenStatusCode = 403;
    static int failedUnauthorizedStatusCode = 401;

    static String userAlreadyExistsMsg = "User already exists";
    static String requiredFieldsMsg = "Email, password and name are required fields";
    static String incorrectEmailOrPassMsg = "email or password are incorrect";
    static String ingredientIdsMustBeProvidedMsg = "Ingredient ids must be provided";
    static String youShouldBeAuthorisedMsg = "You should be authorised";

    @Step("Check response message field")
    public static void checkResponseMessageField(Response response, String message) {
        response.then().assertThat().body("message", equalTo(message));
    }

    @Step("Send POST request to /api/auth/register")
    static Response sendPostRequestUserRegister(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/register");
        return response;
    }

    @Step("Check status code")
    static void checkStatusCode(Response response, int statusCode) {
        response.then().assertThat().statusCode(statusCode);
    }

    @Step("Check success field")
    static void checkSuccessField(Response response, boolean success) {
        response.then().assertThat().body("success", equalTo(success));
    }

    @Step("Send POST request to /api/auth/login")
    static Response sendPostRequestUserLogin(String email, String password) {
        User user = new User(email, password);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/login");
        return response;
    }

    @Step("Send POST request to /api/orders with token")
    static Response sendPostRequestCreateOrderWithToken(String[] ingredients, String accessToken) {
        Order order = new Order(ingredients);
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post("/api/orders");
        return response;
    }

    @Step("Send POST request to /api/orders without token")
    static Response sendPostRequestCreateOrderWithoutToken(String[] ingredients) {
        Order order = new Order(ingredients);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/orders");
        return response;
    }

    @Step("Send POST request to /api/orders without ingredients")
    static Response sendPostRequestCreateOrderWithoutIngredients() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .post("/api/orders");
        return response;
    }

    @Step("Check status done from create order response")
    public static void checkStatusDoneCreateOrderResponse(Response response) {
        response.then().assertThat().body("order.status", equalTo("done"));
    }

    @Step("Check status from create order response not exists")
    public static void checkStatusCreateOrderResponseNotExists(Response response) {
        response.then().assertThat().body("order", not(hasKey("yourFieldName")));
    }

    @Step("Check orders field exists")
    public static void checkOrdersFieldExists(Response response) {
        response.then().assertThat().body("$", hasKey("orders"));
    }

    @Step("Send PATCH request to /api/auth/user")
    static Response sendPatchRequestChangeUserDataWithToken(User user, String accessToken) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch("/api/auth/user");
        return response;
    }

    @Step("Send PATCH request to /api/auth/user")
    static Response sendPatchRequestChangeUserDataWithoutToken(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch("/api/auth/user");
        return response;
    }

    @Step("Get accessToken from auth")
    static String getAccessTokenFromAuth(String email, String password) {
        User user = new User(email, password);
        String accessToken = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
        return accessToken;
    }

    @Step("Get accessToken from register")
    static String getAccessTokenFromRegister(Response response) {
        String accessToken = response
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
        return accessToken;
    }

    @Step("Get ingredient")
    static String getIngredient(int index) {
        String ingredientId = given()
                .get("/api/ingredients")
                .then()
                .statusCode(200)
                .extract()
                .path("data[" + index + "]._id");
        return ingredientId;
    }

    @Step("Get orders without token")
    static Response getOrdersWithoutToken() {
        Response response = given()
                .get("/api/orders");
        return response;
    }

    @Step("Get orders with token")
    static Response getOrdersWithToken(String accessToken) {
        Response response = given()
                .header("Authorization", accessToken)
                .get("/api/orders");
        return response;
    }

    @Step("Send DELETE request to /api/auth/login")
    static void deleteUser(String email, String password) {
        String accessToken = getAccessTokenFromAuth(email, password);

        Response response = given()
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");

        checkStatusCode(response, successAcceptedStatusCode);
        checkSuccessField(response, true);
    }
}
