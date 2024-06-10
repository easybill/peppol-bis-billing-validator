package io.github.easybill;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
class IndexControllerTest {

    @Test
    void testValidationEndpointWhenInvokedWithWrongMethod() {
        given().when().get("/validation").then().statusCode(405);
    }

    @Test
    void testValidationEndpointWhenInvokedWithAnEmptyPayload() {
        given().when().post("/validation").then().statusCode(415);
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "Allowance-example.xml",
            "base-creditnote-correction.xml",
            "base-example.xml",
            "base-negative-inv-correction.xml",
            "sales-order-example.xml",
            "vat-category-E.xml",
            "vat-category-O.xml",
            "Vat-category-S.xml",
            "vat-category-Z.xml",
        }
    )
    void testValidationEndpointWhenInvokedWithValidPeppolXmlPayloads(
        @NonNull String fixtureFileName
    ) throws IOException {
        URL fileContent = Objects.requireNonNull(
            Thread
                .currentThread()
                .getContextClassLoader()
                .getResource(fixtureFileName)
        );

        String content = Files.readString(
            Path.of(fileContent.getFile()),
            StandardCharsets.UTF_8
        );

        given()
            .body(content)
            .contentType(ContentType.XML)
            .when()
            .post("/validation")
            .then()
            .statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(strings = { "base-example-wrong.xml" })
    void testValidationEndpointWhenInvokedWithInvalidPeppolXmlPayloads(
        @NonNull String fixtureFileName
    ) throws IOException {
        URL fileContent = Objects.requireNonNull(
            Thread
                .currentThread()
                .getContextClassLoader()
                .getResource(fixtureFileName)
        );

        String content = Files.readString(
            Path.of(fileContent.getFile()),
            StandardCharsets.UTF_8
        );

        given()
            .body(content)
            .contentType(ContentType.XML)
            .when()
            .post("/validation")
            .then()
            .statusCode(400);
    }
}
