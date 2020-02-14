package com.astakhov.test;

import com.astakhov.dto.Account;
import com.astakhov.dto.CreateAccountRequest;
import com.astakhov.dto.Transfer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TransferControllerTest {

    @BeforeClass
    public static void beforeClass() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4567;
    }

    @Test
    public void shouldRespond400WhenEmptyRequestBody() {
        given()
                .when()
                .body("")
                .post("/transfers")
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("message", notNullValue());
    }

    @Test
    public void shouldRespond400WhenNegativeAmount() {
        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(-1));
        transfer.setDestinationAccountId("source");
        transfer.setDestinationAccountId("destination");

        given()
                .when()
                .body(transfer)
                .post("/transfers")
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("message", notNullValue());
    }

    @Test
    public void shouldRespond400WhenSourceAccountNotExists() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBalance(BigDecimal.valueOf(1));

        Account createdAccount = given().when().body(createAccountRequest)
                .post("/accounts")
                .andReturn().as(Account.class);

        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(123));
        transfer.setSourceAccountId("source");
        transfer.setDestinationAccountId(createdAccount.getId());

        given()
                .when()
                .body(transfer)
                .post("/transfers")
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("message", notNullValue());
    }

    @Test
    public void shouldRespond400WhenDestinationAccountNotExists() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBalance(BigDecimal.valueOf(1));

        Account createdAccount = given().when().body(createAccountRequest)
                .post("/accounts")
                .andReturn().as(Account.class);

        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(123));
        transfer.setSourceAccountId(createdAccount.getId());
        transfer.setDestinationAccountId("destination");

        given()
                .when()
                .body(transfer)
                .post("/transfers")
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("message", notNullValue());
    }

    @Test
    public void shouldRespond400WhenInsufficientBalanceOnSourceAccount() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBalance(BigDecimal.valueOf(0));

        Account source = given().when().body(createAccountRequest)
                .post("/accounts")
                .andReturn().as(Account.class);

        Account destination = given().when().body(createAccountRequest)
                .post("/accounts")
                .andReturn().as(Account.class);

        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(123));
        transfer.setSourceAccountId(source.getId());
        transfer.setDestinationAccountId(destination.getId());

        given()
                .when()
                .body(transfer)
                .post("/transfers")
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("message", notNullValue());
    }

    @Test
    public void shouldRespond201WhenTransferSuccessful() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBalance(BigDecimal.valueOf(10));

        Account source = given().when().body(createAccountRequest)
                .post("/accounts")
                .andReturn().as(Account.class);

        Account destination = given().when().body(createAccountRequest)
                .post("/accounts")
                .andReturn().as(Account.class);

        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(5));
        transfer.setSourceAccountId(source.getId());
        transfer.setDestinationAccountId(destination.getId());

        given()
                .when()
                .body(transfer)
                .post("/transfers")
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .contentType(ContentType.JSON);
    }
}

