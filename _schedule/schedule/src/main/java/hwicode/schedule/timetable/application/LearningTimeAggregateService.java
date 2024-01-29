package hwicode.schedule.timetable.application;

import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeDeleteSubjectCommand;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeModifySubjectCommand;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeModifySubjectOfSubTaskCommand;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeModifySubjectOfTaskCommand;
import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.timetable.infra.limited_repository.LearningTimeFindRepository;
import hwicode.schedule.timetable.infra.limited_repository.SubjectOfSubTaskFindRepository;
import hwicode.schedule.timetable.infra.limited_repository.SubjectOfTaskFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LearningTimeAggregateService {

    private final LearningTimeFindRepository learningTimeFindRepository;
    private final SubjectOfTaskFindRepository subjectOfTaskFindRepository;
    private final SubjectOfSubTaskFindRepository subjectOfSubTaskFindRepository;

    @Transactional
    public boolean deleteSubject(LearningTimeDeleteSubjectCommand command) {
        LearningTime learningTime = learningTimeFindRepository.findById(command.getLearningTimeId());
        learningTime.checkOwnership(command.getUserId());

        return learningTime.deleteSubject();
    }

    @Transactional
    public String changeSubject(LearningTimeModifySubjectCommand command) {
        LearningTime learningTime = learningTimeFindRepository.findById(command.getLearningTimeId());
        learningTime.checkOwnership(command.getUserId());

        return learningTime.changeSubject(command.getSubject());
    }

    @Transactional
    public String changeSubjectOfTask(LearningTimeModifySubjectOfTaskCommand command) {
        LearningTime learningTime = learningTimeFindRepository.findById(command.getLearningTimeId());
        learningTime.checkOwnership(command.getUserId());

        SubjectOfTask subjectOfTask = subjectOfTaskFindRepository.findById(command.getSubjectOfTaskId());
        subjectOfTask.checkOwnership(command.getUserId());
        return learningTime.changeSubjectOfTask(subjectOfTask);
    }

    @Transactional
    public String changeSubjectOfSubTask(LearningTimeModifySubjectOfSubTaskCommand command) {
        LearningTime learningTime = learningTimeFindRepository.findById(command.getLearningTimeId());
        learningTime.checkOwnership(command.getUserId());

        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskFindRepository.findById(command.getSubjectOfSubTaskId());
        subjectOfSubTask.checkOwnership(command.getUserId());
        return learningTime.changeSubjectOfSubTask(subjectOfSubTask);
    }
}
