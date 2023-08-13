package hwicode.schedule.tag.application;

import hwicode.schedule.tag.domain.Memo;
import hwicode.schedule.tag.exception.application.MemoNotFoundException;
import hwicode.schedule.tag.infra.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional
    public String changeMemoText(Long memoId, String text) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(MemoNotFoundException::new);
        memo.changeText(text);
        return text;
    }

}
