package hwicode.schedule.tag.infra.limited_repository;

import hwicode.schedule.tag.infra.jpa_repository.MemoTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemoTagConstraintRepository {

    private final MemoTagRepository memoTagRepository;

    public void deleteTagForeignKeyConstraint(Long tagId) {
        memoTagRepository.deleteAllMemoTagsBy(tagId);
    }
}
