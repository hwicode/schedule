package hwicode.schedule.tag.infra.jpa_repository;

import hwicode.schedule.tag.application.query.dto.TagQueryResponse;
import hwicode.schedule.tag.application.query.dto.TagSearchQueryResponse;
import hwicode.schedule.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// Tag의 name에 세컨더리 인덱스 존재함
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query("SELECT "
            + "new hwicode.schedule.tag.application.query.dto.TagQueryResponse(t.id, t.name) "
            + "FROM Tag t "
            + "ORDER BY t.id ASC")
    List<TagQueryResponse> getTagQueryResponses();

    @Query("SELECT "
            + "new hwicode.schedule.tag.application.query.dto.TagSearchQueryResponse(t.id, t.name) "
            + "FROM Tag t "
            + "WHERE t.name LIKE :nameKeyword%")
    List<TagSearchQueryResponse> getTagSearchQueryResponses(@Param("nameKeyword") String nameKeyword);
}
