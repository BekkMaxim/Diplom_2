package User;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import ru.praktikum.yandex.User;
import ru.praktikum.yandex.UserClient;

public abstract class BaseTestUser {

    protected String name;
    protected String email;
    protected String password;
    protected User user;
    protected UserClient userClient;

    @Before
    public void setUp() {
        name = RandomStringUtils.randomAlphanumeric(5, 20);
        password = RandomStringUtils.randomAlphanumeric(8, 20);
        email = RandomStringUtils.randomAlphanumeric(4, 28).toLowerCase() + "@yandex.ru";

        user = new User(name, email, password);
        User userLogin = new User(email, password);
        this.userClient = new UserClient();
    }

    @After
    @Step("Очистка после теста - удаление пользователя")
    public void tearDown() {
        String accessToken = userClient.checkLoginExistingUser(user).then().extract().path("accessToken");

        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
