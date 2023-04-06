package hwicode.schedule.dailyschedule.checklist.domain;

public interface TaskSaveOnlyRepository {
    TaskChecker save(TaskChecker taskChecker);
}
