package hwicode.schedule.dailyschedule.todolist.domain;

public enum Emoji {
    // 😁
    GOOD("\uD83D\uDE01"),
    // 🙂
    NOT_BAD("\uD83D\uDE42"),
    // 😟
    BAD("\uD83D\uDE1F");

    // utf-16
    private final String code;

    Emoji(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
