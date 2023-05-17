package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.dailyschedule.timetable.exception.application.SubjectOfSubTaskNotFoundException;
import hwicode.schedule.dailyschedule.timetable.exception.application.SubjectOfTaskNotFoundException;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfSubTaskFindRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfTaskFindRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubjectFindService {

    static SubjectOfTask findSubjectOfTask(SubjectOfTaskFindRepository subjectOfTaskFindRepository, Long subjectOfTaskId) {
        return subjectOfTaskFindRepository.findById(subjectOfTaskId)
                .orElseThrow(SubjectOfTaskNotFoundException::new);
    }

    static SubjectOfSubTask findSubjectOfSubTask(SubjectOfSubTaskFindRepository subjectOfSubTaskFindRepository, Long subjectOfSubTaskId) {
        return subjectOfSubTaskFindRepository.findById(subjectOfSubTaskId)
                .orElseThrow(SubjectOfSubTaskNotFoundException::new);
    }
}
