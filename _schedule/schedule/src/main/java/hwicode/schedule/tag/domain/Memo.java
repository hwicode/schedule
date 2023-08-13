package hwicode.schedule.tag.domain;

import hwicode.schedule.tag.exception.domain.memo.MemoTagDuplicateException;
import hwicode.schedule.tag.exception.domain.memo.MemoTagNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @JoinColumn(name = "daily_schedule_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DailyTagList dailyTagList;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MemoTag> memoTags = new ArrayList<>();

    public Memo(String text, DailyTagList dailyTagList) {
        this.text = text;
        this.dailyTagList = dailyTagList;
    }

    public boolean changeText(String text) {
        if (this.text.equals(text)) {
            return false;
        }
        this.text = text;
        return true;
    }

    public List<MemoTag> addTags(List<Tag> tags) {
        return tags.stream()
                .map(this::addTag)
                .collect(Collectors.toList());
    }

    public MemoTag addTag(Tag tag) {
        validateTag(tag);
        MemoTag memoTag = new MemoTag(this, tag);
        memoTags.add(memoTag);
        return memoTag;
    }

    private void validateTag(Tag tag) {
        boolean duplication = memoTags.stream().anyMatch(m -> m.isSameTag(tag));

        if (duplication) {
            throw new MemoTagDuplicateException();
        }
    }

    public void deleteTag(Tag tag) {
        MemoTag memoTag = findMemoTagBy(tag);
        memoTags.remove(memoTag);
    }

    private MemoTag findMemoTagBy(Tag tag) {
        return memoTags.stream()
                .filter(memoTag -> memoTag.isSameTag(tag))
                .findFirst()
                .orElseThrow(MemoTagNotFoundException::new);
    }

    public Long getId() {
        return id;
    }

}
