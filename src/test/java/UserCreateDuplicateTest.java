import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.*;


public class UserCreateDuplicateTest {
    private UserClient userClient;
    private User user;
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
    @DisplayName("Проверка, что нельзя повторно создать такого же пользователя")
    @Description("Проверка: status code=403, message='User already exists'")
    public void userCanBeCreatedTest (){
        final String expectedMessage = "User already exists";
        ValidatableResponse firstUserResponse = userClient.createUser(user);
        accessToken = firstUserResponse.extract().path("accessToken");

        ValidatableResponse secondUserResponse = userClient.createUser(user);

        int actualStatusCode = secondUserResponse.extract().statusCode();
        String actualMessage = secondUserResponse.extract().path("message");
        boolean isUserCreated = secondUserResponse.extract().path("success");

        assertEquals ("Статус-код должен быть 403", SC_FORBIDDEN, actualStatusCode);
        assertFalse ("Дубль пользователя не должен создаться", isUserCreated);
        assertEquals("Неверный текст ошибки при повторном создании пользователя" ,
                expectedMessage,
                actualMessage);
    }
}

