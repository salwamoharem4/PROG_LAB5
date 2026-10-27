package agent.human;
import java.util.ArrayList;

import agent.Agent;

public class Human extends Agent{
    private String healthStatus;
    private ArrayList<String> skills;
    private int fatigueLevel;
    public Human(String id, String name, String currentLocation, boolean available, boolean damaged,
            String healthStatus, ArrayList<String> skills, int fatigueLevel) {
                if (healthStatus == null || healthStatus.trim().isEmpty()) {
                 throw new IllegalArgumentException("Invalid health status");}
                 if (skills == null || skills.isEmpty()) {
                  throw new IllegalArgumentException("Invalid skills");}
                if (fatigueLevel < 0 || fatigueLevel > 100) {
                  throw new IllegalArgumentException("Invalid fatigue level");}  
        super(id, name, currentLocation, available, damaged);
        this.healthStatus = healthStatus;
        this.skills = skills;
        this.fatigueLevel = fatigueLevel;
    }
    public String getHealthStatus() {
        return healthStatus;
    }
    public ArrayList<String> getSkills() {
        return skills;
    }
    public int getFatigueLevel() {
        return fatigueLevel;
    }
    public void setHealthStatus(String healthStatus) {
         if (healthStatus == null || healthStatus.trim().isEmpty()) {
                 throw new IllegalArgumentException("Invalid health status");}
        this.healthStatus = healthStatus;
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
        return false;
    }

    return skills.contains(skill);
}
    public void setFatigueLevel(int fatigueLevel) {
        if (fatigueLevel < 0 || fatigueLevel > 100) {
                  throw new IllegalArgumentException("Invalid fatigue level");}  
        this.fatigueLevel = fatigueLevel;
    }
    
}
