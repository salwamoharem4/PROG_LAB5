package environment;

import agent.Agent;
import core.Capability;
import mission.BaseMission;
import mission.MissionStatus;
import mission.missiontype.MissionType;

public class Environment {

    private EnvironmentCondition currentCondition;

    public Environment() {
        this(EnvironmentCondition.NORMAL);
    }

    public Environment(EnvironmentCondition initialCondition) {
        if (initialCondition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        this.currentCondition = initialCondition;
    }

    public EnvironmentCondition getCurrentCondition() {
        return currentCondition;
    }

    public void setCondition(EnvironmentCondition condition) {
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        this.currentCondition = condition;
        System.out.println("ENVIRONMENT CHANGED TO: " + condition);
    }

    // One rule set for both starting and running missions. Returns null if allowed.
    private String getBlockReason(BaseMission mission) {
        EnvironmentCondition c = currentCondition;

        if (c == EnvironmentCondition.DUST
                && mission.getType() == MissionType.EXPLORATION) {
            return c.getBlockReason();
        }

        if (c == EnvironmentCondition.COMMUNICATION_DISRUPTION
                && (mission.getType() == MissionType.RESCUE
                    || mission.getRequiredCapabilities().contains(Capability.AERIAL_RECON))) {
            return c.getBlockReason();
        }

        return null;
    }

    // Null if the mission may start, otherwise the failure reason.
    public String canStartMission(BaseMission mission) {
        if (mission == null) {
            return "Mission cannot be null.";
        }
        return getBlockReason(mission);
    }

    // Fails the mission if it is running and the new condition blocks it.
    // Returns true if the mission was failed.
    public boolean applyConditionToActiveMission(BaseMission mission) {
        if (mission == null || mission.getStatus() != MissionStatus.IN_PROGRESS) {
            return false;
        }
        String reason = getBlockReason(mission);
        if (reason == null) {
            return false;
        }
        mission.fail("Mission failed during execution: " + reason);
        return true;
    }

    // Drones can't be controlled during a communication disruption.
    public boolean canOperateAgent(Agent agent) {
        if (agent == null) {
            return false;
        }
        return !(currentCondition == EnvironmentCondition.COMMUNICATION_DISRUPTION
                && agent.getCapabilities().contains(Capability.AERIAL_RECON));
    }
}