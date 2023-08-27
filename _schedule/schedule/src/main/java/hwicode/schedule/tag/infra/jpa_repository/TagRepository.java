package hwicode.schedule.tag.infra.jpa_repository;

import hwicode.schedule.tag.application.query.dto.TagQueryResponse;
import hwicode.schedule.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query("SELECT "
            + "new hwicode.schedule.tag.application.query.dto.TagQueryResponse(t.id, t.name)"
            + "FROM Tag t")
    List<TagQueryResponse> getTagQueryResponses();
}
