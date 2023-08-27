package hwicode.schedule.tag.application.query;

import hwicode.schedule.tag.application.query.dto.DailyTagQueryResponse;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DailyTagListQueryService {

    private final DailyTagListRepository dailyTagListRepository;

    @Transactional(readOnly = true)
    public List<DailyTagQueryResponse> getDailyTagQueryResponses(LocalDate date) {
        return dailyTagListRepository.findDailyTagQueryResponsesBy(date);
    }

}