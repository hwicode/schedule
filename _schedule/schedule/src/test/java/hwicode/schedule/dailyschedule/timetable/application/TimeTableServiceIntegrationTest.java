package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.domain.*;
import hwicode.schedule.dailyschedule.timetable.infra.LearningTimeRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfSubTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class TimeTableServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TimeTableService timeTableService;

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
    void 타임_테이블에_학습_시간을_추가할_수_있다() {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        TimeTable timeTable = new TimeTable(localDateTime.toLocalDate());
        timeTableRepository.save(timeTable);

        // when
        Long learningTimeId = timeTableService.createLearningTime(timeTable.getId(), localDateTime);

        // then
        assertThat(learningTimeRepository.existsById(learningTimeId)).isTrue();
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간의_시작_시간을_수정할_수_있다() {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        TimeTable timeTable = new TimeTable(localDateTime.toLocalDate());
        timeTable.createLearningTime(localDateTime);
        timeTableRepository.save(timeTable);

        // when
        LocalDateTime newLocalDateTime = localDateTime.plusMinutes(30);
        timeTableService.changeLearningTimeStartTime(timeTable.getId(), localDateTime, newLocalDateTime);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThatThrownBy(() -> savedTimeTable.createLearningTime(newLocalDateTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간의_끝나는_시간을_수정할_수_있다() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        LocalDateTime endTime = startTime.plusMinutes(30);

        TimeTable timeTable = new TimeTable(startTime.toLocalDate());
        timeTable.createLearningTime(startTime);

        timeTableRepository.save(timeTable);

        // when
        timeTableService.changeLearningTimeEndTime(timeTable.getId(), startTime, endTime);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isEqualTo(30);
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간을_삭제할_수_있다() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        LocalDateTime endTime = startTime.plusMinutes(30);

        TimeTable timeTable = new TimeTable(startTime.toLocalDate());
        timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, endTime);

        timeTableRepository.save(timeTable);

        // when
        timeTableService.deleteLearningTime(timeTable.getId(), startTime);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isZero();
    }

    @Test
    void 타임_테이블에_존재하는_특정_학습_주제의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        LocalDateTime endTime = startTime.plusMinutes(30);

        TimeTable timeTable = new TimeTable(startTime.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, endTime);
        learningTime.changeSubject("subject");

        timeTableRepository.save(timeTable);

        // when
        int totalLearningTime = timeTableService.calculateSubjectTotalLearningTime(timeTable.getId(), "subject");

        // then
        assertThat(totalLearningTime).isEqualTo(30);
    }

    @Test
    void 타임_테이블에_존재하는_특정_Task_학습_주제의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        LocalDateTime endTime = startTime.plusMinutes(30);

        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask("학습 주제"));

        TimeTable timeTable = new TimeTable(startTime.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, endTime);
        learningTime.changeSubjectOfTask(subjectOfTask);

        timeTableRepository.save(timeTable);

        // when
        int totalLearningTime = timeTableService.calculateSubjectOfTaskTotalLearningTime(timeTable.getId(), subjectOfTask.getId());

        // then
        assertThat(totalLearningTime).isEqualTo(30);
    }

    @Test
    void 타임_테이블에_존재하는_특정_SubTask_학습_주제의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        LocalDateTime endTime = startTime.plusMinutes(30);

        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask("학습 주제"));

        TimeTable timeTable = new TimeTable(startTime.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, endTime);
        learningTime.changeSubjectOfSubTask(subjectOfSubTask);

        timeTableRepository.save(timeTable);

        // when
        int totalLearningTime = timeTableService.calculateSubjectOfSubTaskTotalLearningTime(timeTable.getId(), subjectOfSubTask.getId());

        // then
        assertThat(totalLearningTime).isEqualTo(30);
    }

}

@Service
class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final LearningTimeSaveOnlyRepository learningTimeSaveOnlyRepository;

    private final SubjectOfTaskRepository subjectOfTaskRepository;
    private final SubjectOfSubTaskRepository subjectOfSubTaskRepository;

    TimeTableService(TimeTableRepository timeTableRepository, SubjectOfTaskRepository subjectOfTaskRepository, SubjectOfSubTaskRepository subjectOfSubTaskRepository, LearningTimeSaveOnlyRepository learningTimeSaveOnlyRepository) {
        this.timeTableRepository = timeTableRepository;
        this.subjectOfTaskRepository = subjectOfTaskRepository;
        this.subjectOfSubTaskRepository = subjectOfSubTaskRepository;
        this.learningTimeSaveOnlyRepository = learningTimeSaveOnlyRepository;
    }

    @Transactional
    public Long createLearningTime(Long timeTableId, LocalDateTime startTime) {
        TimeTable timeTable = findTimeTableById(timeTableId);

        LearningTime learningTime = timeTable.createLearningTime(startTime);
        return learningTimeSaveOnlyRepository.save(learningTime)
                .getId();
    }

    @Transactional
    public LocalDateTime changeLearningTimeStartTime(Long timeTableId, LocalDateTime startTime, LocalDateTime newStartTime) {
        TimeTable timeTable = findTimeTableById(timeTableId);

        return timeTable.changeLearningTimeStartTime(startTime, newStartTime);
    }

    @Transactional
    public LocalDateTime changeLearningTimeEndTime(Long timeTableId, LocalDateTime startTime, LocalDateTime endTime) {
        TimeTable timeTable = findTimeTableById(timeTableId);

        return timeTable.changeLearningTimeEndTime(startTime, endTime);
    }

    @Transactional
    public void deleteLearningTime(Long timeTableId, LocalDateTime startTime) {
        TimeTable timeTable = findTimeTableById(timeTableId);
        timeTable.deleteLearningTime(startTime);
    }

    @Transactional
    public int calculateSubjectTotalLearningTime(Long timeTableId, String subject) {
        TimeTable timeTable = findTimeTableById(timeTableId);
        return timeTable.getSubjectTotalLearningTime(subject);
    }

    @Transactional
    public int calculateSubjectOfTaskTotalLearningTime(Long timeTableId, Long subjectOfTaskId) {
        TimeTable timeTable = findTimeTableById(timeTableId);
        SubjectOfTask subjectOfTask = subjectOfTaskRepository.findById(subjectOfTaskId)
                .orElseThrow(IllegalArgumentException::new);

        return timeTable.getSubjectOfTaskTotalLearningTime(subjectOfTask);
    }

    @Transactional
    public int calculateSubjectOfSubTaskTotalLearningTime(Long timeTableId, Long subjectOfSubTaskId) {
        TimeTable timeTable = findTimeTableById(timeTableId);
        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.findById(subjectOfSubTaskId)
                .orElseThrow(IllegalArgumentException::new);

        return timeTable.getSubjectOfSubTaskTotalLearningTime(subjectOfSubTask);
    }

    private TimeTable findTimeTableById(Long timeTableId) {
        return timeTableRepository.findTimeTableWithLearningTimes(timeTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

}
