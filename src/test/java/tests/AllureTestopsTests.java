package tests;

import api.AuthorizationApi;
import api.TestCaseApi;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import models.CreateTestCaseBody;
import models.scenario.CreateTestScenarioBody;
import models.scenario.Steps;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.ArrayList;

import static api.AuthorizationApi.ALLURE_TESTOPS_SESSION;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static helpers.CustomApiListener.withCustomTemplates;

public class AllureTestopsTests {
    public final static String
            USERNAME = "allure8",
            PASSWORD = "allure8",
            USER_TOKEN = "efd32a69-217f-41fa-9701-55f54dd55cd4",
            PROJECT_ID = "1740";

    Faker faker = new Faker();
    public String testCaseName, testScenarioName;

    @BeforeAll
    static void beforeAll() {
        Configuration.baseUrl = "https://allure.autotests.cloud";
        RestAssured.baseURI = "https://allure.autotests.cloud";
        RestAssured.filters(withCustomTemplates());
    }

    @BeforeEach
    void createData() {
        testCaseName = faker.book().title();
        testScenarioName = faker.book().publisher();
    }

    @Test
    void createTestCase() {
        AuthorizationApi authorizationApi = new AuthorizationApi();
        TestCaseApi testCaseApi = new TestCaseApi();

        String xsrfToken = authorizationApi.getXsrfToken(USER_TOKEN);
        String authorizationCookie = authorizationApi
                .getAuthorizationCookie(USER_TOKEN, xsrfToken, USERNAME, PASSWORD);

        CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
        testCaseBody.setName(testCaseName);

        ArrayList<Steps> stepsOne = new ArrayList<Steps>();
        stepsOne.add(new Steps(testScenarioName,""));

        ArrayList<Integer> workPath = new ArrayList<Integer>();
        workPath.add(0);

        CreateTestScenarioBody testScenarioBody = new CreateTestScenarioBody(stepsOne,workPath);

        int testCaseId = testCaseApi.createAndGetTestCaseId(xsrfToken, authorizationCookie, testCaseBody, PROJECT_ID, testCaseName);
        testCaseApi.createAndGetTestCaseScenarioId(xsrfToken, authorizationCookie, testScenarioBody, testCaseId, testScenarioName);

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie(ALLURE_TESTOPS_SESSION, authorizationCookie));
        open("/project/" + PROJECT_ID + "/test-cases/" + testCaseId);
        $(".PaneSection .TestCaseScenarioLayout .Multiline").shouldHave(text(testScenarioName));

    }
}