package hwicode.schedule.tag.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long userId;

    public Tag(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }

    public boolean changeName(String name) {
        if (this.name.equals(name)) {
            return false;
        }
        this.name = name;
        return true;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

}
