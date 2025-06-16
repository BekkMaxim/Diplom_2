package user;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import ru.praktikum.yandex.api.UserClientResponse;
import ru.praktikum.yandex.model.User;
import ru.praktikum.yandex.api.UserClientRequest;

import java.util.Locale;

public abstract class BaseTestUser {

    protected String name;
    protected String email;
    protected String password;
    protected Faker faker;
    protected User registeredUser;
    protected User loginCredentials;
    protected UserClientRequest userClientRequest;
    protected UserClientResponse userClientResponse;

    @Before
    public void setUp() {
        faker = new Faker(new Locale("ru"));

        name = faker.name().fullName();
        password = faker.internet().password(8, 20, true, true);
        email = faker.internet().emailAddress();

        registeredUser = new User(name, email, password);
        loginCredentials = new User(email, password);
        this.userClientRequest = new UserClientRequest();
        this.userClientResponse = new UserClientResponse();
    }

    @After
    @Step("Очистка после теста - удаление пользователя")
    public void tearDown() {
        String accessToken = userClientRequest.checkLoginExistingUser(loginCredentials).then().extract().path("accessToken");

        if (accessToken != null) {
            userClientRequest.deleteUser(accessToken);
        }
    }
}
