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

    public Tag(String name) {
        this.name = name;
    }

    public boolean changeName(String name) {
        if (this.name.equals(name)) {
            return false;
        }
        this.name = name;
        return true;
    }

    boolean isSame(Tag tag) {
        return this.name.equals(tag.name);
    }

}
