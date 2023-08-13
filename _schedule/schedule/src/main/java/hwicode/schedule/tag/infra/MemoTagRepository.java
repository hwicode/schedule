package hwicode.schedule.tag.infra;

import hwicode.schedule.tag.domain.MemoTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoTagRepository extends JpaRepository<MemoTag, Long> {
}
