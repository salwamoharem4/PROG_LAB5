package mission;

public enum MissionStatus {
    PENDING,        // Waiting for enough agents to be assigned
    ASSIGNED,       // Mission accepted by colony, waiting to start
    IN_PROGRESS,    // Currently executing and counting time
    COMPLETED,      // Reached required duration successfully
    FAILED,       // Rejected or failed due to conditions
    CANCELLED     //the mission was intentionally stopped by the colony or an authorized user.
}
