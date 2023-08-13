package hwicode.schedule.tag.domain;

public class DailyTag {

    private DailyTagList dailyTagList;
    private Tag tag;

    DailyTag(DailyTagList dailyTagList, Tag tag) {
        this.dailyTagList = dailyTagList;
        this.tag = tag;
    }

    boolean isSameTag(Tag tag) {
        return this.tag.isSame(tag);
    }

}
