import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class UserAuthChangeInfoTest {
    private User user;
    private UserClient userClient;
    String accessToken;

    @Before
    public void startUp() {
        user = User.getRandomEmailAndPassAndName();
        userClient = new UserClient();
        userClient.createUser(user);
        ValidatableResponse login = userClient.loginUser(new User(user.email, user.password, user.name));
        accessToken = login.extract().path("accessToken");
    }
    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Проверка, что авторизованный пользователь может поменять Почту и успешно залогиниться с ней")
    @Description("Проверка: status code=200")
    public void userChangeEmail (){

        String newEmail = User.getUserEmailOnly().email.toLowerCase();

        ValidatableResponse response = userClient.changeUserInfo(accessToken, new User(newEmail, user.password, user.name));

        int actualStatusCode = response.extract().statusCode();
        boolean isUserEmailChanged = response.extract().path("success");

        ValidatableResponse loginWithNewEmail = userClient.loginUser(new User(newEmail, user.password, user.name));
        int actualStatusCodeForLoginWithNewEmail = loginWithNewEmail.extract().statusCode();

        assertEquals("Статус-код должен быть 200",  SC_OK, actualStatusCode);
        assertTrue ("Почта должна измениться", isUserEmailChanged);
        assertEquals("Статус-код при логине с новой почтой должен быть 200", SC_OK, actualStatusCodeForLoginWithNewEmail);

    }

    @Test
    @DisplayName("Проверка, что авторизованный пользователь может поменять Пароль и успешно с ним залогиниться")
    @Description("Проверка: status code=200")
    public void userChangePassword (){
        String newPassword= User.getUserPasswordOnly().password;

        ValidatableResponse response = userClient.changeUserInfo(accessToken, new User(user.email, newPassword, user.name));
        int actualStatusCode = response.extract().statusCode();
        boolean isUserPasswordChanged = response.extract().path("success");

        ValidatableResponse loginWithNewPass = userClient.loginUser(new User(user.email, newPassword, user.name));
        int actualStatusCodeForLoginWithNewPass = loginWithNewPass.extract().statusCode();

        assertEquals("Статус-код при смене пароля должен быть 200", SC_OK, actualStatusCode);
        assertTrue ("Пароль должен измениться", isUserPasswordChanged);
        assertEquals("Статус-код при логине с новым паролем должен быть 200",SC_OK, actualStatusCodeForLoginWithNewPass);

    }

    @Test
    @DisplayName("Проверка, что авторизованный пользователь может поменять Имя")
    @Description("Проверка: status code=200")
    public void userChangeName (){
        String oldName = user.name;
        String newName= User.getUserNameOnly().name.toLowerCase();

        ValidatableResponse response = userClient.changeUserInfo(accessToken, new User(user.email, user.password, newName));

        int actualStatusCode = response.extract().statusCode();
        boolean isUserNameChanged = response.extract().path("success");

        HashMap<String, String> userWithNewName = userClient.getUserInfo(accessToken).extract().path("user");

        assertEquals("Статус-код должен быть 200",SC_OK, actualStatusCode);
        assertTrue ("Имя должно измениться", isUserNameChanged);
        assertNotEquals("Новое имя должно отличаться от старого", oldName, userWithNewName.get("name").toLowerCase());

    }
}
