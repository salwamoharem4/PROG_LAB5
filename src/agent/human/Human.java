
package agent.human;

import agent.BaseAgent;
import core.Location;
import core.Skill;
import mission.Mission;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public abstract class Human extends BaseAgent {

    public static final int MAX_FATIGUE = 100;
    public static final int FATIGUE_LIMIT = 80;

    private HealthStatus healthStatus;
    private int fatigueLevel;
    private final Set<Skill> skills;

    protected Human(int id, String name, Location location,
                    HealthStatus healthStatus, Set<Skill> skills, int fatigueLevel) {

        super(id, name, location);

        if (healthStatus == null) {
            throw new IllegalArgumentException("Invalid health status");
        }

        if (skills == null || skills.contains(null)) {
            throw new IllegalArgumentException("Invalid skills");
        }

        checkFatigue(fatigueLevel);

        this.healthStatus = healthStatus;
        this.fatigueLevel = fatigueLevel;
        this.skills = skills.isEmpty()
                ? EnumSet.noneOf(Skill.class)
                : EnumSet.copyOf(skills);
    }

    private static void checkFatigue(int level) {
        if (level < 0 || level > MAX_FATIGUE) {
            throw new IllegalArgumentException("Invalid fatigue level");
        }
    }

    public HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public int getFatigueLevel() {
        return fatigueLevel;
    }

    public Set<Skill> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    public boolean hasSkill(Skill skill) {
        return skill != null && skills.contains(skill);
    }

    public void addSkill(Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("Invalid skill");
        }
        skills.add(skill);
    }

   // Increase fatigue level.
public void addFatigue(int amount) {
    if (amount < 0) {
        throw new IllegalArgumentException("Amount must be non-negative");
    }

    if (amount > MAX_FATIGUE - fatigueLevel) {
        throw new IllegalArgumentException("Fatigue cannot exceed 100");
    }

    fatigueLevel = fatigueLevel + amount;
}

// Decrease fatigue level.
public void rest(int amount) {
    if (amount < 0) {
        throw new IllegalArgumentException("Amount must be non-negative");
    }

    if (amount > fatigueLevel) {
        throw new IllegalArgumentException("Fatigue cannot go below 0");
    }

    fatigueLevel = fatigueLevel - amount;
}

    // Improve health after medical treatment.
    public void improveHealth() {
        healthStatus = healthStatus.improve();
    }

    // Make health worse if the human gets injured.
    public void worsenHealth() {
        healthStatus = healthStatus.worsen();
    }

    @Override
    public boolean isUsable() {
        return healthStatus == HealthStatus.HEALTHY
                && fatigueLevel < FATIGUE_LIMIT;
    }

    // The human must have all the skills required by the mission.
    @Override
    protected boolean meetsMissionConditions(Mission mission) {
        return skills.containsAll(mission.getRequiredSkills());
    }

    public boolean isHuman(){
        return true;

    }
}