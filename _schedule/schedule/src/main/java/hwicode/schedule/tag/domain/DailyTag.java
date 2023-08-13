package hwicode.schedule.tag.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DailyTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "daily_schedule_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DailyTagList dailyTagList;

    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    DailyTag(DailyTagList dailyTagList, Tag tag) {
        this.dailyTagList = dailyTagList;
        this.tag = tag;
    }

    boolean isSameTag(Tag tag) {
        String tagName = tag.getName();
        return this.tag.isSame(tagName);
    }

    public Long getId() {
        return id;
    }

}
