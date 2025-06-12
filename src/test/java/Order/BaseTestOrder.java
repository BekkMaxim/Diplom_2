package Order;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import ru.praktikum.yandex.Order;
import ru.praktikum.yandex.OrderClient;
import ru.praktikum.yandex.User;
import ru.praktikum.yandex.UserClient;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public abstract class BaseTestOrder {

    protected String name;
    protected String email;
    protected String password;
    protected UserClient userClient;
    protected User user;
    protected String accessToken;
    protected OrderClient orderClient;
    protected List<String> ingredient;
    protected Order order;

    @Before
    public void setUp() {
        name = RandomStringUtils.randomAlphanumeric(5, 20);
        password = RandomStringUtils.randomAlphanumeric(8, 20);
        email = RandomStringUtils.randomAlphanumeric(4, 28).toLowerCase() + "@yandex.ru";

        userClient = new UserClient();
        orderClient = new OrderClient();
        user = new User(name, email, password);

        //Регистрация пользователя
        userClient.createNewUser(user)
            .then()
            .log().ifValidationFails()
            .statusCode(SC_OK);

        //Вход пользователя + получение токена авторизации
        accessToken = userClient.checkLoginExistingUser(user)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");

        if (accessToken == null) {
            throw new IllegalStateException("Не удалось получить токен авторизации");
        }

        ingredient = new ArrayList<>();
        order = new Order(ingredient);
    }

    @After
    @Step("Очистка после теста - удаление пользователя")
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
