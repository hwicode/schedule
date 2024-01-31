package hwicode.schedule.tag.application.query;

import hwicode.schedule.tag.application.query.dto.DailyTagListSearchQueryResponse;
import hwicode.schedule.tag.application.query.dto.MemoSearchQueryResponse;
import hwicode.schedule.tag.application.query.dto.TagQueryResponse;
import hwicode.schedule.tag.application.query.dto.TagSearchQueryResponse;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.MemoTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TagQueryService {

    private static final int PAGE = 0;
    private static final int PAGE_SIZE = 10;

    private final DailyTagRepository dailyTagRepository;
    private final MemoTagRepository memoTagRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<DailyTagListSearchQueryResponse> getDailyTagListSearchQueryResponsePage(Long tagId, Long lastDailyTagListId) {
        PageRequest pageable = PageRequest.of(PAGE, PAGE_SIZE, Sort.by("id").descending());

        if (lastDailyTagListId == null) {
            return dailyTagRepository.getDailyTagListSearchQueryResponseFirstPage(tagId, pageable);
        }
        return dailyTagRepository.getDailyTagListSearchQueryResponseNextPage(tagId, lastDailyTagListId, pageable);
    }

    @Transactional(readOnly = true)
    public List<MemoSearchQueryResponse> getMemoSearchQueryResponsePage(Long tagId, Long lastMemoId) {
        PageRequest pageable = PageRequest.of(PAGE, PAGE_SIZE, Sort.by("id").descending());

        if (lastMemoId == null) {
            return memoTagRepository.getMemoSearchQueryResponseFirstPage(tagId, pageable);
        }
        return memoTagRepository.getMemoSearchQueryResponseNextPage(tagId, lastMemoId, pageable);
    }

    @Transactional(readOnly = true)
    public List<TagQueryResponse> getTagQueryResponses(Long userId) {
        return tagRepository.getTagQueryResponses(userId);
    }

    @Transactional(readOnly = true)
    public List<TagSearchQueryResponse> getTagSearchQueryResponses(Long userId, String nameKeyword) {
        return tagRepository.getTagSearchQueryResponses(userId, nameKeyword);
    }

}
