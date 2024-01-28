package hwicode.schedule.timetable.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.timetable.application.TimeTableAggregateService;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeSaveCommand;
import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.exception.domain.timetablevalidator.StartTimeDuplicateException;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfSubTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import hwicode.schedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyRequest;
import hwicode.schedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyRequest;
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

import static hwicode.schedule.timetable.TimeTableDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimeTableEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TimeTableAggregateService timeTableAggregateService;

    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    SubjectOfTaskRepository subjectOfTaskRepository;

    @Autowired
    SubjectOfSubTaskRepository subjectOfSubTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 학습_시간_생성_요청() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        timeTableRepository.save(timeTable);

        LearningTimeSaveRequest learningTimeSaveRequest = new LearningTimeSaveRequest(START_TIME);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(learningTimeSaveRequest);

        // when
        Response response = requestSpecification.when()
                .post("/dailyschedule/timetables/{timeTableId}/learning-times", timeTable.getId());

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        LearningTimeSaveCommand command = new LearningTimeSaveCommand(userId, timeTable.getId(), START_TIME);
        assertThatThrownBy(() -> timeTableAggregateService.saveLearningTime(command))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 학습_시간_시작_시간_변경_요청() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        StartTimeModifyRequest startTimeModifyRequest = new StartTimeModifyRequest(START_TIME, NEW_START_TIME);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(startTimeModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/start-time",
                        timeTable.getId(), learningTime.getId());

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        LearningTimeSaveCommand command = new LearningTimeSaveCommand(userId, timeTable.getId(), NEW_START_TIME);
        assertThatThrownBy(() -> timeTableAggregateService.saveLearningTime(command))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 학습_시간_끝나는_시간_변경_요청() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        LocalDateTime endTime = START_TIME.plusMinutes(30);
        EndTimeModifyRequest endTimeModifyRequest = new EndTimeModifyRequest(START_TIME, endTime);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(endTimeModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/end-time", timeTable.getId(), learningTime.getId());

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isEqualTo(30);
    }

    @Test
    void 학습_시간_삭제_요청() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));
        timeTableRepository.save(timeTable);

        RequestSpecification requestSpecification = given()
                .port(port)
                .queryParam("startTime", String.valueOf(START_TIME));

        // when
        Response response = requestSpecification.when()
                .delete("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}", timeTable.getId(), learningTime.getId());

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isZero();
    }

    @Test
    void 특정_학습_주제_총_학습_시간_요청() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);

        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));
        learningTime.changeSubject(SUBJECT);

        timeTableRepository.save(timeTable);

        RequestSpecification requestSpecification = given()
                .port(port)
                .queryParam("subject", SUBJECT);

        // when
        Response response = requestSpecification.when()
                .get("/dailyschedule/timetables/{timeTableId}/subject-total-time", timeTable.getId());

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void Task_학습_주제_총_학습_시간_요청() {
        // given
        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(SUBJECT));

        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);

        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));
        learningTime.changeSubjectOfTask(subjectOfTask);

        timeTableRepository.save(timeTable);

        RequestSpecification requestSpecification = given()
                .port(port)
                .queryParam("subject_of_task_id", subjectOfTask.getId());

        // when
        Response response = requestSpecification.when()
                .get("/dailyschedule/timetables/{timeTableId}/task-total-time", timeTable.getId());

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void SubTask_학습_주제_총_학습_시간_요청() {
        // given
        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(SUBJECT));

        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);

        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));
        learningTime.changeSubjectOfSubTask(subjectOfSubTask);

        timeTableRepository.save(timeTable);

        RequestSpecification requestSpecification = given()
                .port(port)
                .queryParam("subject_of_subtask_id", subjectOfSubTask.getId());

        // when
        Response response = requestSpecification.when()
                .get("/dailyschedule/timetables/{timeTableId}/subtask-total-time", timeTable.getId());

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }
}
