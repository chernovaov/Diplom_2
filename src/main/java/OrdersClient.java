import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class OrdersClient extends RestAssuredClient {
    private static final String ORDER_PATH = "/api/orders";
    private static final Map<String, List<String>> body = new HashMap<>();


    @Step ("Создать заказ под авторизованным пользователем")
    public ValidatableResponse createOrder (String accessToken, List<String> ingredients){
        body.put("ingredients", ingredients);
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .body(body).log().all()
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    @Step ("Попытаться создать заказ под неавторизованным пользователем")
    public ValidatableResponse createOrderNonAuth (List<String> ingredients){
        body.put("ingredients", ingredients);
        return given()
                .spec(getBaseSpec())
                .body(body).log().all()
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    @Step ("Попытаться создать заказ под авторизованным пользователем без ингредиентов")
    public ValidatableResponse createOrderWithoutIngredients (String accessToken){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .log().all()
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }


    @Step ("Получить заказы авторизованного пользователя")
    public ValidatableResponse getUserOrders (String accessToken){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken.replace("Bearer ", "")).log().all()
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }

    @Step ("Попытаться получить заказы неавторизованного пользователя")
    public ValidatableResponse getUserOrdersNoToken (){
        return given()
                .spec(getBaseSpec()).log().all()
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }
}