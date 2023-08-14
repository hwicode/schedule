package hwicode.schedule.tag.application.find_service;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.exception.application.DailyTagListNotFoundException;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DailyTagListFindServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyTagListRepository dailyTagListRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 존재하지_않는_DailyTagList를_조회하면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;

        // when
        assertThatThrownBy(() -> DailyTagListFindService.findById(dailyTagListRepository, noneExistId))
                .isInstanceOf(DailyTagListNotFoundException.class);
    }

    @Test
    void 존재하지_않는_DailyTagList와_DailyTags를_함께_조회하면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;

        // when
        assertThatThrownBy(() -> DailyTagListFindService.findDailyTagListWithDailyTags(dailyTagListRepository, noneExistId))
                .isInstanceOf(DailyTagListNotFoundException.class);
    }

}
