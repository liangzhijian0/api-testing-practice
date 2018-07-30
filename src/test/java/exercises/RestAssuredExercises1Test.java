package exercises;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


public class RestAssuredExercises1Test {

    private static RequestSpecification requestSpec;

    @BeforeAll
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("http://localhost").
                setPort(9876).
                setBasePath("/api/f1").
                build();
    }

    /*******************************************************
     * Send a GET request to /2016/drivers.json
     * and check that the response has HTTP status code 200
     ******************************************************/

    @Test
    public void check_response_code_for_correct_request() {

        given().
                spec(requestSpec).
                when().log().all().
                get("/2016/drivers.json").
                then().
                statusCode(200);
    }

    /*******************************************************
     * Send a GET request to /incorrect.json
     * and check that the answer has HTTP status code 404
     ******************************************************/

    @Test
    public void check_response_code_for_incorrect_request() {

        given().
                spec(requestSpec).
                when().
                get("/incorrect.json").
                then().
                statusCode(404);
    }

    /*******************************************************
     * Send a GET request to /2016/drivers.json
     * and check that the response is in JSON format
     ******************************************************/

    @Test
    public void check_response_content_type_is_json() {

        given().
                spec(requestSpec).
                when().
                get("/2014/1/circuits.json").
                then().
                contentType(ContentType.JSON);
    }

    /***********************************************
     * Retrieve circuit information for the first race
     * of the 2014 season and check the circuitId equals
     * albert_park
     * Use /2014/1/circuits.json
     **********************************************/

    @Test
    public void check_the_first_race_of_2014_was_at_AlbertPark() {

        given().
                spec(requestSpec).
                when().
                get("/2014/circuits.json").
                then().
                body("MRData.CircuitTable.Circuits[0].circuitId",equalTo("albert_park"));
    }

    /***********************************************
     * Retrieve the list of circuits for the 2014
     * season and check that it contains silverstone
     * Use /2014/circuits.json
     **********************************************/

    @Test
    public void check_there_was_a_race_at_Silverstone_in_2014() {

        given().
                spec(requestSpec).
                when().
                get("/2014/circuits.json").
                then().
                body("MRData.CircuitTable.Circuits.circuitId", hasItems("silverstone"));
    }

    /***********************************************
     * Retrieve the list of circuits for the 2014
     * season and check that it does not contain
     * nurburgring
     * USe /2014/circuits.json
     **********************************************/

    @Test
    public void check_the_race_was_not_at_Nurburgring_in_2014() {

        given().
                spec(requestSpec).
                when().

                then();
    }
}