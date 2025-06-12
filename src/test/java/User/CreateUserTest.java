package User;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Test;
import ru.praktikum.yandex.User;

import static org.apache.http.HttpStatus.*;

public class CreateUserTest extends BaseTestUser{

    @Test
    @DisplayName("Create New User Test")
    @Description("Создание нового пользователя")
    public void createNewUserTest() {
        userClient.createNewUser(user)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", Matchers.is(true),
                "accessToken", Matchers.notNullValue(),
                "refreshToken", Matchers.notNullValue(),
                        "user.email", Matchers.equalTo(email),
                        "user.name", Matchers.equalTo(name));
    }

    @Test
    @DisplayName("Create Duplicate User Test")
    @Description("Создание не уникального пользователя")
    public void createDuplicateUserTest() {
        userClient.createNewUser(user)
                .then()
                .log().ifValidationFails()
                .statusCode(SC_OK)
                .body("success", Matchers.is(true));

        userClient.createNewUser(user)
                .then()
                .log().ifValidationFails()
                .statusCode(SC_FORBIDDEN)
                .body("success", Matchers.is(false),
                        "message", Matchers.equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create User Without Name Test")
    @Description("Создание пользователя без имени")
    public void createUserWithoutNameTest() {
        Response response = userClient.createNewUser(new User(null, email, password));
        userClient.failedResponseAuthRegistration(response);
    }

    @Test
    @DisplayName("Create User Without Email Test")
    @Description("Создание пользователя без email")
    public void createUserWithoutEmailTest() {
        Response response = userClient.createNewUser(new User(name, null, password));
        userClient.failedResponseAuthRegistration(response);
    }

    @Test
    @DisplayName("Create User Without Password Test")
    @Description("Создание пользователя без пароля")
    public void createUserWithoutPasswordTest() {
        Response response = userClient.createNewUser(new User(name, email, null));
        userClient.failedResponseAuthRegistration(response);
    }

    @Test
    @DisplayName("Create User Without All Fields Test")
    @Description("Создание пользователя без всех полей")
    public void createUserWithoutAllFieldsTest() {
        Response response = userClient.createNewUser(new User(null, null, null));
        userClient.failedResponseAuthRegistration(response);
    }

    @Test
    @DisplayName("Create User Without Name And Email Test")
    @Description("Создание пользователя без имени и email")
    public void createUserWithoutNameAndEmailTest() {
        Response response = userClient.createNewUser(new User(null, null, password));
        userClient.failedResponseAuthRegistration(response);
    }

    @Test
    @DisplayName("Create User Without Name And Password Test")
    @Description("Создание пользователя без имени и пароля")
    public void createUserWithoutNameAndPasswordTest() {
        Response response = userClient.createNewUser(new User(null, email, null));
        userClient.failedResponseAuthRegistration(response);
    }

    @Test
    @DisplayName("Create User Without Email And Password Test")
    @Description("Создание пользователя без email и пароля")
    public void createUserWithoutEmailAndPasswordTest() {
        Response response = userClient.createNewUser(new User(name, null, null));
        userClient.failedResponseAuthRegistration(response);
    }
}
