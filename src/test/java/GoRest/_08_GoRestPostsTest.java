package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class _08_GoRestPostsTest {
    RequestSpecification reqSpec;
    Faker randomUretici=new Faker();
    int PostID =0;

    @BeforeClass
    public void Setup(){ // başlabgıç işlemleri
        // token ve başlangıç set ayarları için spec oluşturuluyor
        reqSpec=new RequestSpecBuilder() // istek paketi setlenmesi
                .setContentType(ContentType.JSON) // giden body cinsi
                .addHeader("Authorization","Bearer 9e8db819231aa548c7ab62d7d520a79bed4180a0619ea0f6aba72f3fb9892942")
                .log(LogDetail.URI) //log.uri
                .build();
    }

    @Test
    public void CreateUser(){

        String rndTitle=randomUretici.name().title();
        String rndParagraf =randomUretici.lorem().paragraph();


        Map<String,String> newPost =new HashMap<>();
        newPost.put("user_id","7913849");
        newPost.put("title",rndTitle);
        newPost.put("body", rndParagraf);

        PostID =
                given()
                        .spec(reqSpec)
                        .body(newPost)

                        .when()
                        .post("https://gorest.co.in/public/v2/posts")

                        .then()
                        .statusCode(201)
                        .log().body()
                        .extract().path("id")

        ;

        System.out.println("PostID = " + PostID);

    }

    @Test(dependsOnMethods = "CreateUser")
    public void GetPostById(){

        given()
                .spec(reqSpec) //json ı seçtim, token i verdim

                .when()
                .get("https://gorest.co.in/public/v2/posts/"+ PostID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(PostID)) //data validation
        ;
    }

    @Test(dependsOnMethods = "GetPostById")
    public void UpdatePost(){

        Map<String,String> uptUser=new HashMap<>();
        uptUser.put("title","çiçek");

        given()
                .spec(reqSpec) //json ı seçtim, token i verdim
                .body(uptUser)

                .when()
                .put("https://gorest.co.in/public/v2/posts/"+ PostID)

                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test(dependsOnMethods = "UpdatePost")
    public void DeletePost()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("https://gorest.co.in/public/v2/posts/"+ PostID)

                .then()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "DeletePost")
    public void DeletePostNegative()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("https://gorest.co.in/public/v2/posts/"+ PostID)

                .then()
                .statusCode(404)
        ;
    }
}
