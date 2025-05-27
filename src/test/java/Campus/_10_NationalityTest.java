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

public class _10_NationalityTest extends CampusParent{

    String NationalityID="";


    @Test
    public void CreateNationality(){
        String nationName =randomUretici.nation().nationality();

        Map<String, String> newNationality=new HashMap<>();
        newNationality.put("name", nationName);

        NationalityID=
                given()
                        .spec(reqSpec)
                        .body(newNationality)

                        .when()
                        .post("school-service/api/nationality")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
                ;
        System.out.println("NationalityID = " + NationalityID);
    }

    @Test(dependsOnMethods = "CreateNationality")
    public void UpdateNatiionality(){

        Map<String, String> uptNationality =new HashMap<>();
        uptNationality.put("id",NationalityID);
        uptNationality.put("name", "Ayşenur"+ randomUretici.number().digits(5));

        given()
                .spec(reqSpec)
                .body(uptNationality)

                .when()
                .put("school-service/api/nationality")

                .then()
                .log().body()
                .statusCode(200)
                ;

    }

    @Test(dependsOnMethods = "UpdateNatiionality")
    public void DeleteNationality() {
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/nationality/" + NationalityID)

                .then()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "DeleteNationality")
    public void DeleteNationalityNegative() {
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/nationality/" + NationalityID)

                .then()
                .statusCode(400)
        ;
    }
}
