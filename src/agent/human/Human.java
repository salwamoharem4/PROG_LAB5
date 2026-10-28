
package agent.human;

import agent.Agent;
import java.util.ArrayList;

public abstract class Human extends Agent {

    private HealthStatus healthStatus;
    private int fatigueLevel;
    private ArrayList<String> skills;

    private static final int MAX_FATIGUE_LEVEL = 80;

    public Human(String id, String name, String currentLocation,
                 boolean notOnMission, HealthStatus healthStatus,
                 ArrayList<String> skills, int fatigueLevel) {

        super(id, name, currentLocation, notOnMission);

        if (healthStatus == null) {
            throw new IllegalArgumentException("Invalid health status");
        }

        if (fatigueLevel < 0 || fatigueLevel > 100) {
            throw new IllegalArgumentException("Invalid fatigue level");
        }

        if (skills == null) {
            throw new IllegalArgumentException("Skills cannot be null");
        }

        this.healthStatus = healthStatus;
        this.fatigueLevel = fatigueLevel;
        this.skills = new ArrayList<>(skills);

        for (String skill : this.skills) {
            if (skill == null || skill.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid skill");
            }
        }
    }

    public HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(HealthStatus healthStatus) {
        if (healthStatus == null) {
            throw new IllegalArgumentException("Invalid health status");
        }

        this.healthStatus = healthStatus;
    }

    public int getFatigueLevel() {
        return fatigueLevel;
    }

    public void setFatigueLevel(int fatigueLevel) {
        if (fatigueLevel < 0 || fatigueLevel > 100) {
            throw new IllegalArgumentException("Invalid fatigue level");
        }

        this.fatigueLevel = fatigueLevel;
    }

    public ArrayList<String> getSkills() {
        return new ArrayList<>(skills);
    }

    public void addSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid skill");
        }

        if (!skills.contains(skill)) {
            skills.add(skill);
        }
    }

    public void removeSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid skill");
        }

        skills.remove(skill);
    }

    public boolean hasSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid skill");
        }

        return skills.contains(skill);
    }

    @Override
public boolean isInUsableCondition() {
    return healthStatus == HealthStatus.HEALTHY
            && fatigueLevel < MAX_FATIGUE_LEVEL;//MAX_FATIGUE_LEVEL is design choice 
}
}
