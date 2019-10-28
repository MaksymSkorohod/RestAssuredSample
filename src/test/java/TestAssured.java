import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.lessThan;

public class TestAssured {

    RequestSpecification requestSpec =
            new RequestSpecBuilder()
                    .setBaseUri("https://reqres.in/")
                    .setContentType(ContentType.JSON)
                    .build();

    ResponseSpecification responseSpec200 =
            new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectResponseTime(lessThan(5000L))
            .expectBody("$", hasKey("data"))
            .build();

    ResponseSpecification responseSpec201 =
            new ResponseSpecBuilder()
                    .expectStatusCode(201)
                    .expectResponseTime(lessThan(5000L))
                    .build();

    ResponseSpecification responseSpec204 =
            new ResponseSpecBuilder()
                    .expectStatusCode(204)
                    .expectResponseTime(lessThan(5000L))
                    .build();
@Before


    @Test
    public void getListOfUsers(){
        given()
                .spec(requestSpec)
                .basePath("/api/users")
                .param("page",2)
                .when()
                .get()
                .then()
                .statusCode(200)
                .and()
                .body("data.first_name[0]", equalTo("Michael"))
                .and()
                .body("data.last_name[0]", equalTo("Lawson"));
    }

    @Test
    public void getSingleUser(){
        given()
                .spec(requestSpec)
                .basePath("/api/users/{id}")
                .pathParam("id", 2)
                .when()
                .get()
                .then()
                .statusCode(200)
                .and()
                .body("data.first_name", equalTo("Janet"))
                .and()
                .body("data.last_name", equalTo("Weaver"));
    }

    @Test
    public void getUserIsNotFound(){
        given()
                .spec(requestSpec)
                .basePath("/api/users/{id}")
                .pathParam("id", 23)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    public void getListResource(){
        given()
                .spec(requestSpec)
                .basePath("/api/unknown")
                .when()
                .get()
                .then()
                .statusCode(200)
                .and()
                .body("data.name", hasItem("aqua sky"));
    }

    @Test
    public void getSingleResource(){
        given()
                .spec(requestSpec)
                .basePath("/api/unknown/{id}")
                .pathParam("id", 2)
                .when()
                .get()
                .then()
                .statusCode(200)
                .and()
                .body("data.name", equalTo("fuchsia rose"));
    }

    @Test
    public void getSingleResourceNotFound(){
        given()
                .spec(requestSpec)
                .basePath("/api/unknown/{id}")
                .pathParam("id", 23)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    public void createUser(){
            String id = given()
                    .spec(requestSpec)
                    .basePath("/api/users")
                    .when()
                    .body("{\"name\": \"Max\", \"job\": \"Superman\"}")
                    .post()
                    .then()
                    .spec(responseSpec201)
                    .and()
                    .body("name", equalTo("Max"))
                    .and()
                    .body("job", equalTo("Superman"))
                    .and()
                    .body("$", hasKey("id"))
            .extract().body().path("id").toString();

            System.out.println(String.format("Current id is %s", id));
        }

    @Test
    public void UpdateUserPut(){

        String PutBody ="{\r\n" +
                "    \"name\": \"Max\",\r\n" +
                "    \"job\": \"Hero\"\r\n" +
                "}";
        RestAssured.baseURI="https://reqres.in";

        String Resp=given()
                .spec(requestSpec)
                .body(PutBody)
                .when()
                .put("/api/users/2")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("name",equalTo("Max"))
                .and()
                .body("job",equalTo("Hero"))
                .extract()
                .response()
                .asString();

        System.out.println("Response is\t" + Resp);
    }

    @Test
    public void UpdateAnotherUserPatch(){
        String PutBody ="{\r\n" +
                "    \"name\": \"Max\",\r\n" +
                "    \"job\": \"Hero\"\r\n" +
                "}";
        RestAssured.baseURI="https://reqres.in";

        String Resp=given()
                .spec(requestSpec)
                .body(PutBody)
                .when()
                .patch("/api/users/2")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("name",equalTo("Max"))
                .and()
                .body("job",equalTo("Hero"))
                .extract()
                .response()
                .asString();

        System.out.println("Response is\t" + Resp);
    }

    @Test
    public void DeleteUser(){
        given()
                .spec(requestSpec)
                .basePath("/api/users/2")
                .when()
                .delete()
                .then()
                .spec(responseSpec204);
    }


}
