package hwicode.schedule.tag.application.query;

import hwicode.schedule.tag.application.query.dto.DailyTagListQueryResponse;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagRepository;
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

    @Transactional(readOnly = true)
    public List<DailyTagListQueryResponse> getDailyTagListQueryResponsePage(Long tagId, Long lastDailyTagListId) {
        PageRequest pageable = PageRequest.of(PAGE, PAGE_SIZE, Sort.by("id").descending());

        if (lastDailyTagListId == null) {
            return dailyTagRepository.getDailyTagListQueryResponseFirstPage(tagId, pageable);
        }
        return dailyTagRepository.getDailyTagListQueryResponseNextPage(tagId, lastDailyTagListId, pageable);
    }

}
