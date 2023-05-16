package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.dailyschedule.timetable.exception.application.SubjectOfSubTaskNotFoundException;
import hwicode.schedule.dailyschedule.timetable.exception.application.SubjectOfTaskNotFoundException;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfSubTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfTaskRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubjectFindService {

    static SubjectOfTask findSubjectOfTask(SubjectOfTaskRepository subjectOfTaskRepository, Long subjectOfTaskId) {
        return subjectOfTaskRepository.findById(subjectOfTaskId)
                .orElseThrow(SubjectOfTaskNotFoundException::new);
    }

    static SubjectOfSubTask findSubjectOfSubTask(SubjectOfSubTaskRepository subjectOfSubTaskRepository, Long subjectOfSubTaskId) {
        return subjectOfSubTaskRepository.findById(subjectOfSubTaskId)
                .orElseThrow(SubjectOfSubTaskNotFoundException::new);
    }
}
