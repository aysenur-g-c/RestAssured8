package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class _09_CountryTest {
    Faker randomUreteci = new Faker();
    RequestSpecification reqSpec;
    String CountryID = "";


    @BeforeClass
    public void Setup() {
        baseURI="https://test.mersys.io"; //rest assured in kendi değişkeni
        // login ol , token al, spec i hazırla
        Map<String, String> credential = new HashMap<>();
        credential.put("username", "Campus25");
        credential.put("password", "Campus.2524");
        credential.put("rememberMe", "true");

        String token =
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)

                        .when()
                        .post("/auth/login") // eğer http ile başlamıyorsa baseUri başa eklenir

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().path("access_token");

        System.out.println("token = " + token);

        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }

    @Test
    public void CreateCountry() {
        String ulkeAdi = randomUreteci.address().country() + randomUreteci.number().digits(7);
        String ulkeKodu = randomUreteci.address().countryCode() + randomUreteci.number().digits(7);

        Map<String, String> newCountry = new HashMap<>();
        newCountry.put("name", ulkeAdi);
        newCountry.put("code", ulkeKodu);

        CountryID =
                given()
                        .spec(reqSpec)
                        .body(newCountry)

                        .when()
                        .post("/school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;

        System.out.println("CountryID = " + CountryID);
    }

    @Test(dependsOnMethods = "CreateCountry")
    public void UpdateCountry() {
        Map<String, String> uptCountry = new HashMap<>();
        uptCountry.put("id", CountryID);
        uptCountry.put("name", "Aysenur" + randomUreteci.number().digits(5));
        uptCountry.put("code", "ays" + randomUreteci.number().digits(5));


        given()
                .spec(reqSpec)
                .body(uptCountry)

                .when()
                .put("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "UpdateCountry")
    public void DeleteCountry() {
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/countries/" + CountryID)

                .then()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "DeleteCountry")
    public void DeleteCountryNegative() {
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/countries/" + CountryID)

                .then()
                .statusCode(400)
        ;
    }

    // TODO:


}
