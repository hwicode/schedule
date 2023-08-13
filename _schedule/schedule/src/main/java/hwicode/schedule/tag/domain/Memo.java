package hwicode.schedule.tag.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Memo {

    private String text;
    private DailyTagList dailyTagList;
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
            throw new RuntimeException();
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
                .orElseThrow(RuntimeException::new);
    }

}
