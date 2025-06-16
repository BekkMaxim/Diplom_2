package ru.praktikum.yandex.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import ru.praktikum.yandex.api.common.BaseApi;

import static org.apache.http.HttpStatus.*;

public class UserClientResponse {

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
}
