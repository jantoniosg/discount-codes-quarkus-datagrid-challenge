package com.redhat.challenge.discount;

import com.redhat.challenge.discount.model.DiscountCode;
import com.redhat.challenge.discount.model.DiscountCodeType;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

// NOTE: When you connect the REST API to RHDG or Infinispan, you will need an Infinispan or DG running in test
// containers The @QuarkusTestResource with the CacheResource provided, will do this for you.
@QuarkusTestResource(CacheResource.class)
@QuarkusTest
class DiscountCodesResourceTest {

  @Test
  void testCreateConsumeAndListCodes() {

    given()
            .body("{\"name\": \"PROMO42\", \"amount\": 20, \"enterprise\": \"ALBACETEBANK\", \"type\": " +
                    "\"VALUE\"}")
            .contentType(ContentType.JSON)
            .when()
            .post("/discounts/{lifespan}", 3600)
            .then()
            .statusCode(201);


    DiscountCode discountCode = given()
            .when()
            .contentType(ContentType.JSON)
            .get("/discounts/consume/{name}", "PROMO42")
            .then()
            .statusCode(200)
            .extract()
            .body().as(DiscountCode.class);

    assertEquals("PROMO42", discountCode.getName());
    assertEquals("ALBACETEBANK", discountCode.getEnterprise());
    assertEquals(20, discountCode.getAmount());
    assertEquals(DiscountCodeType.VALUE, discountCode.getType());
    assertEquals(1, discountCode.getUsed());

    DiscountCodes discountCodes = given()
            .when()
            .contentType(ContentType.JSON)
            .get("/discounts/{type}", DiscountCodeType.VALUE)
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .body().as(DiscountCodes.class);

    assertEquals(1, discountCodes.getTotalCount());
    assertEquals(1, discountCodes.getDiscountCodesList().size());
    assertEquals("PROMO42", discountCodes.getDiscountCodesList().get(0).getName());
  }

}
