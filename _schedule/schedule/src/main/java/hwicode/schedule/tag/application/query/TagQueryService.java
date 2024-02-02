package hwicode.schedule.tag.application.query;

import hwicode.schedule.common.login.validator.PermissionValidator;
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
    public List<DailyTagListSearchQueryResponse> getDailyTagListSearchQueryResponsePage(Long userId, Long tagId, Long lastDailyTagListId) {
        PageRequest pageable = PageRequest.of(PAGE, PAGE_SIZE, Sort.by("id").descending());

        if (lastDailyTagListId == null) {
            List<DailyTagListSearchQueryResponse> dailyTagListSearchQueryResponseFirstPage = dailyTagRepository.getDailyTagListSearchQueryResponseFirstPage(tagId, pageable);
            validateTagOwnership(userId, dailyTagListSearchQueryResponseFirstPage);
            return dailyTagListSearchQueryResponseFirstPage;
        }
        List<DailyTagListSearchQueryResponse> dailyTagListSearchQueryResponseNextPage = dailyTagRepository.getDailyTagListSearchQueryResponseNextPage(tagId, lastDailyTagListId, pageable);
        validateTagOwnership(userId, dailyTagListSearchQueryResponseNextPage);
        return dailyTagListSearchQueryResponseNextPage;
    }

    private void validateTagOwnership(Long userId, List<DailyTagListSearchQueryResponse> dailyTagListSearchQueryResponses) {
        dailyTagListSearchQueryResponses.forEach(
                tag -> PermissionValidator.validateOwnership(userId, tag.getUserId())
        );
    }

    @Transactional(readOnly = true)
    public List<MemoSearchQueryResponse> getMemoSearchQueryResponsePage(Long userId, Long tagId, Long lastMemoId) {
        PageRequest pageable = PageRequest.of(PAGE, PAGE_SIZE, Sort.by("id").descending());

        if (lastMemoId == null) {
            List<MemoSearchQueryResponse> memoSearchQueryResponseFirstPage = memoTagRepository.getMemoSearchQueryResponseFirstPage(tagId, pageable);
            validateMemoOwnership(userId, memoSearchQueryResponseFirstPage);
            return memoSearchQueryResponseFirstPage;
        }
        List<MemoSearchQueryResponse> memoSearchQueryResponseNextPage = memoTagRepository.getMemoSearchQueryResponseNextPage(tagId, lastMemoId, pageable);
        validateMemoOwnership(userId, memoSearchQueryResponseNextPage);
        return memoSearchQueryResponseNextPage;
    }

    private void validateMemoOwnership(Long userId, List<MemoSearchQueryResponse> memoSearchQueryResponses) {
        memoSearchQueryResponses.forEach(
                memo -> PermissionValidator.validateOwnership(userId, memo.getUserId())
        );
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
