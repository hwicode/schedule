package hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubTaskCheckerPrePostService {

    private final List<SubTaskConstraintRemover> subTaskConstraintRemovers;


    public Long performBeforeDelete(Long subTaskId) {
        deleteForeignKeyConstraint(subTaskId);
        return subTaskId;
    }

    // 하나의 테이블에 여러 개의 엔티티가 매핑되어 있다. 다른 바운디드 컨텍스트에서 SubTask 테이블과의 매핑을 제거하는 메서드
    private void deleteForeignKeyConstraint(Long subTaskId) {
        subTaskConstraintRemovers.forEach(
                subTaskConstraintRemover -> subTaskConstraintRemover.delete(subTaskId)
        );
    }

}
