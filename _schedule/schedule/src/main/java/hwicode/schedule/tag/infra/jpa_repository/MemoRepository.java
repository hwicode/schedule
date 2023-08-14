package hwicode.schedule.tag.infra.jpa_repository;

import hwicode.schedule.tag.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query("SELECT m FROM Memo m "
            + "LEFT JOIN FETCH m.memoTags "
            + "WHERE m.id = :id")
    Optional<Memo> findMemoWithMemoTags(@Param("id") Long memoId);
}
