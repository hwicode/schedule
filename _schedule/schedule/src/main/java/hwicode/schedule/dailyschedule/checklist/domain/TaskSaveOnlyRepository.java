package hwicode.schedule.dailyschedule.checklist.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface TaskSaveOnlyRepository {
    Task save(Task task);
}
