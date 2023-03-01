package hwicode.schedule.dailyschedule.checklist.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface SubTaskSaveOnlyRepository {
    SubTask save(SubTask subTask);
}
