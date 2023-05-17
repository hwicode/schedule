package hwicode.schedule.dailyschedule.checklist.infra;

import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTaskCheckerRepository extends JpaRepository<SubTaskChecker, Long> {

}
