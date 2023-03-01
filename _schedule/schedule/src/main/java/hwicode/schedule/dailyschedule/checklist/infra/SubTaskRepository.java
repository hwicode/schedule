package hwicode.schedule.dailyschedule.checklist.infra;

import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskSaveOnlyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTaskRepository extends JpaRepository<SubTask, Long>, SubTaskSaveOnlyRepository {

}
