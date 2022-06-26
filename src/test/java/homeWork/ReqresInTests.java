package homeWork;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static listeners.CustomAllureListener.withCustomTemplates;
import static spec.Spec.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresInTests {

    @Test
    void loginTest() {

        String authorizeData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        String contentType = "application/json";

        given()
                .filter(withCustomTemplates())
                .spec(request)
                .body(authorizeData)
                .contentType(contentType)
                .post("/login")
                .then()
                .spec(responseSpec200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
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
                .contentType(JSON)
                .body(gson.toJson(jsonPayload))
                .when()
                .post("/users")
                .then()
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