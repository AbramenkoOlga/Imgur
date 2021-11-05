package com.geekbrains.rest.Imgur;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImgurApiTest {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = ImgurApiParams.API_URL + "/" + ImgurApiParams.API_VERSION;
    }

    @DisplayName("Тест на получение базовой информации об аккаунте")
    @Test
    @Order(1)
    void testAccountBase() {
        String url = "account/" + "abramenkoolga";
        given().when()
                .auth()
                .oauth2(ImgurApiParams.TOKEN)
                .log()
                .all()
                .expect()
                .statusCode(is(200))
                .body("success", is(true))
                .body("status", is(200))
                .body("data.bio", is(null))
                .body("data.reputation", is(8))
                .log()
                .all()
                .when()
                .get(url);
    }

    @DisplayName("Тест получение информации о картинке")
    @Test
    @Order(2)
    void testImage() {
        String imageHash = "zHF1a9f";
        String url = "image/" + imageHash;
        given().when()
                .auth()
                .oauth2(ImgurApiParams.TOKEN)
                .log()
                .all()
                .expect()
                .statusCode(is(200))
                .body("success", is(true))
                .body("status", is(200))
                .log()
                .all()
                .when()
                .get(url);
    }

    @DisplayName("Тест загрузка изображения")
    @Test
    @Order(3)
    void testImageUpload() {
        given().when()
                .auth()
                .oauth2(ImgurApiParams.TOKEN)
                .log()
                .all()
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/upload")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @DisplayName("Добавление в избранные")
    @Test
    @Order(4)
    void testFavoriteAnImage() {
        String imageHash = "zHF1a9f";
        String url = "image/" + imageHash + "/favorite";
        given().when()
                .auth()
                .oauth2(ImgurApiParams.TOKEN)
                .log()
                .all()
                .expect()
                .statusCode(is(200))
                .body("success", is(true))
                .body("status", is(200))
                .log()
                .all()
                .when()
                .get(url);
    }

    @DisplayName("Тест обновления информации о картинке")
    @Test
    @Order(5)
    void testUpdateImageInfo() {
        String imageHash = "zHF1a9f";
        String url = "image/" + imageHash;
        given().when()
                .auth()
                .oauth2(ImgurApiParams.TOKEN)
                .log()
                .all()
                .formParam("title", "Test1")
                .formParam("description", "TestTest1")
                .expect()
                .log()
                .all()
                .statusCode(is(200))
                .body("success", is(true))
                .body("data", is(true))
                .when()
                .post(url);
    }

    @DisplayName("Тест удаление картинки")
    @Test
    @Order(6)
    void testImageDeletion() {
            String imageHash = "QbaURAS";
            String url = "image/" + imageHash;
            given().when()
                    .auth()
                    .oauth2(ImgurApiParams.TOKEN)
                    .log()
                    .all()
                    .expect()
                    .statusCode(is(200))
                    .body("success", is(true))
                    .body("status", is(200))
                    .log()
                    .all()
                    .when()
                    .delete(url);
    }
}
