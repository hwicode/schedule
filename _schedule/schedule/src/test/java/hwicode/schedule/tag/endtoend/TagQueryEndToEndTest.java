package hwicode.schedule.tag.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static hwicode.schedule.tag.TagDataHelper.*;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TagQueryEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 특정_태그를_가지고_있는_계획표들_조회_요청() {
        // given
        RequestSpecification requestSpecification = given().port(port)
                .queryParam("tagId", TAG_ID)
                .queryParam("lastDailyTagListId", DAILY_TAG_LIST_ID);

        // when
        Response response = requestSpecification.when()
                .get("/daily-tag-lists");

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 특정_태그를_가지고_있는_메모들_조회_요청() {
        // given
        RequestSpecification requestSpecification = given().port(port)
                .queryParam("tagId", TAG_ID)
                .queryParam("lastMemoId", MEMO_ID);

        // when
        Response response = requestSpecification.when()
                .get("/memos");

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 모든_태그_조회_요청() {
        // given
        RequestSpecification requestSpecification = given().port(port);

        // when
        Response response = requestSpecification.when()
                .get("/tags");

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

}
