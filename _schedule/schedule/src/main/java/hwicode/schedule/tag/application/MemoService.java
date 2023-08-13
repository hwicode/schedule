package hwicode.schedule.tag.application;

import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Memo;
import hwicode.schedule.tag.exception.application.DailyTagListNotFoundException;
import hwicode.schedule.tag.exception.application.MemoNotFoundException;
import hwicode.schedule.tag.infra.DailyTagListRepository;
import hwicode.schedule.tag.infra.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final DailyTagListRepository dailyTagListRepository;

    @Transactional
    public Long saveMemo(Long dailyTagListId, String text) {
        DailyTagList dailyTagList = dailyTagListRepository.findById(dailyTagListId)
                .orElseThrow(DailyTagListNotFoundException::new);
        Memo memo = new Memo(text, dailyTagList);
        memoRepository.save(memo);
        return memo.getId();
    }

    @Transactional
    public String changeMemoText(Long memoId, String text) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(MemoNotFoundException::new);
        memo.changeText(text);
        return text;
    }

}
