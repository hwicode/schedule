package hwicode.schedule.tag.application.query;

import hwicode.schedule.tag.application.query.dto.TagQueryResponse;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TagQueryService {

    private final DailyTagListRepository dailyTagListRepository;

    @Transactional(readOnly = true)
    public List<TagQueryResponse> getTagQueryResponses(LocalDate date) {
        return dailyTagListRepository.findTagQueryResponsesBy(date);
    }

}
