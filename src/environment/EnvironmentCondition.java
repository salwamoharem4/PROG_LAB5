package environment;

public enum EnvironmentCondition {
    NORMAL(null),
    DUST("Surface operations are not allowed during a dust storm."),
    COMMUNICATION_DISRUPTION("Communication-dependent operations are not allowed during a communication disruption.");
    private String blockReason;
    EnvironmentCondition(String blockReason){
        this.blockReason=blockReason;
    }
    public String getBlockReason() {
        return blockReason;
    }
    
}
