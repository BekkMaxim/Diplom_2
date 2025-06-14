package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class LoginUserTest extends BaseTestUser{

    @Before
    public void setUp() {
        super.setUp();
        userClientRequest.createNewUser(registeredUser);
    }

    @Test
    @DisplayName("Check Authorization")
    @Description("Авторизация существующего пользователя")
    public void checkAuthorization() {
        userClientRequest.checkLoginExistingUser(loginCredentials)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", Matchers.is(true),
                        "accessToken", Matchers.notNullValue(),
                        "refreshToken", Matchers.notNullValue(),
                        "user.email", Matchers.notNullValue(),
                        "user.name", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Check login with wrong password")
    @Description("Проверка логина с неверным паролем")
    public void checkLoginWithWrongPassword() {
        loginCredentials.setPassword("wrongPassword");
        Response response = userClientRequest.checkLoginExistingUser(loginCredentials);
        userClientResponse.checkFailAuthLogin(response);
    }

    @Test
    @DisplayName("Check Login With Wrong Email")
    @Description("Проверка логина с неверным email")
    public void checkLoginWithWrongEmail() {
        loginCredentials.setEmail("wrongEmail@gmail.com");
        Response response = userClientRequest.checkLoginExistingUser(loginCredentials);
        userClientResponse.checkFailAuthLogin(response);
    }

    @Test
    @DisplayName("Check Login Without Email")
    @Description("Проверка логина без email")
    public void checkLoginWithoutEmail() {
        loginCredentials.setEmail(null);
        Response response = userClientRequest.checkLoginExistingUser(loginCredentials);
        userClientResponse.checkFailAuthLogin(response);
    }

    @Test
    @DisplayName("Check Login Without Password")
    @Description("Проверка логина без пароля")
    public void checkLoginWithoutPassword() {
        loginCredentials.setPassword(null);
        Response response = userClientRequest.checkLoginExistingUser(loginCredentials);
        userClientResponse.checkFailAuthLogin(response);
    }

    @Test
    @DisplayName("Check Login Without Email and Password")
    @Description("Проверка логина без email и пароля")
    public void checkLoginWithoutEmailAndPassword() {
        loginCredentials.setEmail(null);
        loginCredentials.setPassword(null);
        Response response = userClientRequest.checkLoginExistingUser(loginCredentials);
        userClientResponse.checkFailAuthLogin(response);
    }
}
