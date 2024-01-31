package hwicode.schedule.timetable.application.query;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.timetable.application.query.dto.LearningTimeQueryResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectOfSubTaskTotalLearningTimeResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectOfTaskTotalLearningTimeResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectTotalLearningTimeResponse;
import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfSubTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static hwicode.schedule.timetable.TimeTableDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TimeTableQueryServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TimeTableQueryService timeTableQueryService;

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
    void 특정_날짜의_학습_시간들을_조회할_수_있다() {
        // given
        LocalDate date = LocalDate.of(2023, 8, 24);

        Long userId = 1L;
        TimeTable timeTable = new TimeTable(date, userId);
        timeTableRepository.save(timeTable);

        SubjectOfTask subjectOfTask = new SubjectOfTask(SUBJECT, userId);
        subjectOfTaskRepository.save(subjectOfTask);

        SubjectOfSubTask subjectOfSubTask = new SubjectOfSubTask(NEW_SUBJECT, userId);
        subjectOfSubTaskRepository.save(subjectOfSubTask);

        LocalDateTime startTime = date.atStartOfDay();
        LearningTimeQueryResponse learningTimeQueryResponse = createLearningTimeWithSubjectOfTask(timeTable, startTime, subjectOfTask, userId);
        LearningTimeQueryResponse learningTimeQueryResponse2 = createLearningTimeWithSubjectOfSubTask(timeTable, startTime.plusHours(1), subjectOfSubTask, userId);
        LearningTimeQueryResponse learningTimeQueryResponse3 = createLearningTimeWithSubject(timeTable, startTime.plusHours(2), SUBJECT, userId);

        // when
        List<LearningTimeQueryResponse> result = timeTableQueryService.getLearningTimeQueryResponses(userId, date);

        // then
        List<LearningTimeQueryResponse> expectedResponses = List.of(learningTimeQueryResponse, learningTimeQueryResponse2, learningTimeQueryResponse3);
        assertThat(result).isEqualTo(expectedResponses);
    }

    private LearningTimeQueryResponse createLearningTimeWithSubjectOfTask(TimeTable timeTable, LocalDateTime startTime, SubjectOfTask subjectOfTask, Long userId) {
        LearningTime learningTime = new LearningTime(timeTable, startTime, userId);
        learningTime.changeSubjectOfTask(subjectOfTask);

        learningTimeRepository.save(learningTime);

        return LearningTimeQueryResponse.builder()
                .id(learningTime.getId())
                .startTime(startTime)
                .endTime(null)
                .taskId(subjectOfTask.getId())
                .build();
    }

    private LearningTimeQueryResponse createLearningTimeWithSubjectOfSubTask(TimeTable timeTable, LocalDateTime startTime, SubjectOfSubTask subjectOfSubTask, Long userId) {
        LearningTime learningTime = new LearningTime(timeTable, startTime, userId);
        learningTime.changeSubjectOfSubTask(subjectOfSubTask);

        learningTimeRepository.save(learningTime);

        return  LearningTimeQueryResponse.builder()
                .id(learningTime.getId())
                .startTime(startTime)
                .endTime(null)
                .subTaskId(subjectOfSubTask.getId())
                .build();
    }

    private LearningTimeQueryResponse createLearningTimeWithSubject(TimeTable timeTable, LocalDateTime startTime, String subject, Long userId) {
        LearningTime learningTime = new LearningTime(timeTable, startTime, userId);
        learningTime.changeSubject(subject);

        learningTimeRepository.save(learningTime);

        return  LearningTimeQueryResponse.builder()
                .id(learningTime.getId())
                .startTime(startTime)
                .endTime(null)
                .subject(SUBJECT)
                .build();
    }

    @Test
    void 타임_테이블에_존재하는_특정_학습_주제의_총_학습_시간을_계산할_수_있다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);

        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));
        learningTime.changeSubject(SUBJECT);

        timeTableRepository.save(timeTable);

        // when
        SubjectTotalLearningTimeResponse subjectTotalLearningTimeResponse = timeTableQueryService.calculateSubjectTotalLearningTime(timeTable.getId(), SUBJECT);

        // then
        assertThat(subjectTotalLearningTimeResponse.getSubjectTotalLearningTime()).isEqualTo(30);
    }

    @Test
    void 타임_테이블에_존재하는_특정_Task_학습_주제의_총_학습_시간을_계산할_수_있다() {
        // given
        Long userId = 1L;
        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(SUBJECT, userId));

        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);

        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));
        learningTime.changeSubjectOfTask(subjectOfTask);

        timeTableRepository.save(timeTable);

        // when
        SubjectOfTaskTotalLearningTimeResponse subjectOfTaskTotalLearningTimeResponse = timeTableQueryService.calculateSubjectOfTaskTotalLearningTime(timeTable.getId(), subjectOfTask.getId());

        // then
        assertThat(subjectOfTaskTotalLearningTimeResponse.getSubjectOfTaskTotalLearningTime()).isEqualTo(30);
    }

    @Test
    void 타임_테이블에_존재하는_특정_SubTask_학습_주제의_총_학습_시간을_계산할_수_있다() {
        // given
        Long userId = 1L;
        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(SUBJECT, userId));

        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);

        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));
        learningTime.changeSubjectOfSubTask(subjectOfSubTask);

        timeTableRepository.save(timeTable);

        // when
        SubjectOfSubTaskTotalLearningTimeResponse subjectOfSubTaskTotalLearningTimeResponse = timeTableQueryService.calculateSubjectOfSubTaskTotalLearningTime(timeTable.getId(), subjectOfSubTask.getId());

        // then
        assertThat(subjectOfSubTaskTotalLearningTimeResponse.getSubjectOfSubTaskTotalLearningTime()).isEqualTo(30);
    }

}
