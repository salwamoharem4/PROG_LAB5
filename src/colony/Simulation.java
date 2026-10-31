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

    // Advances simulation time one unit at a time.
    public void advanceTime(int units) {
        if (units <= 0) {
            throw new IllegalArgumentException("Time units must be positive");
        }
        System.out.println(
                "\n>>> ADVANCING SIMULATION TIME BY "
                        + units + " UNITS <<<"
        );

        // Process each time unit separately.
        for (int i = 0; i < units; i++) {
            currentCycle++;
            System.out.println(
                    "\n--- Simulation Cycle: " + currentCycle + " ---"
            );
            // Get missions that are currently in progress.
            List<BaseMission> inProgressMissions = new ArrayList<>();

            for (BaseMission mission : colony.getMissions()) {

                if (mission.getStatus() == MissionStatus.IN_PROGRESS) {
                    inProgressMissions.add(mission);
                }
            }

            // Update the agents assigned to each active mission.
            for (BaseMission mission : inProgressMissions) {

                // Copy the list because fail() may release all agents.
                List<Agent> teamCopy = new ArrayList<>(mission.getAssignedAgents());
                for (Agent agent : teamCopy) {

                    // Advance this agent by ONE time unit.
                    agent.advanceTime(1);

                    // Check whether the agent can continue working.
                    if (!agent.isUsable()) { //3alshan law el sbb battery yb'a specific
                        String reason;
                        if (agent.isRobot() && agent.getBatteryLevel() == 0) {
                            reason = "Mission failed: Robot "
                                    + agent.getName()
                                    + " ran out of battery.";
                        } else {
                            reason = "Mission failed: Agent "
                                    + agent.getName()
                                    + " became unusable during operation.";
                        }
                        mission.fail(reason);
                        System.out.println(reason);
                        break;
                    }
                }
            }

            // Advance all missions by ONE time unit.
            // Failed missions should not progress.
            colony.advanceTime(1);

            System.out.println(
                    "Cycle " + currentCycle + " completed."
            );
        }

        System.out.println(
                "\n>>> SIMULATION TIME ADVANCED SUCCESSFULLY <<<"
        );
    }
}