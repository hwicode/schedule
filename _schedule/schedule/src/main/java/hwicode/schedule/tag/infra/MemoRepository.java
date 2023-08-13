package hwicode.schedule.tag.infra;

import hwicode.schedule.tag.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}
