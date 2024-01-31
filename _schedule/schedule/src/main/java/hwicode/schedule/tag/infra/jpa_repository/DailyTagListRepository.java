package hwicode.schedule.tag.infra.jpa_repository;

import hwicode.schedule.tag.application.query.dto.DailyTagQueryResponse;
import hwicode.schedule.tag.domain.DailyTagList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyTagListRepository extends JpaRepository<DailyTagList, Long> {

    @Query("SELECT d FROM DailyTagList d "
            + "LEFT JOIN FETCH d.dailyTags "
            + "WHERE d.id = :id")
    Optional<DailyTagList> findDailyTagListWithDailyTags(@Param("id") Long dailyTagListId);

    // 여기부터 조회 기능
    @Query("SELECT "
            + "new hwicode.schedule.tag.application.query.dto.DailyTagQueryResponse(t.id, t.name) "
            + "FROM DailyTagList d "
            + "LEFT JOIN d.dailyTags dt "
            + "INNER JOIN dt.tag t "
            + "WHERE d.userId = :userId AND d.today = :today "
            + "ORDER BY t.id ASC")
    List<DailyTagQueryResponse> findDailyTagQueryResponsesBy(@Param("userId") Long userId, @Param("today") LocalDate date);
}
