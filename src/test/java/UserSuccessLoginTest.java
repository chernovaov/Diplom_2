import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class UserSuccessLoginTest {
    private UserClient userClient;
    private User user;
    String accessToken;
    String refreshToken;

    @Before
    public void startUp() {
        user = User.getRandomEmailAndPassAndName();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Проверка, что существующий пользователь может залогиниться")
    @Description("Проверка: status code=200, токен пользователя не пустой")

    public void courierCanLogin() {
        userClient.createUser(user);
        ValidatableResponse login = userClient.loginUser(new User(user.email, user.password, user.name));

        boolean isUserLogin = login.extract().path("success");
        int actualStatusCode = login.extract().statusCode();
        accessToken = login.extract().path("accessToken");
        refreshToken = login.extract().path("refreshToken");

        assertEquals("Статус-код должен быть 200", SC_OK, actualStatusCode);
        assertTrue ("Пользователь должен авторизоваться", isUserLogin);

        assertThat("Инфо о пользователе не должно быть пустым", login.extract().path("user"), notNullValue());
        assertThat("accessToken не должен быть пустым", accessToken, notNullValue());
        assertThat("refreshToken не должен быть пустым", refreshToken, notNullValue());
    }
}
