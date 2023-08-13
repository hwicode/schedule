package hwicode.schedule.tag.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Memo;
import hwicode.schedule.tag.exception.application.MemoNotFoundException;
import hwicode.schedule.tag.infra.DailyTagListRepository;
import hwicode.schedule.tag.infra.MemoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.tag.TagDataHelper.MEMO_TEXT;
import static hwicode.schedule.tag.TagDataHelper.NEW_MEMO_TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemoServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    MemoService memoService;

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    DailyTagListRepository dailyTagListRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 메모의_내용를_변경할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        // when
        String changedText = memoService.changeMemoText(memo.getId(), NEW_MEMO_TEXT);

        // then
        Memo savedMemo = memoRepository.findById(memo.getId()).orElseThrow();
        assertThat(savedMemo.changeText(changedText)).isFalse();
    }

    @Test
    void 존재하지_않는_메모를_조회하면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;

        // when then
        assertThatThrownBy(() -> memoService.changeMemoText(noneExistId, MEMO_TEXT))
                .isInstanceOf(MemoNotFoundException.class);
    }

}
