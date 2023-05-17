package hwicode.schedule.dailyschedule.timetable.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.SubjectOfSubTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.SubjectOfTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.TimeTableRepository;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subject_modify.LearningTimeSubjectModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify.LearningTimeSubjectOfSubTaskModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LearningTimeEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    LearningTimeRepository learningTimeRepository;

    @Autowired
    SubjectOfTaskRepository subjectOfTaskRepository;

    @Autowired
    SubjectOfSubTaskRepository subjectOfSubTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 학습_주제_삭제_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        learningTime.changeSubject(SUBJECT);
        timeTableRepository.save(timeTable);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON);

        // when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/dailyschedule/learning-times/%s", port, learningTime.getId()));

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();
    }

    @Test
    void 학습_주제_수정_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        LearningTimeSubjectModifyRequest learningTimeSubjectModifyRequest = new LearningTimeSubjectModifyRequest(NEW_SUBJECT);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(learningTimeSubjectModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/learning-times/%s/subject", port, learningTime.getId()));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void Task_학습_주제_수정_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(SUBJECT));

        LearningTimeSubjectOfTaskModifyRequest learningTimeSubjectOfTaskModifyRequest = new LearningTimeSubjectOfTaskModifyRequest(
                subjectOfTask.getId());

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(learningTimeSubjectOfTaskModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/learning-times/%s/subject-of-task", port, learningTime.getId()));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void SubTask_학습_주제_수정_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(SUBJECT));

        LearningTimeSubjectOfSubTaskModifyRequest learningTimeSubjectOfSubTaskModifyRequest = new LearningTimeSubjectOfSubTaskModifyRequest(
                subjectOfSubTask.getId());

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(learningTimeSubjectOfSubTaskModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/learning-times/%s/subject-of-subtask", port, learningTime.getId()));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }
}
