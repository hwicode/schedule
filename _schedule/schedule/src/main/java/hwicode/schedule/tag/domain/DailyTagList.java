package hwicode.schedule.tag.domain;

import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagDuplicateException;
import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagListForbiddenException;
import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagNotFoundException;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Table(name = "daily_schedule")
@Entity
public class DailyTagList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate today;

    private String mainTagName;

    @Column(nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "dailyTagList", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DailyTag> dailyTags = new ArrayList<>();

    // 테스트 코드에서만 사용되는 생성자
    public DailyTagList(LocalDate today, Long userId) {
        this.today = today;
        this.userId = userId;
    }

    public void checkOwnership(Long userId) {
        if (!this.userId.equals(userId)) {
            throw new DailyTagListForbiddenException();
        }
    }

    public Memo createMemo(String text) {
        return new Memo(text, this, userId);
    }

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
        if (tag.getName().equals(mainTagName)) {
            mainTagName = null;
        }
    }

    public String changeMainTag(Tag tag) {
        findDailyTagBy(tag);
        this.mainTagName = tag.getName();
        return tag.getName();
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

    public String getMainTagName() {
        return mainTagName;
    }

}
