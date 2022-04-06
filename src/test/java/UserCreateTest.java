import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class UserCreateTest {
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
    @Description("Проверка создания пользователя")
    public void userCanBeCreatedTest (){
        ValidatableResponse response = userClient.createUser(user);
        int actualStatusCode = response.extract().statusCode();
        boolean isUserCreated = response.extract().path("success");
        accessToken = response.extract().path("accessToken");
        refreshToken = response.extract().path("refreshToken");

        assertEquals ("Статус-код должен быть 200", SC_OK, actualStatusCode);
        assertTrue ("Пользователь должен создаться", isUserCreated);

        assertThat("Инфо о пользователе не должно быть пустым", response.extract().path("user"), notNullValue());
        assertThat("accessToken не должен быть пустым", accessToken, notNullValue());
        assertThat("refreshToken не должен быть пустым", refreshToken, notNullValue());
    }

}
