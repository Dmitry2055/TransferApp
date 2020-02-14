package com.astakhov.test;

import com.astakhov.dto.Account;
import com.astakhov.dto.CreateAccountRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class AccountControllerTest {

    @BeforeClass
    public static void beforeClass() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4567;
    }

    @Test
    public void shouldRespond404WhenAccountNotFound() {
        given()
                .when()
                .pathParam("id", "nonexistent")
                .get("/accounts/{id}")
                .then()
                .assertThat()
                .statusCode(404)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("message", notNullValue());
    }

    @Test
    public void shouldRespond200AndAccountWhenAccountFound() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBalance(BigDecimal.valueOf(10.0));

        Account account = given().when().body(createAccountRequest)
                .post("/accounts")
                .andReturn().as(Account.class);

        given()
                .when()
                .pathParam("id", account.getId())
                .get("/accounts/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("id", equalTo(account.getId()))
                .and()
                .body("balance", equalTo(account.getBalance().floatValue()));
    }

    @Test
    public void shouldRespond400WhenCreatingAccountWithNegativeBalance() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBalance(BigDecimal.valueOf(-1));

        given()
                .when()
                .body(createAccountRequest)
                .post("/accounts")
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("message", notNullValue());
    }

    @Test
    public void shouldRespond201AndAccountWhenCreatingAccountWithPositiveBalance() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBalance(BigDecimal.valueOf(1.0));

        given()
                .when()
                .body(createAccountRequest)
                .post("/accounts")
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("id", notNullValue())
                .and()
                .body("balance", equalTo(createAccountRequest.getBalance().floatValue()));
    }

    @Test
    public void shouldRespond200AndBodyContainingAccount() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBalance(BigDecimal.valueOf(9876543.21));

        Account account = given().when().body(createAccountRequest)
                .post("/accounts")
                .andReturn().as(Account.class);

        given()
                .when()
                .get("/accounts")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .body("items.id", hasItem(account.getId()))
                .and()
                .body("items.balance", hasItem(account.getBalance().floatValue()));
    }
}
