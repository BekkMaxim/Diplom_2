package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Test;
import ru.praktikum.yandex.model.Ingredient;
import ru.praktikum.yandex.model.Ingredients;

import static org.apache.http.HttpStatus.*;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderTest extends BaseTestOrder {

    @Test
    @DisplayName("Create Order With Authorization Test")
    @Description("Успешное создание заказа с авторизацией")
    public void createOrderWithAuthorizationTest() {
        List<String> ingredients = orderClient.getIngredient().getData().stream()
                .limit(7)
                .map(Ingredient::get_id)
                .collect(Collectors.toList());

        order.setIngredients(ingredients);

        orderClient.createOrderWithAuthorization(order, accessToken)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", Matchers.is(true),
                        "name", Matchers.notNullValue(),
                        "order.number", Matchers.any(Integer.class),
                        "order.ingredients", Matchers.notNullValue(),
                        "order._id", Matchers.notNullValue(),
                        "order.owner.name", Matchers.is(name),
                        "order.owner.email", Matchers.equalToIgnoringCase(email),
                        "order.status", Matchers.is("done"),
                        "order.price", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Create Order Without Authorization Test")
    @Description("Успешное создание заказа без авторизации")
    public void createOrderWithoutAuthorizationTest() {
        List<String> ingredients = orderClient.getIngredient().getData().stream()
                .limit(4)
                .map(Ingredient::get_id)
                .collect(Collectors.toList());

        order.setIngredients(ingredients);

        orderClient.createOrderWithoutAuthorization(order)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", Matchers.is(true),
                        "name", Matchers.notNullValue(),
                        "order.number", Matchers.any(Integer.class));
    }

    @Test
    @DisplayName("Create Empty Order Without Authorization")
    @Description("Создание заказа без ингредиентов и без авторизации")
    public void createEmptyOrderWithoutAuthorization() {
        Response response = orderClient.createOrderWithoutAuthorization(order);
        orderClient.verifyResponseForOrderWithoutIngredients(response);
    }

    @Test
    @DisplayName("Create Empty Order With Authorization")
    @Description("Создание заказа с авторизацией, без ингредиентов")
    public void createEmptyOrderWithAuthorization() {
        Response response = orderClient.createOrderWithAuthorization(order, accessToken);
        orderClient.verifyResponseForOrderWithoutIngredients(response);
    }

    @Test
    @DisplayName("Create Order Without Authorization With Wrong Hash")
    @Description("Создание заказа без авторизации и неверным хешем ингредиентов")
    public void createOrderWithoutAuthorizationWithWrongHash() {
        Ingredients ingredients = orderClient.getIngredient();
        String validIngredientId = ingredients.getData().get(0).get_id();

        order.setIngredients(List.of("invalid_ingredient_id", validIngredientId));

        orderClient.createOrderWithoutAuthorization(order)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Create Order With Authorization With Wrong Hash")
    @Description("Создание заказа с авторизацией и неверным хешем ингредиентов")
    public void createOrderWithAuthorizationWithWrongHash() {
        order.setIngredients(
                orderClient.getIngredient().getData().stream()
                        .limit(3)
                        .map(i -> i.get_id() + "invalid")
                        .collect(Collectors.toList())
        );

        orderClient.createOrderWithoutAuthorization(order)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

}