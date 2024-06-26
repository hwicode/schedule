package hwicode.schedule.tag.infra.jpa_repository;

import hwicode.schedule.tag.application.query.dto.DailyTagListMemoQueryResponse;
import hwicode.schedule.tag.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query("SELECT m FROM Memo m "
            + "LEFT JOIN FETCH m.memoTags "
            + "WHERE m.id = :id")
    Optional<Memo> findMemoWithMemoTags(@Param("id") Long memoId);

    // 여기부터 조회 기능
    @Query("SELECT "
            + "new hwicode.schedule.tag.application.query.dto.DailyTagListMemoQueryResponse(m.id, m.text, m.userId) "
            + "FROM Memo m "
            + "WHERE m.dailyTagList.id = :dailyTagListId "
            + "ORDER BY m.id ASC")
    List<DailyTagListMemoQueryResponse> getDailyTagListMemoQueryResponses(@Param("dailyTagListId") Long dailyTagListId);
}
