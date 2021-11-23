import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresinRestTests {

    private final String baseURI = "https://reqres.in/",
            basePath = "api/";

    @Test
    void checkSingleUserLastName() {
        Response response = given()
                .baseUri(baseURI)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .when()
                .get("users/2")
                .then()
                .statusCode(200)
                .extract().response();
        assertEquals(response.path("data.last_name"), "Weaver");
    }

    @Test
    void checkSingleUserNotFound() {
        Response response = given()
                .baseUri(baseURI)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .when()
                .get("users/23")
                .then()
                .extract().response();
        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    void checkDeleteRequest() {
        given()
                .baseUri(baseURI)
                .basePath(basePath)
                .when()
                .delete("users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void checkSuccessfulLogin() {
        String body = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"cityslicka\"\n" +
                "}";
        given()
                .baseUri(baseURI)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("login")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void checkCreateUser() {
        String body = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";
        given()
                .baseUri(baseURI)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("users")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }
}
