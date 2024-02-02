package hwicode.schedule.dailyschedule.todolist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto.DailyToDoListInformationChangeRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DailyToDoListEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TokenProvider tokenProvider;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 투두리스트_정보_변경_요청() {
        //given
        Long userId = 1L;
        String accessToken = ToDoListDataHelper.createAccessToken(tokenProvider, userId);

        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD, userId);
        dailyToDoListRepository.save(dailyToDoList);

        String review = "좋은데!";
        DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest = new DailyToDoListInformationChangeRequest(review, Emoji.GOOD);

        RequestSpecification requestSpecification = given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .contentType(ContentType.JSON)
                .body(dailyToDoListInformationChangeRequest);

        //when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/information", dailyToDoList.getId());

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        DailyToDoList savedDailyToDoList = dailyToDoListRepository.findById(dailyToDoList.getId()).orElseThrow();
        assertThat(savedDailyToDoList.writeReview(review)).isFalse();
        assertThat(savedDailyToDoList.changeTodayEmoji(Emoji.GOOD)).isFalse();
    }
}
