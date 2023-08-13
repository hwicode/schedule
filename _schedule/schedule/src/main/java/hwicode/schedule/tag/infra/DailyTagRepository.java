package hwicode.schedule.tag.infra;

import hwicode.schedule.tag.domain.DailyTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyTagRepository extends JpaRepository<DailyTag, Long> {
}
