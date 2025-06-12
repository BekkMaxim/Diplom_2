package ru.praktikum.yandex;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import static org.apache.http.HttpStatus.*;


public class OrderClient extends BaseApi {
    private static final String API_INGREDIENTS = "api/ingredients";
    private static final String API_ORDERS = "api/orders";

    @Step("Получение ингредиентов")
    public Ingredients getIngredient() {
        return reqSpec
                .get(API_INGREDIENTS)
                .as(Ingredients.class);
    }

    @Step("Создание заказ с авторизацией")
    public  Response createOrderWithAuthorization(Order order, String token) {
        return reqSpec
                .filter(new AllureRestAssured())
                .header("Authorization", token)
                .body(order)
                .when()
                .post(API_ORDERS);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuthorization(Order order) {
        return reqSpec
                .filter(new AllureRestAssured())
                .body(order)
                .when()
                .post(API_ORDERS);
    }

    @Step("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients(Response response) {
        response.then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is("Ingredient ids must be provided"));
    }
}
