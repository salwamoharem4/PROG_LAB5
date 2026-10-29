
package agent.human;

import emergencies.EmergencyType;

import java.util.ArrayList;

public class Scientist extends Human {

    private String researchSpecialty;

    public Scientist(String id, String name, String currentLocation,
                     boolean notOnMission, HealthStatus healthStatus,
                     ArrayList<String> skills, int fatigueLevel,
                     String researchSpecialty) {

        super(id, name, currentLocation, notOnMission,
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
    return isAvailableForMission() && hasSkill(requiredSkill);
}

    @Override
    public boolean canHandle(EmergencyType type) {

        return false;
    }

}

