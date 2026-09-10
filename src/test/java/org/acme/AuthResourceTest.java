package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class AuthResourceTest {

    @Test
    void testRegisterRejectsPasswordWithoutUppercase() {
        assertRegister("noupper", "alllowercase1!", 400);
    }

    @Test
    void testRegisterRejectsPasswordWithoutLowercase() {
        assertRegister("nolower", "ALLUPPERCASE1!", 400);
    }

    @Test
    void testRegisterRejectsPasswordWithoutDigit() {
        assertRegister("nodigit", "NoDigitsHere!!", 400);
    }

    @Test
    void testRegisterRejectsPasswordWithoutSpecial() {
        assertRegister("nospecial", "NoSpecial12345", 400);
    }

    @Test
    void testRegisterRejectsShortPassword() {
        assertRegister("shortpw", "Ab1!", 400);
    }

    @Test
    void testRegisterAcceptsStrongPassword() {
        assertRegister("stronguser", "Str0ng!Pass", 201);
    }

    private void assertRegister(String username, String password, int expectedStatus) {
        given()
            .contentType("application/json")
            .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
            .when().post("/auth/register")
            .then()
                .statusCode(expectedStatus);
    }
}
