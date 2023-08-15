package dk.magnusjensen.squareprotect.db.actions;

public enum BlockAction {
    BREAK("removed"),
    PLACE("placed"),
    INTERACT("clicked");

    private final String name;
    BlockAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
