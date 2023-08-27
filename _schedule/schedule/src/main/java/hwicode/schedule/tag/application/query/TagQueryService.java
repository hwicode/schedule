package hwicode.schedule.tag.application.query;

import hwicode.schedule.tag.application.query.dto.DailyTagListQueryResponse;
import hwicode.schedule.tag.application.query.dto.MemoSearchQueryResponse;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.MemoTagRepository;
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

    @Transactional(readOnly = true)
    public List<DailyTagListQueryResponse> getDailyTagListQueryResponsePage(Long tagId, Long lastDailyTagListId) {
        PageRequest pageable = PageRequest.of(PAGE, PAGE_SIZE, Sort.by("id").descending());

        if (lastDailyTagListId == null) {
            return dailyTagRepository.getDailyTagListQueryResponseFirstPage(tagId, pageable);
        }
        return dailyTagRepository.getDailyTagListQueryResponseNextPage(tagId, lastDailyTagListId, pageable);
    }

    @Transactional(readOnly = true)
    public List<MemoSearchQueryResponse> getMemoSearchQueryResponsePage(Long tagId, Long lastMemoId) {
        PageRequest pageable = PageRequest.of(PAGE, PAGE_SIZE, Sort.by("id").descending());

        if (lastMemoId == null) {
            return memoTagRepository.getMemoSearchQueryResponseFirstPage(tagId, pageable);
        }
        return memoTagRepository.getMemoSearchQueryResponseNextPage(tagId, lastMemoId, pageable);
    }

}
