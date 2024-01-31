package hwicode.schedule.tag.infra.jpa_repository;

import hwicode.schedule.tag.application.query.dto.TagQueryResponse;
import hwicode.schedule.tag.application.query.dto.TagSearchQueryResponse;
import hwicode.schedule.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query("SELECT "
            + "new hwicode.schedule.tag.application.query.dto.TagQueryResponse(t.id, t.name) "
            + "FROM Tag t "
            + "WHERE t.userId = :userId "
            + "ORDER BY t.id ASC")
    List<TagQueryResponse> getTagQueryResponses(@Param("userId") Long userId);

    @Query("SELECT "
            + "new hwicode.schedule.tag.application.query.dto.TagSearchQueryResponse(t.id, t.name) "
            + "FROM Tag t "
            + "WHERE t.userId = :userId AND t.name LIKE :nameKeyword%")
    List<TagSearchQueryResponse> getTagSearchQueryResponses(@Param("userId") Long userId, @Param("nameKeyword") String nameKeyword);
}
