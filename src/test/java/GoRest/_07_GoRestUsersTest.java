package GoRest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import Model.User;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class _07_GoRestUsersTest {
    RequestSpecification reqSpec;
    Faker randomUretici=new Faker();
    int userID=0;

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
        //body e ihtiyacım var
        // 1.Yöntem (Giden body hazırlama)
//        String bodyUser="{" +
//                "  \"name\":\"{{$randomFullName}}\"," +
//                "  \"gender\":\"male\"," +
//                "  \"email\":\"{{$randomEmail}}\"," +
//                "  \"status\":\"active\"" +
//                "}";

        // 2.Yöntem (Giden body hazırlama)
//        User newUser2=new User();
//        newUser2.setName("Ayşenur Can");
//        newUser2.setGender("male");
//        newUser2.setEmail("aysenurCan@gmail.com");
//        newUser2.setStatus("active");



        // 3.Yöntem (Giden body hazırlama)

        String rndFullName=randomUretici.name().fullName();
        String rndEmail=randomUretici.internet().emailAddress();

        Map<String,String> newUser=new HashMap<>();
        newUser.put("name",rndFullName);
        newUser.put("gender", "male");
        newUser.put("email",rndEmail);
        newUser.put("status","active");

        userID=  //collection varible daki user ıd set ettim
        given()
                .spec(reqSpec) //json ı seçtim, token i verdim
                .body(newUser) // body i verdim

                .when()
                .post("https://gorest.co.in/public/v2/users")

                .then()
                .statusCode(201)
                .log().body()
                .extract().path("id")

                ;

        System.out.println("userID = " + userID);

    }

    @Test(dependsOnMethods = "CreateUser")
    public void GetUserById(){

        given()
                        .spec(reqSpec) //json ı seçtim, token i verdim

                        .when()
                        .get("https://gorest.co.in/public/v2/users/"+userID)

                        .then()
                        .statusCode(200)
                        .body("id", equalTo(userID)) //data validation
        ;
    }

    @Test(dependsOnMethods = "GetUserById")
    public void UpdateUser(){

        Map<String,String> uptUser=new HashMap<>();
        uptUser.put("name","Aysenur Can");

        given()
                .spec(reqSpec) //json ı seçtim, token i verdim
                .body(uptUser)

                .when()
                .put("https://gorest.co.in/public/v2/users/"+userID)

                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test(dependsOnMethods = "UpdateUser")
    public void DeleteUser()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("https://gorest.co.in/public/v2/users/"+userID)

                .then()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "DeleteUser")
    public void DeleteUserNegative()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("https://gorest.co.in/public/v2/users/"+userID)

                .then()
                .statusCode(404)
        ;
    }

}
