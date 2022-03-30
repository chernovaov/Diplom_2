import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class UserOrdersInfoTest {
    private UserClient userClient;
    private static User user;
    private OrdersClient ordersClient;
    private IngredientsClient ingredientsClient;
    String accessToken;

    @Before
    public void startUp() {
        user = User.getRandomEmailAndPassAndName();
        userClient = new UserClient();
        ordersClient = new OrdersClient();
        ingredientsClient = new IngredientsClient ();
    }
    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Проверка, что можно получить заказы авторизованного пользователя")
    @Description("Проверка: status code=200")
    public void authUserOrdersInfoCanBeViewed (){
        userClient.createUser(user);
        ValidatableResponse login = userClient.loginUser(new User(user.email, user.password, user.name));
        accessToken = login.extract().path("accessToken");
        ValidatableResponse allIngredients = ingredientsClient.getIngredients();
        List<String> fiveIngredientsForBurger = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            fiveIngredientsForBurger.add(allIngredients.extract().path("data["+ i + "]._id"));
        }

        ordersClient.createOrder(accessToken, fiveIngredientsForBurger);

        ValidatableResponse response = ordersClient.getUserOrders(accessToken);

        int actualStatusCode = response.extract().statusCode();
        boolean isInfoGot = response.extract().path("success");
        List<Map<String, Object>> authUserOrders = response.extract().path("orders");

        assertEquals("Статус-код должен быть 200",  SC_OK, actualStatusCode);
        assertTrue ("Информация о заказах пользователя должна быть получена", isInfoGot);
        assertThat("Список заказов пользователя не должен быть пустым", authUserOrders, notNullValue());
    }

    @Test
    @DisplayName("Проверка, что нельзя получить заказы неавторизованного пользователя")
    @Description("Проверка: status code=401")
    public void nonAuthUserOrdersInfoCanNotBeViewed (){
        ValidatableResponse response = ordersClient.getUserOrdersNoToken();

        int actualStatusCode = response.extract().statusCode();
        boolean isInfoGot = response.extract().path("success");

        assertEquals("Статус-код должен быть 401", SC_UNAUTHORIZED, actualStatusCode);
        assertFalse ("Информация о заказах неавторизованного пользователя не должна быть получена", isInfoGot);

    }
}
