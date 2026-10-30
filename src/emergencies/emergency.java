package emergencies;
import core.EmergencyType;

import agent.BaseAgent;
import core.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class emergency {

    private Location Emergencylocation;
    private final EmergencyType type;
    private EmergencyStatus status = EmergencyStatus.PENDING;
    private String unresolvedReason = "";

    private final List <BaseAgent> dispatchedResponders = new ArrayList<>();



    public emergency(EmergencyType type , Location EmergencyLocation) {
        this.type = type;
        this.Emergencylocation=EmergencyLocation;
    }

    public EmergencyType getType() {
        return type;
    }

    public Location getEmergencylocation() {
        return Emergencylocation;
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

    public void dispatchResponder(BaseAgent agent){


        this.dispatchedResponders.add(agent);
        agent.moveTo(getEmergencylocation());
        agent.setAvailability(false);

    }
    public List<BaseAgent> getDispatchedResponders() {
        return Collections.unmodifiableList(dispatchedResponders);
    }

    public void releaseResponders(){

        for (BaseAgent agent : dispatchedResponders){
            agent.setAvailability(true);
        }

    }
    public void markResolved(){
        setStatus(EmergencyStatus.RESOLVED);
        releaseResponders();
    }

    public void markUnresolved(String Reason){
        setStatus(EmergencyStatus.UNRESOLVED);
        setUnresolvedReason(Reason);
        releaseResponders();

    }


    protected boolean dispatchTeam(List<BaseAgent> candidateResponders){  // searches all agents for an appropriate team
    BaseAgent human = null;
    BaseAgent robot = null;

    for(BaseAgent agent : candidateResponders){
        if(agent.isAvailable() && agent.canHandle(getType())){
            if (agent.isHuman() && human==null){
                human=agent;
            }else if( agent.isRobot() && robot== null){
                robot = agent;
            }

        }
        if( human!=null && robot != null){
            break;
        }


    }

        if (human == null || robot == null) {

            if (human == null && robot == null) {

                markUnresolved("No available human or robotic responders with capability: " + getType().getRequiredCapability());

            } else if (human == null) {

                markUnresolved("No available human responder with capability: " + getType().getRequiredCapability());

            } else {

                markUnresolved("No available robotic responder with capability: " + getType().getRequiredCapability());
            }

            return false;

        }else {

        dispatchResponder(human);
        dispatchResponder(robot);
        return true;


    }

    }
    public abstract boolean resolve(List<BaseAgent> candidateResponders);
}