package api;

import models.CreateTestCaseBody;
import models.scenario.CreateTestScenarioBody;

import static api.AuthorizationApi.ALLURE_TESTOPS_SESSION;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.is;

public class TestCaseApi {

    public int createAndGetTestCaseId(String xsrfToken, String authorizationCookie, CreateTestCaseBody testCaseBody, String projectId, String testCaseName) {
        int testCaseId = given()
                .log().all()
                .header("X-XSRF-TOKEN", xsrfToken)
                .cookies("XSRF-TOKEN", xsrfToken,
                        ALLURE_TESTOPS_SESSION, authorizationCookie)
                .body(testCaseBody)
                .contentType(JSON)
                .post("/api/rs/testcasetree/leaf?projectId=" + projectId)
                .then()
                .log().body()
                .statusCode(200)
                .body("name", is(testCaseName))
                .body("automated", is(false))
                .body("external", is(false))
                .extract()
                .path("id");

        return testCaseId;
    }

    public void createAndGetTestCaseScenarioId(String xsrfToken, String authorizationCookie, CreateTestScenarioBody testScenarioBody, int testcaseId, String testScenario) {
        given()
                .log().all()
                .header("X-XSRF-TOKEN", xsrfToken)
                .cookies("XSRF-TOKEN", xsrfToken,
                        ALLURE_TESTOPS_SESSION, authorizationCookie)
                .body(testScenarioBody)
                .contentType(JSON)
                .post("/api/rs/testcase/" + testcaseId + "/scenario")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .path("steps.name");
    }
}

