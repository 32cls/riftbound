package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testDecksEndpoint() {
        given()
          .when().get("/decks")
          .then()
             .statusCode(200);
    }

}
