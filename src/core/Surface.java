package core;

/** The Martian surface, as a single shared Location. */
public enum Surface implements Location {
    INSTANCE;

    @Override
    public String getName() {
        return "Martian Surface";
    }

    @Override
    public String toString() {
        return getName();
    }
}
