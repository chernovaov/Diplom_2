import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
    public class UserCreateWithoutFieldTest {
<<<<<<< HEAD
=======

>>>>>>> 5e9511b6af64b44b3e723964e0703d40649d37f2
    private final User user;

        public UserCreateWithoutFieldTest(User user) {
            this.user = user;
        }

        @Parameterized.Parameters
        public static Object[][] getData() {
            return new Object[][] {
                    {User.getUserPasswordAndEmail()},
                    {User.getUserPasswordAndName()},
                    {User.getUserNameAndEmail()}
            };
        }

        @Test
        @DisplayName("Проверка валидации при регистрации пользователя")
        @Description("1. Нельзя зарегистрировать пользователя без поля Имя"
                + "\n"+ "2. Нельзя зарегистрировать пользователя без поля Почта"
                + "\n"+ "3. Нельзя зарегистрировать пользователя без поля Пароль"
        )

        public void userWithoutFieldIsNotAllowed() {
<<<<<<< HEAD

            final String expectedMessage = "Email, password and name are required fields";
            UserClient userClient = new UserClient();
            ValidatableResponse response = userClient.createUser(user);
            String accessToken = response.extract().path("accessToken");
            String actualMessage = response.extract().path("message");
            int actualStatus = response.extract().statusCode();
            boolean isUserReg = response.extract().path("success");

            if (isUserReg) {
                userClient.delete(accessToken);
            }

            assertEquals("Статус-код должен быть 403", SC_FORBIDDEN, actualStatus);
            assertFalse ("Пользователь не должен зарегистрироваться", isUserReg);
            assertEquals("В сообщении об ошибке другой текст",
                    expectedMessage, actualMessage);
=======
            final String expectedMessage = "Email, password and name are required fields";
            ValidatableResponse response = new UserClient().createUser(user);
            String actualMessage = response.extract().path("message");
            boolean isUserReg = response.extract().path("success");

            assertEquals("Статус-код должен быть 403", SC_FORBIDDEN, response.extract().statusCode());
            assertFalse ("Пользователь не должен зарегистрироваться", isUserReg);
            assertEquals("В сообщении об ошибке другой текст",
                    expectedMessage, actualMessage);

>>>>>>> 5e9511b6af64b44b3e723964e0703d40649d37f2
        }
}
