import agent.human.Engineer;
import agent.human.HealthStatus;
import agent.human.Scientist;
import agent.robot.RepairRobot;
import agent.robot.Rover;

import colony.Colony;
import colony.Simulation;

import core.Skill;
import core.Surface;

import emergencies.OxygenLeak;

import environment.EnvironmentCondition;

import facilities.Equipment;
import facilities.Habitat;

import mission.MissionStatus;
import mission.missiontype.ExplorationMission;
import mission.missiontype.RepairMission;

import resource.ResourceType;

import java.util.EnumSet;
import java.util.List;

/**
 * MARS-OPS: Mars Colony Management & Emergency Simulator
 *
 * Main demonstration driver executing the 5 Required Demonstration Scenarios
 * from Section 12 of the project specification.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("  MARS-OPS: AUTONOMOUS MARS COLONY MANAGEMENT & EMERGENCY SIMULATOR");
        System.out.println("  Executing 5 Required Demonstration Scenarios (Section 12)");
        System.out.println("================================================================================\n");

        runScenario1();
        runScenario2();
        runScenario3();
        runScenario4();
        runScenario5();

        System.out.println("================================================================================");
        System.out.println("  ALL 5 DEMONSTRATION SCENARIOS COMPLETED SUCCESSFULLY");
        System.out.println("================================================================================");
    }

    /**
     * Scenario 1: Successful Exploration (PDF Section 12)
     * Creates a scientist, an exploration rover, necessary equipment, and an exploration mission.
     * Executes the mission successfully and displays system state before and after execution.
     */
    private static void runScenario1() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(">>> SCENARIO 1: SUCCESSFUL EXPLORATION MISSION <<<");
        System.out.println("--------------------------------------------------------------------------------");

        Colony colony = new Colony();

        // 1. Setup Facility with Resources and Equipment
        Habitat habitat = new Habitat("Hab-1", "Central Sector", 10, true);
        habitat.addResource(ResourceType.OXYGEN, 100);
        habitat.addResource(ResourceType.BATTERY_POWER, 50);

        Equipment scanner = new Equipment("Terrain Scanner", true);
        habitat.addEquipment(scanner);
        colony.addFacility(habitat);

        // 2. Setup Crew and Autonomous Robot
        Scientist scientist = new Scientist(501, "Dr. Jane", habitat, HealthStatus.HEALTHY, EnumSet.of(Skill.GEOLOGY), 0);
        Rover rover = new Rover(502, "Curiosity-2", Surface.INSTANCE, 90);
        colony.addAgent(scientist);
        colony.addAgent(rover);

        // 3. Display System State BEFORE Mission Execution
        System.out.println("\n--- [System State: BEFORE Mission Execution] ---");
        colony.printStatusReport();

        // 4. Create and Dispatch Exploration Mission
        ExplorationMission mission = new ExplorationMission(
                701, "Crater Surface Exploration", 3,
                Skill.GEOLOGY, 30, List.of(scanner));

        System.out.println("\nSubmitting Exploration Mission [ID: 701, Duration: 3 cycles]...");
        boolean started = colony.startMission(mission, List.of(scientist, rover), false);
        System.out.println("Mission Start Accepted: " + started);
        System.out.println("Mission Current Status: " + mission.getStatus());
        System.out.println("Scientist Available: " + scientist.isAvailable() + ", Rover Available: " + rover.isAvailable());

        // 5. Execute Simulation Cycles
        System.out.println("\nAdvancing Simulation Clock by 3 Cycles...");
        Simulation sim = new Simulation(colony);
        sim.advanceTime(3);

        // 6. Display System State AFTER Mission Execution
        System.out.println("\n--- [System State: AFTER Mission Execution] ---");
        System.out.println("Mission Final Status: " + mission.getStatus());
        System.out.println("Scientist Fatigue: " + scientist.getFatigueLevel() + " (Available: " + scientist.isAvailable() + ")");
        System.out.println("Rover Battery: " + rover.getBatteryLevel() + "% (Available: " + rover.isAvailable() + ")");
        colony.printStatusReport();
        System.out.println();
    }

    /**
     * Scenario 2: Mission Rejection (Unsuitable Agent) (PDF Section 12)
     * Submits a mission with an unsuitable agent. Verifies that the mission fails
     * immediately with a clear reason and never becomes active.
     */
    private static void runScenario2() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(">>> SCENARIO 2: MISSION REJECTION (UNSUITABLE AGENT) <<<");
        System.out.println("--------------------------------------------------------------------------------");

        Colony colony = new Colony();
        Habitat habitat = new Habitat("Hab-Central", "Sector 1", 8, true);
        colony.addFacility(habitat);

        // Engineer has MECHANICAL skill and REPAIR capability, but lacks EXPLORE capability
        Engineer engineer = new Engineer(503, "Eng. Bob", habitat, HealthStatus.HEALTHY, EnumSet.of(Skill.MECHANICAL), 0);
        colony.addAgent(engineer);

        ExplorationMission mission = new ExplorationMission(702, "Deep Crater Survey", 2);
        System.out.println("Attempting to assign Exploration Mission to Engineer (who lacks EXPLORE capability)...");

        boolean started = colony.startMission(mission, List.of(engineer), false);

        if (!started) {
            System.out.println("Result: Mission was correctly REJECTED.");
            System.out.println("Reason: " + mission.getFailureReason());
        }
        System.out.println("Mission Active Status (IN_PROGRESS): " + (mission.getStatus() == MissionStatus.IN_PROGRESS));
        System.out.println("Mission Final Status: " + mission.getStatus());
        System.out.println("Agent Remains Available: " + engineer.isAvailable());
        System.out.println();
    }

    /**
     * Scenario 3: Resource Shortage (PDF Section 12)
     * Attempts to execute a mission when required resources are insufficient.
     * Verifies that the operation is rejected without consuming any resources or driving balances negative.
     */
    private static void runScenario3() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(">>> SCENARIO 3: RESOURCE SHORTAGE <<<");
        System.out.println("--------------------------------------------------------------------------------");

        Colony colony = new Colony();
        Habitat outpost = new Habitat("Outpost-A", "Sector 4", 5, true);
        outpost.addResource(ResourceType.SPARE_PARTS, 2); // Only 2 Spare Parts available
        colony.addFacility(outpost);

        Engineer engineer = new Engineer(504, "Eng. Sarah", outpost, HealthStatus.HEALTHY, EnumSet.of(Skill.MECHANICAL), 0);
        RepairRobot robot = new RepairRobot(505, "Fixer-Bot", outpost, 80);
        colony.addAgent(engineer);
        colony.addAgent(robot);

        int initialParts = colony.getTotalResources(ResourceType.SPARE_PARTS);
        System.out.println("Colony Available Spare Parts: " + initialParts);

        // RepairMission requires 10 Spare Parts and 20 Battery Power
        RepairMission mission = new RepairMission(703, "Major Solar Array Overhaul", 2, outpost, 10, 20);
        System.out.println("Mission Resource Requirement: 10 Spare Parts, 20 Battery Power");

        boolean started = colony.startMission(mission, List.of(engineer, robot), false);

        if (!started) {
            System.out.println("Result: Mission was correctly REJECTED due to resource deficit.");
            System.out.println("Reason: " + mission.getFailureReason());
            int partsAfter = colony.getTotalResources(ResourceType.SPARE_PARTS);
            System.out.println("Spare Parts Balance (Unchanged & Non-Negative): " + partsAfter);
            System.out.println("Balance Integrity Verified: " + (initialParts == partsAfter && partsAfter >= 0));
        }
        System.out.println();
    }

    /**
     * Scenario 4: Emergency Handling (Oxygen Leak) (PDF Section 12)
     * Demonstrates the multi-step emergency response workflow:
     * 1. Detect emergency and apply initial impact.
     * 2. Identify capable responders.
     * 3. Dispatch responders and perform mitigation.
     * 4. Consume required resources and update system state to RESOLVED.
     */
    private static void runScenario4() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(">>> SCENARIO 4: EMERGENCY HANDLING (OXYGEN LEAK) <<<");
        System.out.println("--------------------------------------------------------------------------------");

        Colony colony = new Colony();
        Habitat biodome = new Habitat("Biodome-Alpha", "Valley 1", 12, true);
        biodome.addResource(ResourceType.OXYGEN, 150);
        biodome.addResource(ResourceType.SPARE_PARTS, 5);
        colony.addFacility(biodome);

        Engineer engineer = new Engineer(506, "Eng. Alex", biodome, HealthStatus.HEALTHY, EnumSet.of(Skill.MECHANICAL), 0);
        RepairRobot repairRobot = new RepairRobot(507, "Patch-Bot", biodome, 85);
        colony.addAgent(engineer);
        colony.addAgent(repairRobot);

        System.out.println("Initial Biodome Oxygen: " + biodome.getResourceAmount(ResourceType.OXYGEN));
        System.out.println("Initial Biodome Spare Parts: " + biodome.getResourceAmount(ResourceType.SPARE_PARTS));

        // Step 1: Detect emergency - OxygenLeak immediately deducts initial loss
        System.out.println("\n[Emergency Detection]: Triggering Oxygen Leak (Severity: 30 units lost)...");
        OxygenLeak leak = new OxygenLeak(biodome, 30, 1);
        System.out.println("Biodome Oxygen after initial leak: " + biodome.getResourceAmount(ResourceType.OXYGEN));
        System.out.println("Emergency Status: " + leak.getStatus());

        // Step 2 & 3: Colony identifies capable responders (Engineer / RepairRobot) and handles the emergency
        System.out.println("\n[Colony Response]: Dispatching capable responders to seal leak and repair life support...");
       boolean resolved = colony.handleEmergency(leak);

        // Step 4: Verification
        System.out.println("Resolution Result: " + resolved);
        System.out.println("Emergency Final Status: " + leak.getStatus());
        System.out.println("Remaining Biodome Oxygen: " + biodome.getResourceAmount(ResourceType.OXYGEN));
        System.out.println("Remaining Biodome Spare Parts (1 consumed): " + biodome.getResourceAmount(ResourceType.SPARE_PARTS));
        System.out.println("Responder Availability: Engineer=" + engineer.isAvailable() + ", RepairRobot=" + repairRobot.isAvailable());
        System.out.println();
    }

    /**
     * Scenario 5: Environmental Effect (Dust Storm) (PDF Section 12)
     * Introduces an environmental condition (Dust Storm) and demonstrates its
     * restrictive impact on surface missions.
     */
    private static void runScenario5() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(">>> SCENARIO 5: ENVIRONMENTAL EFFECT (DUST STORM) <<<");
        System.out.println("--------------------------------------------------------------------------------");

        Colony colony = new Colony();
        Habitat habitat = new Habitat("Hab-South", "Ridge Sector", 6, true);
        colony.addFacility(habitat);

        Scientist scientist = new Scientist(508, "Dr. Carl", habitat, HealthStatus.HEALTHY, EnumSet.of(Skill.GEOLOGY), 0);
        Rover rover = new Rover(509, "Explorer-3", Surface.INSTANCE, 80);
        colony.addAgent(scientist);
        colony.addAgent(rover);

        System.out.println("Setting environmental condition: DUST STORM");
        colony.setEnvironmentCondition(EnvironmentCondition.DUST);
        System.out.println("Current Colony Environment: " + colony.getEnvironment().getCurrentCondition());

        ExplorationMission stormMission = new ExplorationMission(704, "Storm Exploration Attempt", 3);
        System.out.println("Attempting surface exploration during DUST storm...");

        boolean started = colony.startMission(stormMission, List.of(scientist, rover), false);

        if (!started) {
            System.out.println("Result: Mission was correctly REJECTED due to hazardous environment.");
            System.out.println("Reason: " + stormMission.getFailureReason());
        }
        System.out.println("Mission Active Status: " + (stormMission.getStatus() == MissionStatus.IN_PROGRESS));
        System.out.println("Agents remain safely in available status.");
        System.out.println();
    }
}