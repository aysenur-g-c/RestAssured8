import Model.ToDo;
import Model.ZipCode;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Task 1
 * create a request to https://jsonplaceholder.typicode.com/todos/2
 * expect status 200
 * expect content type JSON
 * expect title in response body to be "quis ut nam facilis et officia qui"
 */
public class _05_Task {
    @Test
    public void soru(){

      //  ZipCode zipCodeNesnesi=
        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                //.log().body();
                .body("title", equalTo("quis ut nam facilis et officia qui"))
                .log().body();

    }

    /**
     * Task 2
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * a) expect response completed status to be false(hamcrest)
     * b) extract completed field and testNG assertion(testNG)
     */
    @Test
    public void soru2(){

        //a
        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false))
        ;

        //b
        boolean completedData=
                given()

                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().path("completed");

        Assert.assertFalse(completedData);
    }

    /** Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * Converting Into POJO body data and write
     */
    @Test
    public void soru3(){

        ToDo toDoNesnesi=
        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .extract().body().as(ToDo.class);

        System.out.println("toDoNesnesi = " + toDoNesnesi);

        System.out.println("toDoNesnesi = " + toDoNesnesi);

    }

}
