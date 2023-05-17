package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.exception.application.DailyToDoListNotExistException;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto.DailyToDoListInformationChangeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class DailyToDoListAggregateServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyToDoListAggregateService dailyToDoListAggregateService;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void ToDo_리스트의_정보를_변경할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        String review = "좋았다!";
        DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest = new DailyToDoListInformationChangeRequest(review, Emoji.GOOD);

        // when
        dailyToDoListAggregateService.changeDailyToDoListInformation(dailyToDoList.getId(), dailyToDoListInformationChangeRequest);

        // then
        DailyToDoList savedDailyToDoList = dailyToDoListRepository.findById(dailyToDoList.getId()).orElseThrow();
        assertThat(savedDailyToDoList.writeReview(review)).isFalse();
        assertThat(savedDailyToDoList.changeTodayEmoji(Emoji.GOOD)).isFalse();
    }

    @Test
    void 존재하지_않는_투두리스트를_조회하면_에러가_발생한다() {
        //given
        Long noneExistId = 1L;

        String review = "좋았다!";
        DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest = new DailyToDoListInformationChangeRequest(review, Emoji.GOOD);

        // when then
        assertThatThrownBy(() -> dailyToDoListAggregateService.changeDailyToDoListInformation(noneExistId, dailyToDoListInformationChangeRequest))
                .isInstanceOf(DailyToDoListNotExistException.class);
    }
}
