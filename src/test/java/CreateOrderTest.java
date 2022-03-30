import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class CreateOrderTest {
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
    @DisplayName("Проверка, что авторизованный пользователь может создать заказ")
    @Description("Проверка: status code=200")
    public void authUserCanCreateOrder (){

        userClient.createUser(user);
        ValidatableResponse login = userClient.loginUser(new User(user.email, user.password, user.name));
        accessToken = login.extract().path("accessToken");
        ValidatableResponse allIngredients = ingredientsClient.getIngredients();
        List<String> fiveIngredientsForBurger = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            fiveIngredientsForBurger.add(allIngredients.extract().path("data["+ i + "]._id"));
        }

        ValidatableResponse response = ordersClient.createOrder(accessToken, fiveIngredientsForBurger);

        int actualStatusCode = response.extract().statusCode();
        boolean isOrderCreated = response.extract().path("success");
        int orderNum = response.extract().path("order.number");

        assertEquals("Статус-код должен быть 200",  SC_OK, actualStatusCode);
        assertTrue ("Заказ должен создаться", isOrderCreated);
        assertThat("Номер заказа не должен быть пустым", orderNum, notNullValue());

    }

    @Test
    @DisplayName("Проверка, что неавторизованный пользователь не может создать заказ")
    @Description("Проверка: status code=401")
    public void nonAuthUserCanNotCreateOrder (){

        ValidatableResponse allIngredients = ingredientsClient.getIngredients();
        List<String> fiveIngredientsForBurger = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            fiveIngredientsForBurger.add(allIngredients.extract().path("data["+ i + "]._id"));
        }

        ValidatableResponse response = ordersClient.createOrderNonAuth(fiveIngredientsForBurger);
        int actualStatusCode = response.extract().statusCode();
        boolean isOrderCreated = response.extract().path("success");

        assertEquals("Статус-код должен быть 401",  SC_UNAUTHORIZED, actualStatusCode);
        assertFalse ("Заказ не должен создаться", isOrderCreated);
    }

    @Test
    @DisplayName("Проверка, что авторизованный пользователь не может создать заказ без ингредиентов")
    @Description("Проверка: status code=400")
    public void authUserCanNotCreateOrderWithoutIngredients (){

        userClient.createUser(user);
        ValidatableResponse login = userClient.loginUser(new User(user.email, user.password, user.name));
        accessToken = login.extract().path("accessToken");

        ValidatableResponse response = ordersClient.createOrderWithoutIngredients(accessToken);
        int actualStatusCode = response.extract().statusCode();
        boolean isOrderCreated = response.extract().path("success");

        assertEquals("Статус-код должен быть 400",  SC_BAD_REQUEST, actualStatusCode);
        assertFalse ("Заказ не должен создаться", isOrderCreated);
    }

    @Test
    @DisplayName("Проверка, что авторизованный пользователь не может создать заказ с некорректными хэш ингредиентов")
    @Description("Проверка: status code=500")
    public void authUserCanNotCreateOrderWithIncorrectHash (){

        userClient.createUser(user);
        ValidatableResponse login = userClient.loginUser(new User(user.email, user.password, user.name));
        accessToken = login.extract().path("accessToken");
        ValidatableResponse allIngredients = ingredientsClient.getIngredients();
        List<String> fiveIngredientsForBurger = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            fiveIngredientsForBurger.add(allIngredients.extract().path("data["+ i + "]._id")+"aa");
        }

        ValidatableResponse response = ordersClient.createOrder(accessToken, fiveIngredientsForBurger);

        int actualStatusCode = response.extract().statusCode();
        assertEquals("Статус-код должен быть 500",  SC_INTERNAL_SERVER_ERROR, actualStatusCode);

    }
}
