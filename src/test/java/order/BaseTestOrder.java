package order;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import ru.praktikum.yandex.api.UserClientResponse;
import ru.praktikum.yandex.model.Order;
import ru.praktikum.yandex.api.OrderClient;
import ru.praktikum.yandex.model.User;
import ru.praktikum.yandex.api.UserClientRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public abstract class BaseTestOrder {

    protected Faker faker;
    protected String name;
    protected String email;
    protected String password;
    protected UserClientRequest userClientRequest;
    protected UserClientResponse userClientResponse;
    protected User user;
    protected String accessToken;
    protected OrderClient orderClient;
    protected List<String> ingredient;
    protected Order order;

    @Before
    public void setUp() {
        faker = new Faker(new Locale("ru"));

        name = faker.name().fullName();
        password = faker.internet().password(8, 20, true, true);
        email = faker.internet().emailAddress();

        userClientRequest = new UserClientRequest();
        userClientResponse = new UserClientResponse();
        orderClient = new OrderClient();
        user = new User(name, email, password);

        //Регистрация пользователя
        userClientRequest.createNewUser(user)
            .then()
            .log().ifValidationFails()
            .statusCode(SC_OK);

        //Вход пользователя + получение токена авторизации
        accessToken = userClientRequest.checkLoginExistingUser(user)
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
            userClientRequest.deleteUser(accessToken);
        }
    }
}
