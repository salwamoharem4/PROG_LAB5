package agent.human;

import java.util.ArrayList;

public class Scientist extends Human {

    private String researchSpecialty;

    public Scientist(String id, String name, String currentLocation,
                     boolean available, String healthStatus,
                     ArrayList<String> skills, int fatigueLevel,
                     String researchSpecialty) {

        super(id, name, currentLocation, available,
              healthStatus, skills, fatigueLevel);

        if (researchSpecialty == null || researchSpecialty.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid research specialty");
        }

        this.researchSpecialty = researchSpecialty;
    }

    public String getResearchSpecialty() {
        return researchSpecialty;
    }

    public void setResearchSpecialty(String researchSpecialty) {
        if (researchSpecialty == null || researchSpecialty.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid research specialty");
        }

        this.researchSpecialty = researchSpecialty;
    }

    public boolean canPerformResearch(String requiredSkill) {
        if (!isAvailable()) {
            return false;
        }

        if (!"Healthy".equalsIgnoreCase(getHealthStatus())) {
            return false;
        }

        if (getFatigueLevel() >= 80) {
            return false;
        }

        if (!hasSkill(requiredSkill)) {
            return false;
        }

        return true;
    }
}