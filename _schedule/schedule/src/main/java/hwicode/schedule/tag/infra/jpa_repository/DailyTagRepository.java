package hwicode.schedule.tag.infra.jpa_repository;

import hwicode.schedule.tag.domain.DailyTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DailyTagRepository extends JpaRepository<DailyTag, Long> {

    @Query("Delete DailyTag d "
            + "WHERE d.tag.id = :id")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteAllDailyTagsBy(@Param("id") Long tagId);
}
