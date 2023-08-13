package hwicode.schedule.tag.domain;

import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagDuplicateException;
import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagNotFoundException;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Table(name = "daily_schedule")
@Entity
public class DailyTagList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "dailyTagList", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DailyTag> dailyTags = new ArrayList<>();

    public DailyTag addTag(Tag tag) {
        validateTag(tag);
        DailyTag dailyTag = new DailyTag(this, tag);
        dailyTags.add(dailyTag);
        return dailyTag;
    }

    private void validateTag(Tag tag) {
        boolean duplication = dailyTags.stream()
                .anyMatch(dailyTag -> dailyTag.isSameTag(tag));

        if (duplication) {
            throw new DailyTagDuplicateException();
        }
    }

    public void deleteTag(Tag tag) {
        DailyTag dailyTag = findDailyTagBy(tag);
        dailyTags.remove(dailyTag);
    }

    private DailyTag findDailyTagBy(Tag tag) {
        return dailyTags.stream()
                .filter(dailyTag -> dailyTag.isSameTag(tag))
                .findFirst()
                .orElseThrow(DailyTagNotFoundException::new);
    }

    public Long getId() {
        return id;
    }

}
