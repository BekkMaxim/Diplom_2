package ru.praktikum.yandex;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import static org.apache.http.HttpStatus.*;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseApi {

    private static final String API_REGISTER_USER = "api/auth/register";
    private static final String API_LOGIN_USER = "api/auth/login";
    private static final String API_DELETE_USER = "api/auth/user";

    @Step("Создание пользователя")
    public Response createNewUser(User user) {
        return reqSpec
                .body(user)
                .when()
                .post(API_REGISTER_USER);
    }

    @Step("Ошибка при регистрации пользователя")
    public void failedResponseAuthRegistration(Response response) {
        response.then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Step("Логин существующего пользователя")
    public Response checkLoginExistingUser(User user) {
        return reqSpec
                .log().all()
                .body(user)
                .when()
                .post(API_LOGIN_USER);
    }

    @Step("Логин с неверным логином или паролем")
    public void checkFailAuthLogin(Response response) {
    response.then()
            .assertThat()
            .statusCode(SC_UNAUTHORIZED)
            .and()
            .body("success", Matchers.is(false))
            .and()
            .body("message", Matchers.is("email or password are incorrect"));
        }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(API_DELETE_USER);
    }

}
