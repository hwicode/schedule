package hwicode.schedule.dailyschedule.shared_domain;

public enum Difficulty {
    EASY(1), NORMAL(2), HARD(3);

    private final int value;

    Difficulty(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
