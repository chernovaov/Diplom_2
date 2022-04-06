import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
public class UserClient extends RestAssuredClient {
    private static final String REG_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String USER_INFO_PATH = "/api/auth/user";

    @Step("Зарегистрировать нового пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user).log().all()
                .when()
                .post(REG_PATH)
                .then().log().all();
    }

    @Step ("Залогиниться")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user).log().all()
                .when()
                .post(LOGIN_PATH)
                .then().log().all();
    }

    @Step ("Получить инфо о пользователе")
    public ValidatableResponse getUserInfo(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken.replace("Bearer ", "")).log().all()
                .when()
                .get(USER_INFO_PATH)
                .then().log().all();
    }

    @Step ("Изменить инфо о пользователе с токеном")
    public ValidatableResponse changeUserInfo(String accessToken, User user) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken.replace("Bearer ", "")).log().all()
                .body(user)
                .when()
                .patch(USER_INFO_PATH)
                .then().log().all();
    }

    @Step ("Изменить инфо о пользователе без токена")
    public ValidatableResponse changeUserInfoNoToken(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user).log().all()
                .when()
                .patch(USER_INFO_PATH)
                .then().log().all();
    }

    @Step ("Удалить пользователя")
    public void delete(String accessToken) {
        if (accessToken == null) {
            return;
        }
        given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken.replace("Bearer ", "")).log().all()
                .when()
                .delete(USER_INFO_PATH)
                .then().log().all();

    }
}
