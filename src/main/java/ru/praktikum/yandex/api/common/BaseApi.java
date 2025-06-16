package ru.praktikum.yandex.api.common;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApi {
    protected static final  String BASE_URI = "https://stellarburgers.nomoreparties.site/";
    protected RequestSpecification reqSpec;

    public BaseApi() {
        RestAssured.baseURI = BASE_URI;
        this.reqSpec = RestAssured.given()
                .contentType(ContentType.JSON);
    }
}
