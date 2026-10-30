package emergencies;
import core.EmergencyType;

import agent.Agent;

import java.util.ArrayList;
import java.util.List;

public abstract class emergency {

    private final EmergencyType type;
    private EmergencyStatus status = EmergencyStatus.PENDING;
    private String unresolvedReason = "";

    private final List <Agent> dispatchedResponders = new ArrayList<>();



    public emergency(EmergencyType type) {
        this.type = type;
    }

    public EmergencyType getType() {
        return type;
    }

    public EmergencyStatus getStatus() {
        return status;
    }

    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }

    public String getUnresolvedReason() {
        return unresolvedReason;
    }

    public void setUnresolvedReason(String unresolvedReason) {
        this.unresolvedReason = unresolvedReason;
    }
    public void dispatchResponder(Agent agent){
        this.dispatchedResponders.add(agent);
        agent.setAvailability(false);

    }

    public void releaseResponders(){

        for (Agent agent : dispatchedResponders){
            agent.setAvailability(true);
        }

        dispatchedResponders.clear();
    }
    public void markResolved(){
        this.status=EmergencyStatus.RESOLVED;
        releaseResponders();
    }

    public void markUnresolved(String Reason){
        this.status=EmergencyStatus.UNRESOLVED;
        this.unresolvedReason = Reason;
        releaseResponders();

    }

    public abstract boolean resolve(List<Agent> candidateResponders);
}