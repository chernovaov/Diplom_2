import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class UserNonAuthChangeInfoTest {
    private static User user;
    private UserClient userClient;
    String accessToken;

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
    @DisplayName("Проверка, что неавторизованный пользователь не может поменять Почту")
    @Description("Проверка: status code=401")
    public void notChangeEmail (){
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        String newEmail = User.getUserEmailOnly().email.toLowerCase();

        ValidatableResponse changeEmail = userClient.changeUserInfoNoToken(new User(newEmail, user.password, user.name));
        int actualStatusCode = changeEmail.extract().statusCode();
        boolean isUserEmailChanged = changeEmail.extract().path("success");

        assertEquals("Статус-код должен быть 401", SC_UNAUTHORIZED, actualStatusCode);
        assertFalse ("Почта не должна измениться", isUserEmailChanged);
    }

    @Test
    @DisplayName("Проверка, что неавторизованный пользователь не может поменять Пароль")
    @Description("Проверка: status code=401")
    public void notChangePassword (){
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        String newPassword= User.getUserPasswordOnly().password;

        ValidatableResponse changePass = userClient.changeUserInfoNoToken(new User(user.email, newPassword, user.name));
        int actualStatusCode = changePass.extract().statusCode();
        boolean isUserPasswordChanged = changePass.extract().path("success");

        assertEquals("Статус-код должен быть 401",  SC_UNAUTHORIZED, actualStatusCode);
        assertFalse ("Пароль не должен измениться", isUserPasswordChanged);
    }

    @Test
    @DisplayName("Проверка, что неавторизованный пользователь не может поменять Имя")
    @Description("Проверка: status code=401")
    public void notChangeName (){
        ValidatableResponse response = userClient.createUser(user);
        accessToken =response.extract().path("accessToken");
        String newName= User.getUserNameOnly().name;

        ValidatableResponse changeName = userClient.changeUserInfoNoToken(new User(user.email, user.password, newName));
        int actualStatusCode = changeName.extract().statusCode();
        boolean isUserNameChanged = changeName.extract().path("success");

        assertEquals("Статус-код должен быть 401",  SC_UNAUTHORIZED, actualStatusCode);
        assertFalse ("Имя не должно измениться", isUserNameChanged);
    }

}
