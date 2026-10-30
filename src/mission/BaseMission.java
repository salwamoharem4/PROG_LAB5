package mission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import agent.Agent;
import agent.BaseAgent;
import core.Capability;
import core.Location;
import core.Skill;
import facilities.Equipment;
import mission.missiontype.MissionType;
import resource.ResourceType;

public abstract class BaseMission implements Mission {
   private final int id;
    private final String name;
    private final MissionType type;
    private MissionStatus status;
    private final int duration;
    private int elapsedTime;
    private String failureReason;
    private final Set<Capability> requiredCapabilities;
    private final Set<Skill> requiredSkills;
    private final int requiredBattery;
   private final Map<ResourceType, Integer> requiredResources;
    private final Location targetLocation;
    private final List<Equipment> requiredEquipment;
    private final List<Agent> assignedAgents;
    public BaseMission(int id, String name, MissionType type, MissionStatus status, int duration, int elapsedTime,
            String failureReason, Set<Capability> requiredCapabilities, Set<Skill> requiredSkills, int requiredBattery,
            Map<ResourceType, Integer> requiredResources, Location targetLocation, List<Equipment> requiredEquipment,
            List<Agent> assignedAgents) {
        if (id<0){
            throw new IllegalArgumentException("Invalid ID");
        }      
        if (name==null||name.trim().isEmpty()){
            throw new IllegalArgumentException("Invalid Name");
        }
        if (requiredCapabilities == null || requiredCapabilities.contains(null)
                || requiredSkills == null || requiredSkills.contains(null)) {
            throw new IllegalArgumentException("Invalid requirements");
        }
        if (requiredResources == null || requiredEquipment == null
                || requiredEquipment.contains(null)) {
            throw new IllegalArgumentException("Invalid resources or equipment");
        }
        for (Integer amount : requiredResources.values()) {
            if (amount == null || amount < 0) {
                throw new IllegalArgumentException("Invalid resource amount");
            }
        } 
        if (requiredBattery < 0 || requiredBattery > 100) {
            throw new IllegalArgumentException("Invalid battery requirement");
        }
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = MissionStatus.PENDING;
        this.duration = duration;
        this.elapsedTime = 0;
        this.failureReason = null;
        this.requiredCapabilities = requiredCapabilities.isEmpty()? EnumSet.noneOf(Capability.class): EnumSet.copyOf(requiredCapabilities);
        this.requiredSkills = requiredSkills.isEmpty() ? EnumSet.noneOf(Skill.class) : EnumSet.copyOf(requiredSkills);
        this.requiredBattery = requiredBattery;
        this.requiredResources = requiredResources;
        this.targetLocation = targetLocation;
        this.requiredEquipment = requiredEquipment;
        this.assignedAgents = new ArrayList<>();
    }
    // Get mission ID.
    public int getId() {
        return id;
    }

    // Get mission name.
    public String getName() {
        return name;
    }

    public MissionType getType() {
        return type;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public int getDuration() {
        return duration;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public String getFailureReason() {
        return failureReason;
    }

    @Override
    public Set<Capability> getRequiredCapabilities() {
        return Collections.unmodifiableSet(requiredCapabilities);
    }

    @Override
    public Set<Skill> getRequiredSkills() {
        return Collections.unmodifiableSet(requiredSkills);
    }

    @Override
    public int getRequiredBattery() {
        return requiredBattery;
    }

    public Map<ResourceType, Integer> getRequiredResources() {
        return Collections.unmodifiableMap(requiredResources);
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public List<Equipment> getRequiredEquipment() {
        return Collections.unmodifiableList(requiredEquipment);
    }

    public List<Agent> getAssignedAgents() {
        return Collections.unmodifiableList(assignedAgents);
    }//this method chexks wether the agent canperform or not assigned to mission and then assign them 
    public void assignAgent(Agent agent){
        if(agent==null||!agent.canPerform(this)){
             throw new IllegalArgumentException("Agent cannot perform this mission");
        }
        if(assignedAgents.contains(agent)){
            throw new IllegalArgumentException("Agent is already assigned");
        }
        agent.assign();
        assignedAgents.add(agent);

        if (isCoveredBy(assignedAgents)) {
            status = MissionStatus.ASSIGNED;//checks whether the assigned agents collectively have all the capabilities required by the mission.
            //if false the mission status stays PENDING.
        }
    }
    // Remove an agent from the mission.
    public void removeAgent(Agent agent) {
        if (agent == null || !assignedAgents.contains(agent)) {
            throw new IllegalArgumentException("Agent is not assigned");
        }

        assignedAgents.remove(agent);
        agent.release();//Calling release() makes the agent available again

        if (!isCoveredBy(assignedAgents)) {
            status = MissionStatus.PENDING;
        }
    }
    // Start the mission.
    public void start() {
        if (status != MissionStatus.ASSIGNED || assignedAgents.isEmpty()
                || !isCoveredBy(assignedAgents)) {
            throw new IllegalStateException("Mission is not ready to start");
        }

        status = MissionStatus.IN_PROGRESS;
    }
    // Advance mission time.
    public void advanceTime(int units) {
        if (status != MissionStatus.IN_PROGRESS) {
            throw new IllegalStateException("Mission is not in progress");
        }

        if (units <= 0) {
            throw new IllegalArgumentException("Invalid time units");
        }

        elapsedTime = Math.min(duration, elapsedTime + units);

        if (elapsedTime >= duration) {
            complete();
        }
    }
    // Mark the mission as completed.
    public void complete() {
        if (status != MissionStatus.IN_PROGRESS) {
            throw new IllegalStateException("Mission is not in progress");
        }

        status = MissionStatus.COMPLETED;
        releaseAllAgents();
    }
     // Release all assigned agents.
    private void releaseAllAgents() {
        for (Agent agent : assignedAgents) {
            agent.release();
        }

        assignedAgents.clear();
    }
     // Mark the mission as failed.
    public void fail(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Failure reason is required");
        }

        if (status == MissionStatus.COMPLETED
                || status == MissionStatus.FAILED
                || status == MissionStatus.CANCELLED) {
            throw new IllegalStateException("Mission has already ended");
        }

        failureReason = reason;
        status = MissionStatus.FAILED;
        releaseAllAgents();
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + id + ", " + name
                + ", " + status + "]";
    }
}
