import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class _01_ApiTest {

    @Test
    public void Test1(){

        // 1- Endpoint i çağırmadna önce hazırlıkların yapıldığı bölüm : Request, gidecek body, token
        // 2- Endpoint in çağrıldığı bölüm  : Endpoint in çağrılması(METOD: GET,POST ..)
        // 3- Endpoint çağrıldıktan sonraki bölüm : Response, Test(Assert), data

        given().
                //1.bölümle ilgili işler: giden, body, token
                when().
                //2.bölümle ilgili işler: gidiş metodu, endpoint, apinin çağırılma kısmı
                then()
                //3. bölümle ilgili işler: gelen data, assert, test
                ;

    }

    @Test
    public void Test2(){

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() //dönüş datasını gösteriyor  all: tüm bilgiler
                .statusCode(200) // dönen değer 200 e eşit mi, assert

                ;
    }

    @Test
    public void contentTypeTest(){

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()  // dönen body yi yaz
                .statusCode(200)  // dönen status code 200 mü assert
                .contentType(ContentType.JSON)  // dönen içerik formatı JSON mı assert

                ;
    }

    @Test
    public void checkCountryInResponseBody(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // dönüş datalarını yaz // all: tüm dönüş bilgilerini yaz
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("country",equalTo("United States")) // assert
                // country yi dışarı almadan
                // bulunduğu yeri (path i) vererek içerde assertion yapıyorum
                //bunu hamcrest kütüphanesi yapıyor

                ;
    }

    @Test
    public void checkHasItem(){
        // Soru : "http://api.zippopotam.us/tr/01000"  endpoint in dönen
        // place dizisinin herhangi bir elemanında  "Dörtağaç Köyü" değerinin
        // olduğunu doğrulayınız

        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'place name'",hasItem("Dörtağaç Köyü"))
        // places içinde ki bütün place name ler in
        //içinde dörtağaç köyü var mı assert
                ;


    }


    @Test
    public void bodyArrayHasSizeTest() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
        // place dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .body("places",hasSize(1))
                ;
    }

    @Test
    public void combiningTest(){
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON) //assert
                .body("places", hasSize(1)) // assert
                .body("places.'place name'", hasItem("Beverly Hills")) // assert
                .body("country", equalTo("United States")) // assert
        ;
    }

    @Test
    public void pathParamTest(){
        given()
                .pathParam("ulke","us")
                .pathParam("pk",90210)
                .log().uri() // oluşacak endpointi yazıyorum

                .when()
                .get("http://api.zippopotam.us/{ulke}/{pk}")

                .then()
                .log().body()
        ;
    }

    @Test
    public void queryParamTest(){
//https://gorest.co.in/public/v1/users?page=3

        given()
                .param("page",3)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
        ;
    }


    // https://gorest.co.in/public/v1/users?page=3
    // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki
    // donen page degerlerinin çağrılan page nosu ile aynı
    // olup olmadığını kontrol ediniz.
    @Test
    public void Soru(){
        for (int i = 1; i <= 10; i++) {
            given()
                    .param("page", i)

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .body("meta.pagination.page", equalTo(i))
            ;

        }
    }
}
