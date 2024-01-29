package hwicode.schedule.tag.infra.jpa_repository;

import hwicode.schedule.tag.application.query.dto.MemoSearchQueryResponse;
import hwicode.schedule.tag.application.query.dto.MemoTagQueryResponse;
import hwicode.schedule.tag.domain.MemoTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoTagRepository extends JpaRepository<MemoTag, Long> {

    @Query("Delete MemoTag m "
            + "WHERE m.tag.id = :id")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteAllMemoTagsBy(@Param("id") Long tagId);

    // 여기부터 쿼리 조회 기능
    @Query("SELECT new hwicode.schedule.tag.application.query.dto.MemoSearchQueryResponse(m.id, m.text) "
            + "FROM MemoTag mt "
            + "INNER JOIN mt.memo m "
            + "WHERE mt.tag.id = :tagId")
    List<MemoSearchQueryResponse> getMemoSearchQueryResponseFirstPage(@Param("tagId") Long tagId, Pageable pageable);

    @Query("SELECT new hwicode.schedule.tag.application.query.dto.MemoSearchQueryResponse(m.id, m.text) "
            + "FROM MemoTag mt "
            + "INNER JOIN mt.memo m "
            + "WHERE mt.tag.id = :tagId "
                + "AND m.id < :memoId")
    List<MemoSearchQueryResponse> getMemoSearchQueryResponseNextPage(@Param("tagId") Long tagId,
                                                                     @Param("memoId") Long memoId,
                                                                     Pageable pageable);

    @Query("SELECT "
            + "new hwicode.schedule.tag.application.query.dto.MemoTagQueryResponse(t.id, t.name, mt.memo.id) "
            + "FROM MemoTag mt "
            + "INNER JOIN mt.tag t "
            + "WHERE mt.memo.id IN (:memoIds) "
            + "ORDER BY mt.memo.id ASC")
    List<MemoTagQueryResponse> findMemoTagsQueryResponsesBy(@Param("memoIds") List<Long> memoIds);
}
