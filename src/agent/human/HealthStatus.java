package agent.human;

public enum HealthStatus {
    CRITICAL,
    INJURED,
    HEALTHY;

    public HealthStatus improve() {
        if (this == CRITICAL) {
            return INJURED;
        }

        return HEALTHY;
    }

    public HealthStatus worsen() {
        if (this == HEALTHY) {
            return INJURED;
        }

        return CRITICAL;
    }
}