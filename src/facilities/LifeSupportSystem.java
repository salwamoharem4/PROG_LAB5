
package facilities;

public class LifeSupportSystem {
    private boolean operational;

    public LifeSupportSystem() {
        this.operational = true;
    }

    public boolean isOperational() {
        return operational;
    }

    public void setOperational(boolean operational) {
        this.operational = operational;
    }

    public boolean provideOxygen() {
        return operational;
    }

    public boolean filterAir() {
        return operational;
    }
}
