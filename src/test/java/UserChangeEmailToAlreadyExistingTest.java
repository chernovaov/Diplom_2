import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserChangeEmailToAlreadyExistingTest {
    private static User user;
    private static User secondUser;

    private UserClient userClient;

    String accessToken1;
    String accessToken2;

    @Before
    public void startUp() {
        user = User.getRandomEmailAndPassAndName();
        secondUser = User.getRandomEmailAndPassAndName();
        userClient = new UserClient();

    }
    @After
    public void tearDown() {
        userClient.delete(accessToken1);
        userClient.delete(accessToken2);
    }

    @Test
    @DisplayName("Проверка, что нельзя изменить почту пользователя на уже существующую")
    @Description("Проверка: status code=403,  message='User with such email already exists'")
    public void userCantChangeEmailToExisting (){
        final String expectedMessage = "User with such email already exists";
        ValidatableResponse regFirstUser= userClient.createUser(user);
        accessToken1 = regFirstUser.extract().path("accessToken");

        ValidatableResponse regSecondUser= userClient.createUser(secondUser);
        accessToken2 = regSecondUser.extract().path("accessToken");

        userClient.loginUser(new User(user.email, user.password, user.name));

        ValidatableResponse response = userClient.changeUserInfo(accessToken1, new User(secondUser.email, user.password, user.name));

        int actualStatusCode = response.extract().statusCode();
        boolean isUserInfoChanged = response.extract().path("success");
        String errorMessage = response.extract().path("message");

        assertEquals("Статус-код должен быть 403", actualStatusCode, SC_FORBIDDEN);
        assertFalse ("Почта не должна измениться на уже существующую", isUserInfoChanged);
        assertEquals("Неверный текст ошибки при попытке поменять почту на уже существующую", expectedMessage,errorMessage);

    }
}
