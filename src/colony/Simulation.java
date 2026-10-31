
package colony;

import agent.Agent;
import mission.BaseMission;
import mission.MissionStatus;

import java.util.ArrayList;
import java.util.List;

// Handles simulation time, mission progression, and agent states.
public class Simulation {

    private final Colony colony;
    private int currentCycle;

    public Simulation(Colony colony) {
        if (colony == null) {
            throw new IllegalArgumentException("Colony cannot be null");
        }

        this.colony = colony;
        this.currentCycle = 0;
    }

    public Colony getColony() {
        return colony;
    }

    public int getCurrentCycle() {
        return currentCycle;
    }

    // Advances simulation time by the specified number of time units.
    public void advanceTime(int units) {
        if (units <= 0) {
            throw new IllegalArgumentException("Time units must be positive");
        }

        currentCycle += units;

        System.out.println("\n>>> ADVANCING SIMULATION TIME BY " + units + " UNITS (Current Cycle: " + currentCycle + ") <<<");

        // Get all missions that are currently in progress.
        List<BaseMission> inProgressMissions = new ArrayList<>();

        for (BaseMission m : colony.getMissions()) {
            if (m.getStatus() == MissionStatus.IN_PROGRESS) {
                inProgressMissions.add(m);
            }
        }

        // Process each mission and its assigned agents.
        for (BaseMission mission : inProgressMissions) {

            // Create a copy to avoid problems if the original list changes.
            List<Agent> teamCopy = new ArrayList<>(mission.getAssignedAgents());

            for (Agent agent : teamCopy) {

                // Update the agent's fatigue or battery.
                agent.advanceTime(units);

                // Check if the agent is still usable.
                if (!agent.isUsable()) {
                    mission.fail("Mission failed: Agent " + agent.getName() + " became unusable during operation.");
                    break;
                }
            }
        }

        // Advance mission progress in the colony.
        colony.advanceTime(units);
    }
}
