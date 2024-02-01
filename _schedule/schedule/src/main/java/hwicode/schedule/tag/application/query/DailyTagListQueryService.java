package hwicode.schedule.tag.application.query;

import hwicode.schedule.tag.application.find_service.DailyTagListFindService;
import hwicode.schedule.tag.application.query.dto.DailyTagListMemoQueryResponse;
import hwicode.schedule.tag.application.query.dto.DailyTagQueryResponse;
import hwicode.schedule.tag.application.query.dto.MemoTagQueryResponse;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import hwicode.schedule.tag.infra.jpa_repository.MemoRepository;
import hwicode.schedule.tag.infra.jpa_repository.MemoTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Service
public class DailyTagListQueryService {

    private final DailyTagListRepository dailyTagListRepository;
    private final MemoRepository memoRepository;
    private final MemoTagRepository memoTagRepository;

    @Transactional(readOnly = true)
    public List<DailyTagQueryResponse> getDailyTagQueryResponses(Long userId, LocalDate date) {
        return dailyTagListRepository.findDailyTagQueryResponsesBy(userId, date);
    }

    @Transactional(readOnly = true)
    public List<DailyTagListMemoQueryResponse> getDailyTagListMemoQueryResponses(Long userId, Long dailyTagListId) {
        validateOwnership(userId, dailyTagListId);
        List<DailyTagListMemoQueryResponse> dailyTagListMemoQueryResponses = memoRepository.getDailyTagListMemoQueryResponses(dailyTagListId);
        List<Long> memoIds = getMemoIds(dailyTagListMemoQueryResponses);

        Map<Long, List<MemoTagQueryResponse>> tagsQueryResponseMap = memoTagRepository.findMemoTagsQueryResponsesBy(memoIds)
                .stream()
                .collect(groupingBy(MemoTagQueryResponse::getMemoId));

        dailyTagListMemoQueryResponses.forEach(
                dailyTagListMemoQueryResponse -> dailyTagListMemoQueryResponse.setMemoTagQueryResponses(
                        tagsQueryResponseMap.get(dailyTagListMemoQueryResponse.getId())
                )
        );
        return dailyTagListMemoQueryResponses;
    }

    private void validateOwnership(Long userId, Long dailyTagListId) {
        DailyTagList dailyTagList = DailyTagListFindService.findById(dailyTagListRepository, dailyTagListId);
        dailyTagList.checkOwnership(userId);
    }

    private List<Long> getMemoIds(List<DailyTagListMemoQueryResponse> dailyTagListMemoQueryResponses) {
        return dailyTagListMemoQueryResponses.stream()
                .map(DailyTagListMemoQueryResponse::getId)
                .collect(Collectors.toList());
    }

}
