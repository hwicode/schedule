package hwicode.schedule.tag.infra.jpa_repository;

import hwicode.schedule.tag.domain.MemoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoTagRepository extends JpaRepository<MemoTag, Long> {

    @Query("Delete MemoTag m "
            + "WHERE m.tag.id = :id")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteAllMemoTagsBy(@Param("id") Long tagId);
}
