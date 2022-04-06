import static io.restassured.RestAssured.given;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class IngredientsClient extends RestAssuredClient {
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Получить данные о всех ингредиентах")
    public ValidatableResponse getIngredients() {
         return given()
<<<<<<< HEAD
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then();
=======
                .spec(getBaseSpec()).log().all()
                .when()
                .get(INGREDIENTS_PATH)
                .then().log().all();
>>>>>>> 5e9511b6af64b44b3e723964e0703d40649d37f2
    }
}

