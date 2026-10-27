package facilities;

public class Equipment {
    private String name;

    public Equipment(String name, boolean operational) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public boolean setName(String name) {
        if (name == null|| name.isEmpty()) return false;
        this.name = name;
        return true;
    }

}
