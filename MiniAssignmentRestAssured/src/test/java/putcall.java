import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class putcall {
    @Test
    public void test_put_call(){
        File JsonData=new File("C:\\Users\\udayprsingh\\IdeaProjects\\MiniAssignmentRestAssured\\src\\test\\resources\\Putdata.json");
        given().
                baseUri("https://reqres.in/api/users").
                body(JsonData).
                header("Content-Type","application/json").
                when().
                put("/users").
                then().
                statusCode(200).body("name",equalTo("Arun")).body("job",equalTo("Manager"));
    }
}
