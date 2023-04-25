package hwicode.schedule.dailyschedule.timetable.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.application.TimeTableService;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.exception.domain.timetablevalidator.StartTimeDuplicateException;
import hwicode.schedule.dailyschedule.timetable.infra.TimeTableRepository;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimeTableEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TimeTableService timeTableService;

    @Autowired
    TimeTableRepository timeTableRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 학습_시간_생성_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTableRepository.save(timeTable);

        LearningTimeSaveRequest learningTimeSaveRequest = createLearningTimeSaveRequest(START_TIME);

        RequestSpecification requestSpecification = given()
                .pathParam("timeTableId", timeTable.getId())
                .contentType(ContentType.JSON)
                .body(learningTimeSaveRequest);

        // when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/dailyschedule/timetables/{timeTableId}", port));

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThatThrownBy(() -> timeTableService.saveLearningTime(timeTable.getId(), START_TIME))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 학습_시간_시작_시간_변경_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        StartTimeModifyRequest startTimeModifyRequest = createStartTimeModifyRequest(timeTable.getId(), NEW_START_TIME);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(startTimeModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/timetable/%s/starttime", port, START_TIME));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        assertThatThrownBy(() -> timeTableService.saveLearningTime(timeTable.getId(), NEW_START_TIME))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 학습_시간_끝나는_시간_변경_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        LocalDateTime endTime = START_TIME.plusMinutes(30);
        EndTimeModifyRequest endTimeModifyRequest = createEndTimeModifyRequest(timeTable.getId(), endTime);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(endTimeModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/timetable/%s/endtime", port, START_TIME));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isEqualTo(30);
    }

}
