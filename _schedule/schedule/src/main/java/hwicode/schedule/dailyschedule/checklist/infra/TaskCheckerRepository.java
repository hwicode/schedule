package hwicode.schedule.dailyschedule.checklist.infra;

import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.TaskCheckerSaveOnlyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCheckerRepository extends JpaRepository<TaskChecker, Long>, TaskCheckerSaveOnlyRepository {

}
