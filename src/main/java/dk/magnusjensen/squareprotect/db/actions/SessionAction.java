package dk.magnusjensen.squareprotect.db.actions;

public enum SessionAction {
    JOIN("joined"),
    LEAVE("left");

    private final String name;
    SessionAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
