package exercises;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class RestAssuredExercises3Test {

    private static RequestSpecification requestSpec;

    @BeforeAll
    public static void setUp() {

        createRequestSpecification();
        createResponseSpecification();
        getNinthDriverId();
    }

    static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("http://localhost").
                setPort(9876).
                setBasePath("/api/f1").
                build();
    }


    /*******************************************************
     * Create a static ResponseSpecification that checks whether:
     * - the response has statusCode 200
     * - the response contentType is JSON
     * - the value of MRData.CircuitTable.Circuits.circuitName[0]
     *   is equal to 'Albert Park Grand Prix Circuit'
     ******************************************************/

    private static ResponseSpecification responseSpec;


    static void createResponseSpecification() {
        responseSpec = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                expectBody("MRData.CircuitTable.Circuits.circuitName[0]", equalTo("Albert Park Grand Prix Circuit")).
                build();
    }

    /*******************************************************
     * Retrieve the list of 2016 Formula 1 drivers and store
     * the driverId for the ninth mentioned driver in a
     * static String variable
     * Use /2016/drivers.json
     ******************************************************/

    private static String ninthDriverId;


    static void getNinthDriverId() {
        ninthDriverId = given().
                when().
                spec(requestSpec).
                get("/2016/drivers.json").
                then().
                extract().
                path("MRData.DriverTable.Drivers[8].driverId");
    }

    /*******************************************************
     * Retrieve the circuit data for the first race in 2014
     * Use the previously created ResponseSpecification to
     * execute the specified checks
     * Use /2014/1/circuits.json
     * Additionally, check that the circuit is located in Melbourne
     ******************************************************/

    @Test
    public void useResponseSpecification() {

        given().
                when().
                spec(requestSpec).
                get("/2014/1/circuits.json").
                then().
                spec(responseSpec).
                and().
                body("MRData.CircuitTable.Circuits.Location[0].locality",equalTo("Melbourne"));
    }

    /*******************************************************
     * Retrieve the driver data for the ninth mentioned driver
     * Use the previously extracted driverId to do this
     * Use it as a path parameter to /drivers/<driverId>.json
     * Check that the driver is German
     ******************************************************/

    @Test
    public void useExtractedDriverId() {
        System.out.println(ninthDriverId);
        given().
                pathParam("driverId", ninthDriverId).
                spec(requestSpec).
                when().log().all().
                get("/drivers/{driverId}.json").
                then().
                body("MRData.DriverTable.Drivers[0].nationality",equalTo("German"));
    }
}