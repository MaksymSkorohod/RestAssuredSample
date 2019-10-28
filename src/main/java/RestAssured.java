import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class RestAssured {

    public static String baseURI;

    @Test
    public void restGet() {
        given()
                .baseUri("https://reqres.in/")
                .header("Content-Type", "application/json")
                .basePath("/api/users")
                .param("page", 2)
                .when()
                .get()
                .then()
                .statusCode(200)
                .and()
                .body("data.last_name", hasItem("Ferguson"));
    }
}
