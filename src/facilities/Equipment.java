package facilities;

public class Equipment {
    private String name;
    private boolean operational;

    public String getName() {
        return name;
    }
    public Equipment(String name, boolean operational) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid equipment name");
        }

        this.name = name.trim();
        this.operational = operational;
    }

    public boolean isOperational() {
        return operational;
    }

    public void setOperational(boolean operational) {
        this.operational = operational;
    }
}
