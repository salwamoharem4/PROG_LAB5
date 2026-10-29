package emergencies;
import facilities.Facility;

public class emergency {
    private emergencyStatus status = emergencyStatus.PENDING;
    private emergencyType type;
    private String unresolvedReason="";

    public emergency(emergencyStatus status, emergencyType type, String unresolvedReason) {
        this.status = status;
        this.type = type;
        this.unresolvedReason = unresolvedReason;
    }


    public emergencyStatus getStatus() {
        return status;
    }

    public void setStatus(emergencyStatus status) {
        this.status = status;
    }

    public emergencyType getType() {
        return type;
    }

    public void setType(emergencyType type) {
        this.type = type;
    }

    public String getUnresolvedReason() {
        return unresolvedReason;
    }

    public void setUnresolvedReason(String unresolvedReason) {
        this.unresolvedReason = unresolvedReason;
    }
}
