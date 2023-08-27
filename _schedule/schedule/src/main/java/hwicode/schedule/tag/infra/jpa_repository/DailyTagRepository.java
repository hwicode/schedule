package hwicode.schedule.tag.infra.jpa_repository;

import hwicode.schedule.tag.application.query.dto.DailyTagListQueryResponse;
import hwicode.schedule.tag.domain.DailyTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyTagRepository extends JpaRepository<DailyTag, Long> {

    @Query("Delete DailyTag d "
            + "WHERE d.tag.id = :id")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteAllDailyTagsBy(@Param("id") Long tagId);

    // 여기부터 쿼리 조회 기능
    @Query("SELECT new hwicode.schedule.tag.application.query.dto.DailyTagListQueryResponse(d.id, d.today, d.mainTagName) "
            + "FROM DailyTag dt "
            + "INNER JOIN dt.dailyTagList d "
            + "WHERE dt.tag.id = :tagId")
    List<DailyTagListQueryResponse> getDailyTagListQueryResponseFirstPage(@Param("tagId") Long tagId, Pageable pageable);

    @Query("SELECT new hwicode.schedule.tag.application.query.dto.DailyTagListQueryResponse(d.id, d.today, d.mainTagName) "
            + "FROM DailyTag dt "
            + "INNER JOIN dt.dailyTagList d "
            + "WHERE dt.tag.id = :tagId "
                + "AND d.id < :dailyTagListId")
    List<DailyTagListQueryResponse> getDailyTagListQueryResponseNextPage(@Param("tagId") Long tagId,
                                                                          @Param("dailyTagListId") Long dailyTagListId,
                                                                          Pageable pageable);
}
