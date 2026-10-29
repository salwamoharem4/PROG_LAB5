package emergencies;

public class emergency {
    private EmergencyStatus status = EmergencyStatus.PENDING;
    private EmergencyType type;
    private String unresolvedReason="";

    public emergency(EmergencyStatus status, EmergencyType type, String unresolvedReason) {
        this.status = status;
        this.type = type;
        this.unresolvedReason = unresolvedReason;
    }


    public EmergencyStatus getStatus() {
        return status;
    }

    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }

    public EmergencyType getType() {
        return type;
    }

    public void setType(EmergencyType type) {
        this.type = type;
    }

    public String getUnresolvedReason() {
        return unresolvedReason;
    }

    public void setUnresolvedReason(String unresolvedReason) {
        this.unresolvedReason = unresolvedReason;
    }
}
