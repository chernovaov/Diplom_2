import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class UserIncorrectLoginTest {

    private final User user;

    public UserIncorrectLoginTest(User user) {
        this.user = user;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                {User.getUserEmailOnly()},
                {User.getUserPasswordOnly()},
                {User.getUserPasswordAndEmail()}
        };
    }

    @Test
    @DisplayName("Проверка невозможности логина только по Почте, только по Паролю или с незарегистрированными Почтой и Паролем")
    @Description("1. Нельзя залогиниться с одним полем Почта, ожидается ошибка 401"
            + "\n"+ "2. Нельзя залогиниться с одним полем Пароль, ожидается ошибка 401"
            + "\n"+ "3. Нельзя залогиниться с незарегистрированными полями Почта и Пароль, ожидается ошибка 401")

    public void oneFieldOrUnregisteredDataIsNotAllowed() {
        final String expectedMessage = "email or password are incorrect";
        ValidatableResponse login = new UserClient().loginUser(user);
        int actualStatusCode = login.extract().statusCode();
        boolean isUserLogin = login.extract().path("success");
        String actualMessage = login.extract().path("message");

        assertEquals("Статус-код должен быть 401", SC_UNAUTHORIZED, actualStatusCode);
        assertFalse ("Пользователь не должен залогиниться", isUserLogin);
        assertEquals("В сообщении об ошибке другой текст", expectedMessage, actualMessage);
    }
}
