package User;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Test;
import ru.praktikum.yandex.User;

import static org.apache.http.HttpStatus.*;

public class LoginUserTest extends BaseTestUser{

    @Test
    @DisplayName("Check Authorization")
    @Description("Авторизация существующего пользователя")
    public void checkAuthorization() {
        userClient.createNewUser(user);
        userClient.checkLoginExistingUser(new User(email, password))
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
        userClient.createNewUser(user);
        user.setPassword("wrongPassword");
        Response response = userClient.checkLoginExistingUser(user);
        userClient.checkFailAuthLogin(response);
    }

    @Test
    @DisplayName("Check Login With Wrong Email")
    @Description("Проверка логина с неверным email")
    public void checkLoginWithWrongEmail() {
        userClient.createNewUser(user);
        user.setEmail("wrongEmail@gmail.com");
        Response response = userClient.checkLoginExistingUser(user);
        userClient.checkFailAuthLogin(response);
    }

    @Test
    @DisplayName("Check Login Without Email")
    @Description("Проверка логина без email")
    public void checkLoginWithoutEmail() {
        userClient.createNewUser(user);
        user.setEmail(null);
        user.setName(null);
        Response response = userClient.checkLoginExistingUser(user);
        userClient.checkFailAuthLogin(response);
    }

    @Test
    @DisplayName("Check Login Without Password")
    @Description("Проверка логина без пароля")
    public void checkLoginWithoutPassword() {
        userClient.createNewUser(user);
        user.setPassword(null);
        user.setName(null);
        Response response = userClient.checkLoginExistingUser(user);
        userClient.checkFailAuthLogin(response);
    }

    @Test
    @DisplayName("Check Login Without Email and Password")
    @Description("Проверка логина без email и пароля")
    public void checkLoginWithoutEmailAndPassword() {
        userClient.createNewUser(user);
        user.setEmail(null);
        user.setPassword(null);
        user.setName(null);
        Response response = userClient.checkLoginExistingUser(user);
        userClient.checkFailAuthLogin(response);
    }
}
