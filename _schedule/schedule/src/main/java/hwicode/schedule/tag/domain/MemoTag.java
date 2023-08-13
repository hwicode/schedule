package hwicode.schedule.tag.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
class MemoTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "memo_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Memo memo;

    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    MemoTag(Memo memo, Tag tag) {
        this.memo = memo;
        this.tag = tag;
    }

    boolean isSameTag(Tag tag) {
        return this.tag.isSame(tag);
    }

}
