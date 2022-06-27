package homeWork;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Credentials;
import models.LoginResponse;
import models.LoginResponseWithLombok;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static listeners.CustomAllureListener.withCustomTemplates;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static spec.Spec.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresInTests {

    @Test
    void loginTest() {
        Credentials data = Credentials.builder()
                .password("cityslicka")
                .email("eve.holt@reqres.in")
                .build();

        LoginResponse loginResponse =
                given()
                        .filter(withCustomTemplates())
                        .spec(request)
                        .contentType(JSON)
                        .body(data)
                        .when()
                        .post("/login")
                        .then()
                        .spec(responseSpec200)
                        .body(matchesJsonSchemaInClasspath("schemas/LoginResponse.json"))
                        .extract().as(LoginResponse.class);

        assertThat(loginResponse.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    void loginTestUseLombok() {
        Credentials data = Credentials.builder()
                .password("cityslicka")
                .email("eve.holt@reqres.in")
                .build();

        LoginResponseWithLombok loginResponse =
                given()
                        .filter(withCustomTemplates())
                        .spec(request)
                        .contentType(JSON)
                        .body(data)
                        .when()
                        .post("/login")
                        .then()
                        .spec(responseSpec200)
                        .body(matchesJsonSchemaInClasspath("schemas/LoginResponse.json"))
                        .extract().as(LoginResponseWithLombok.class);

        assertThat(loginResponse.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    void createUserTest() {

        Map<String, Object> jsonPayload = new HashMap<>();
        jsonPayload.put("name", "morpheus");
        jsonPayload.put("job", "leader");

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        given()
                .filter(withCustomTemplates())
                .spec(request)
                .body(gson.toJson(jsonPayload))
                .when()
                .post("/users")
                .then()
                .log().body()
                .spec(responseSpec201)
                .body("name", is("morpheus"));
    }

    @Test
    void updateUserTest() {

        Map<String, Object> jsonPayload = new HashMap<>();
        jsonPayload.put("name", "morpheus");
        jsonPayload.put("job", "zion resident");

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        given()
                .filter(withCustomTemplates())
                .spec(request)
                .contentType(JSON)
                .body(gson.toJson(jsonPayload))
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }

    @Test
    void deleteUserTest() {

        given()
                .filter(withCustomTemplates())
                .spec(request)
                .when()
                .delete("/users/2")
                .then()
                .spec(responseSpec204);
    }

    @Test
    void getUsersListTest() {

        given()
                .filter(withCustomTemplates())
                .spec(request)
                .when()
                .get("/users?page=2")
                .then()
                .log().body()
                .spec(responseSpec200)
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()", hasItem("michael.lawson@reqres.in"));
    }

    @Test
    void resourceNotFoundTest() {

        given()
                .filter(withCustomTemplates())
                .spec(request)
                .when()
                .get("/unknown/23")
                .then()
                .spec(responseSpec404)
                .body(is("{}"));
    }
}