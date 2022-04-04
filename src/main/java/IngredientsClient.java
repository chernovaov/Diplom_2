import static io.restassured.RestAssured.given;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class IngredientsClient extends RestAssuredClient {
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Получить данные о всех ингредиентах")
    public ValidatableResponse getIngredients() {
         return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then();
    }
}

