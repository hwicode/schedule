package hwicode.schedule.tag.domain;

public class Tag {

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

}
