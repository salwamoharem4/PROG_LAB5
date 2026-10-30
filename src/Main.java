
import agent.Agent;
import agent.human.Engineer;
import agent.human.HealthStatus;
import agent.human.MedicalOfficer;
import agent.human.Scientist;
import agent.robot.Drone;
import agent.robot.RepairRobot;
import agent.robot.Rover;

import core.Capability;
import core.EmergencyType;
import core.Location;
import core.Skill;
import core.Surface;

import facilities.Equipment;
import facilities.Habitat;

import mission.BaseMission;
import mission.Mission;
import mission.MissionStatus;
import mission.missiontype.MissionType;

import colony.Colony;
import resource.ResourceType;

import java.util.EnumSet;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Smoke test. Note there is no instanceof and no cast anywhere: everything goes through Agent. */
public class Main {

        static int failures = 0;

        static void check(String label, boolean actual, boolean expected) {
                boolean ok = actual == expected;

                if (!ok) {
                        failures++;
                }

                System.out.println((ok ? "PASS " : "FAIL ") + label
                        + " | Expected: " + expected + ", Actual: " + actual);
        }

        // Original helper for testing agent capabilities.
        static Mission mission(
                Set<Capability> caps,
                Set<Skill> skills,
                int battery) {

                return new Mission() {
                        public Set<Capability> getRequiredCapabilities() {
                                return caps;
                        }

                        public Set<Skill> getRequiredSkills() {
                                return skills;
                        }

                        public int getRequiredBattery() {
                                return battery;
                        }
                };
        }

        // Creates a concrete mission for Colony and BaseMission tests.
        static BaseMission createMission(
                int id,
                String name,
                MissionType type,
                int duration,
                Set<Capability> caps,
                Set<Skill> skills,
                int battery,
                Map<ResourceType, Integer> resources,
                List<Equipment> equipment) {

                // BaseMission uses EnumMap internally, so pass an EnumMap
                // even when the mission does not require resources.
                EnumMap<ResourceType, Integer> resourceCopy =
                        new EnumMap<>(ResourceType.class);

                resourceCopy.putAll(resources);

                return new BaseMission(
                        id,
                        name,
                        type,
                        MissionStatus.PENDING,
                        duration,
                        0,
                        null,
                        caps,
                        skills,
                        battery,
                        resourceCopy,
                        () -> "Habitat A",
                        new java.util.ArrayList<>(equipment),
                        new java.util.ArrayList<>()
                ) {
                };
        }

        static BaseMission simpleMission(
                int id,
                String name,
                Set<Capability> caps,
                int duration) {

                return createMission(
                        id,
                        name,
                        MissionType.EXPLORATION,
                        duration,
                        caps,
                        EnumSet.noneOf(Skill.class),
                        0,
                        new EnumMap<>(ResourceType.class),
                        List.of()
                );
        }

        public static void main(String[] args) {

                Location habitatA = () -> "Habitat A";

                // Create agents
                Scientist sci = new Scientist(
                        1, "Dr. Sara", habitatA,
                        HealthStatus.HEALTHY,
                        EnumSet.of(Skill.GEOLOGY), 10);

                Engineer eng = new Engineer(
                        2, "Omar", habitatA,
                        HealthStatus.HEALTHY,
                        EnumSet.of(Skill.MECHANICAL), 20);

                MedicalOfficer med = new MedicalOfficer(
                        3, "Dr. Lina", habitatA,
                        HealthStatus.HEALTHY,
                        EnumSet.of(Skill.FIRST_AID), 0);

                Rover rover = new Rover(
                        4, "Rover-1", habitatA,
                        80);

                RepairRobot fixer = new RepairRobot(
                        5, "Fixer-1", habitatA,
                        60);

                Drone drone = new Drone(
                        6, "Drone-1", habitatA,
                        5);

                // Create missions
                Mission explore = mission(
                        EnumSet.of(Capability.EXPLORE),
                        EnumSet.of(Skill.GEOLOGY),
                        30);

                Mission repair = mission(
                        EnumSet.of(Capability.REPAIR),
                        EnumSet.noneOf(Skill.class),
                        10);

                Mission aerialMission = mission(
                        EnumSet.of(Capability.AERIAL_RECON),
                        EnumSet.noneOf(Skill.class),
                        30);

                System.out.println("\n===== 1. CAPABILITY TESTS =====");

                check("Scientist can explore",
                        sci.canPerform(explore), true);

                check("Rover can explore",
                        rover.canPerform(explore), true);

                check("Engineer cannot explore",
                        eng.canPerform(explore), false);

                check("Drone cannot explore",
                        drone.canPerform(explore), false);

                check("Scientist cannot repair",
                        sci.canPerform(repair), false);


                System.out.println("\n===== 2. SKILL TESTS =====");

                Scientist noSkill = new Scientist(
                        7, "Newbie", habitatA,
                        HealthStatus.HEALTHY,
                        EnumSet.noneOf(Skill.class), 0);

                check("Scientist with GEOLOGY can explore",
                        sci.canPerform(explore), true);

                check("Scientist without GEOLOGY cannot explore",
                        noSkill.canPerform(explore), false);


                System.out.println("\n===== 3. BATTERY TESTS =====");

                check("Rover battery 80 is enough for mission requiring 30",
                        rover.canPerform(explore), true);

                check("Drone battery 5 is not enough for mission requiring 30",
                        drone.canPerform(aerialMission), false);


                System.out.println("\n===== 4. TEAM CAPABILITY TESTS =====");

                check("Scientist and Rover team covers exploration",
                        explore.isCoveredBy(List.of(sci, rover)), true);

                check("Engineer alone does not cover exploration",
                        explore.isCoveredBy(List.of(eng)), false);

                check("Empty team does not cover exploration",
                        explore.isCoveredBy(List.of()), false);


                System.out.println("\n===== 5. AGENT AVAILABILITY TESTS =====");

                eng.assign();

                check("Assigned engineer is not available",
                        eng.isAvailable(), false);

                check("Assigned engineer cannot handle emergency",
                        eng.canHandle(EmergencyType.OXYGEN_LEAK), false);

                eng.release();

                check("Released engineer is available again",
                        eng.isAvailable(), true);


                System.out.println("\n===== 6. EMERGENCY TESTS =====");

                check("Engineer handles oxygen leak",
                        eng.canHandle(EmergencyType.OXYGEN_LEAK), true);

                check("Repair robot handles oxygen leak",
                        fixer.canHandle(EmergencyType.OXYGEN_LEAK), true);

                check("Medical officer handles medical emergency",
                        med.canHandle(EmergencyType.MEDICAL_EMERGENCY), true);

                check("Rover cannot handle oxygen leak",
                        rover.canHandle(EmergencyType.OXYGEN_LEAK), false);


                System.out.println("\n===== 7. AGENT USABILITY TESTS =====");

                fixer.breakDown();

                check("Broken robot is not available",
                        fixer.isAvailable(), false);

                fixer.repair();

                check("Repaired robot is available",
                        fixer.isAvailable(), true);

                // Sara starts with fatigue 10, so add 90 to reach 100.
                sci.addFatigue(90);

                check("Exhausted scientist is not available",
                        sci.isAvailable(), false);


                System.out.println("\n===== 8. HEALTH TESTS =====");

                med.worsenHealth();
                med.worsenHealth();

                check("Critical medical officer is not usable",
                        med.isUsable(), false);

                med.improveHealth();
                med.improveHealth();

                check("Treated medical officer is usable again",
                        med.isUsable(), true);


                System.out.println("\n===== 9. MOVEMENT AND BREAKDOWN TESTS =====");

                rover.moveTo(Surface.INSTANCE);
                rover.breakDown();

                check("Broken rover remains on the surface",
                        rover.getLocation() == Surface.INSTANCE, true);


                // =====================================================
                // SECTION 6: MISSION MANAGEMENT TESTS
                // =====================================================

                System.out.println("\n===== 10. SECTION 6: MISSION ADMISSION =====");

                // Use fresh agents because Sara and Rover were modified above.
                Scientist scientist1 = new Scientist(
                        10, "Scientist-1", habitatA,
                        HealthStatus.HEALTHY,
                        EnumSet.of(Skill.GEOLOGY), 0);

                Engineer engineer1 = new Engineer(
                        11, "Engineer-1", habitatA,
                        HealthStatus.HEALTHY,
                        EnumSet.of(Skill.MECHANICAL), 0);

                MedicalOfficer doctor1 = new MedicalOfficer(
                        12, "Doctor-1", habitatA,
                        HealthStatus.HEALTHY,
                        EnumSet.of(Skill.FIRST_AID), 0);

                Colony colony = new Colony();

                colony.addAgent(scientist1);
                colony.addAgent(engineer1);
                colony.addAgent(doctor1);

                BaseMission normalMission = simpleMission(
                        100,
                        "Exploration Mission",
                        EnumSet.of(Capability.EXPLORE),
                        5);

                check("Suitable mission is accepted",
                        colony.startMission(
                                normalMission,
                                List.of(scientist1),
                                false),
                        true);

                check("Accepted mission starts IN_PROGRESS",
                        normalMission.getStatus() == MissionStatus.IN_PROGRESS,
                        true);

                check("Assigned scientist is unavailable",
                        scientist1.isAvailable(), false);


                System.out.println("\n===== 11. ONE ACTIVE NORMAL MISSION =====");

                BaseMission secondNormalMission = simpleMission(
                        101,
                        "Second Exploration",
                        EnumSet.of(Capability.REPAIR),
                        3);

                check("Second normal mission is rejected while first is active",
                        colony.startMission(
                                secondNormalMission,
                                List.of(engineer1),
                                false),
                        false);

                check("Rejected mission becomes FAILED",
                        secondNormalMission.getStatus() == MissionStatus.FAILED,
                        true);

                check("Rejected mission has a failure reason",
                        secondNormalMission.getFailureReason() != null
                                && !secondNormalMission.getFailureReason().isBlank(),
                        true);


                System.out.println("\n===== 12. EMERGENCY MISSION EXCEPTION =====");

                BaseMission emergencyMission = simpleMission(
                        102,
                        "Medical Emergency",
                        EnumSet.of(Capability.MEDICAL),
                        2);

                check("Emergency mission can start while normal mission is active",
                        colony.startMission(
                                emergencyMission,
                                List.of(doctor1),
                                true),
                        true);

                check("Normal mission remains IN_PROGRESS",
                        normalMission.getStatus() == MissionStatus.IN_PROGRESS,
                        true);

                check("Emergency mission starts IN_PROGRESS",
                        emergencyMission.getStatus() == MissionStatus.IN_PROGRESS,
                        true);


                System.out.println("\n===== 13. UNSUITABLE AGENT =====");

                BaseMission unsuitableMission = simpleMission(
                        103,
                        "Repair Mission",
                        EnumSet.of(Capability.REPAIR),
                        2);

                // Scientist does not have REPAIR capability.
                // Use a separate colony because the first normal mission is active.
                Colony unsuitableColony = new Colony();
                unsuitableColony.addAgent(scientist1);

                check("Unsuitable agent is rejected",
                        unsuitableColony.startMission(
                                unsuitableMission,
                                List.of(scientist1),
                                false),
                        false);

                check("Unsuitable mission becomes FAILED",
                        unsuitableMission.getStatus() == MissionStatus.FAILED,
                        true);


                System.out.println("\n===== 14. AGENT NOT BELONGING TO COLONY =====");

                Colony ownershipColony = new Colony();

                BaseMission ownershipMission = simpleMission(
                        104,
                        "Ownership Test",
                        EnumSet.of(Capability.EXPLORE),
                        2);

                check("Agent outside colony is rejected",
                        ownershipColony.startMission(
                                ownershipMission,
                                List.of(scientist1),
                                false),
                        false);


                System.out.println("\n===== 15. DUPLICATE AGENT TEST =====");

                Colony duplicateColony = new Colony();
                duplicateColony.addAgent(engineer1);

                BaseMission duplicateMission = simpleMission(
                        105,
                        "Duplicate Agent Test",
                        EnumSet.of(Capability.REPAIR),
                        2);

                check("Same agent cannot be selected twice",
                        duplicateColony.startMission(
                                duplicateMission,
                                List.of(engineer1, engineer1),
                                false),
                        false);


                System.out.println("\n===== 16. UNAVAILABLE AGENT TEST =====");

                Colony unavailableColony = new Colony();
                unavailableColony.addAgent(engineer1);

                engineer1.assign();

                BaseMission unavailableMission = simpleMission(
                        106,
                        "Unavailable Agent Test",
                        EnumSet.of(Capability.REPAIR),
                        2);

                check("Unavailable agent is rejected",
                        unavailableColony.startMission(
                                unavailableMission,
                                List.of(engineer1),
                                false),
                        false);

                engineer1.release();


                // =====================================================
                // SECTION 6: RESOURCES AND EQUIPMENT
                // =====================================================

                System.out.println("\n===== 17. SECTION 6: RESOURCE TESTS =====");

                Habitat resourceHabitat = new Habitat(
                        "Resource Habitat", "Area A", 10, true);

                resourceHabitat.addResource(ResourceType.OXYGEN, 10);

                Colony resourceColony = new Colony();
                resourceColony.addFacility(resourceHabitat);
                resourceColony.addAgent(engineer1);

                EnumMap<ResourceType, Integer> requiredResources =
                        new EnumMap<>(ResourceType.class);

                requiredResources.put(ResourceType.OXYGEN, 5);

                BaseMission resourceMission = createMission(
                        107,
                        "Resource Mission",
                        MissionType.REPAIR,
                        2,
                        EnumSet.of(Capability.REPAIR),
                        EnumSet.noneOf(Skill.class),
                        0,
                        requiredResources,
                        List.of());

                check("Mission with sufficient resources is accepted",
                        resourceColony.startMission(
                                resourceMission,
                                List.of(engineer1),
                                false),
                        true);

                check("Required oxygen is consumed",
                        resourceHabitat.getResourceAmount(ResourceType.OXYGEN) == 5,
                        true);


                System.out.println("\n===== 18. INSUFFICIENT RESOURCES =====");

                Habitat poorHabitat = new Habitat(
                        "Poor Habitat", "Area B", 10, true);

                poorHabitat.addResource(ResourceType.OXYGEN, 2);

                Colony poorColony = new Colony();
                poorColony.addFacility(poorHabitat);
                poorColony.addAgent(engineer1);

                BaseMission expensiveMission = createMission(
                        108,
                        "Expensive Mission",
                        MissionType.REPAIR,
                        2,
                        EnumSet.of(Capability.REPAIR),
                        EnumSet.noneOf(Skill.class),
                        0,
                        requiredResources,
                        List.of());

                check("Mission without sufficient resources is rejected",
                        poorColony.startMission(
                                expensiveMission,
                                List.of(engineer1),
                                false),
                        false);

                check("Insufficient resources are not consumed",
                        poorHabitat.getResourceAmount(ResourceType.OXYGEN) == 2,
                        true);

                check("Insufficient-resource mission becomes FAILED",
                        expensiveMission.getStatus() == MissionStatus.FAILED,
                        true);


                System.out.println("\n===== 19. EQUIPMENT TESTS =====");

                Equipment drill = new Equipment("Drill", true);


                Habitat equipmentHabitat = new Habitat(
                        "Equipment Habitat", "Area C", 10, true);

                Engineer equipmentEngineer = new Engineer(
                        32, "Equipment Engineer", equipmentHabitat,
                        HealthStatus.HEALTHY,
                        EnumSet.noneOf(Skill.class), 0);

                equipmentHabitat.addEquipment(drill);

                Colony equipmentColony = new Colony();
                equipmentColony.addFacility(equipmentHabitat);
                equipmentColony.addAgent(equipmentEngineer);

                BaseMission equipmentMission = createMission(
                        109,
                        "Equipment Mission",
                        MissionType.REPAIR,
                        2,
                        EnumSet.of(Capability.REPAIR),
                        EnumSet.noneOf(Skill.class),
                        0,
                        new EnumMap<>(ResourceType.class),
                        List.of(new Equipment("Drill", true)));

                check("Mission with available equipment is accepted",
                        equipmentColony.startMission(
                                equipmentMission,
                                List.of(equipmentEngineer),
                                false),
                        true);

                // Test missing equipment in a separate colony.
                Colony missingEquipmentColony = new Colony();
                Engineer missingEquipmentEngineer = new Engineer(
                        33, "Missing Equipment Engineer", equipmentHabitat,
                        HealthStatus.HEALTHY,
                        EnumSet.noneOf(Skill.class), 0);

                missingEquipmentColony.addAgent(missingEquipmentEngineer);

                BaseMission missingEquipmentMission = createMission(
                        110,
                        "Missing Equipment Mission",
                        MissionType.REPAIR,
                        2,
                        EnumSet.of(Capability.REPAIR),
                        EnumSet.noneOf(Skill.class),
                        0,
                        new EnumMap<>(ResourceType.class),
                        List.of(new Equipment("Missing Drill", true)));

                check("Mission with missing equipment is rejected",
                        missingEquipmentColony.startMission(
                                missingEquipmentMission,
                                List.of(missingEquipmentEngineer),
                                false),
                        false);


                // =====================================================
                // SECTION 6: MULTIPLE AGENTS
                // =====================================================

                System.out.println("\n===== 20. SECTION 6: MULTIPLE AGENTS =====");

                Colony teamColony = new Colony();

                Scientist teamScientist = new Scientist(
                        20, "Team Scientist", habitatA,
                        HealthStatus.HEALTHY,
                        EnumSet.noneOf(Skill.class), 0);

                Engineer teamEngineer = new Engineer(
                        21, "Team Engineer", habitatA,
                        HealthStatus.HEALTHY,
                        EnumSet.noneOf(Skill.class), 0);

                teamColony.addAgent(teamScientist);
                teamColony.addAgent(teamEngineer);

                BaseMission teamMission = simpleMission(
                        111,
                        "Combined Team Mission",
                        EnumSet.of(Capability.RESEARCH, Capability.REPAIR),
                        2);

                check("Two agents collectively cover mission capabilities",
                        teamColony.startMission(
                                teamMission,
                                List.of(teamScientist, teamEngineer),
                                false),
                        true);

                check("Both agents become unavailable during mission",
                        !teamScientist.isAvailable()
                                && !teamEngineer.isAvailable(),
                        true);


                // =====================================================
                // SECTION 7: MISSION EXECUTION
                // =====================================================

                System.out.println("\n===== 21. SECTION 7: MISSION TIME =====");

                Colony timeColony = new Colony();

                Scientist timeScientist = new Scientist(
                        30, "Time Scientist", habitatA,
                        HealthStatus.HEALTHY,
                        EnumSet.noneOf(Skill.class), 0);

                timeColony.addAgent(timeScientist);

                BaseMission timedMission = simpleMission(
                        112,
                        "Timed Mission",
                        EnumSet.of(Capability.EXPLORE),
                        3);

                check("Timed mission starts successfully",
                        timeColony.startMission(
                                timedMission,
                                List.of(timeScientist),
                                false),
                        true);

                check("Mission initially has zero elapsed time",
                        timedMission.getElapsedTime() == 0,
                        true);

                timeColony.advanceTime(1);

                check("Mission advances by one time unit",
                        timedMission.getElapsedTime() == 1,
                        true);

                check("Mission is still IN_PROGRESS before duration",
                        timedMission.getStatus() == MissionStatus.IN_PROGRESS,
                        true);

                timeColony.advanceTime(2);

                check("Mission completes after reaching its duration",
                        timedMission.getStatus() == MissionStatus.COMPLETED,
                        true);

                check("Agent is released after mission completion",
                        timeScientist.isAvailable(),
                        true);

                check("Completed mission appears in completed missions",
                        timeColony.getCompletedMissions().contains(timedMission),
                        true);


                System.out.println("\n===== 22. SECTION 7: MISSION FAILURE =====");

                Colony failureColony = new Colony();

                Engineer failureEngineer = new Engineer(
                        31, "Failure Engineer", habitatA,
                        HealthStatus.HEALTHY,
                        EnumSet.noneOf(Skill.class), 0);

                failureColony.addAgent(failureEngineer);

                BaseMission failureMission = simpleMission(
                        113,
                        "Mission That Fails",
                        EnumSet.of(Capability.REPAIR),
                        5);

                check("Mission starts before failure",
                        failureColony.startMission(
                                failureMission,
                                List.of(failureEngineer),
                                false),
                        true);

                failureMission.fail("Equipment failed during execution.");

                check("Mission status becomes FAILED",
                        failureMission.getStatus() == MissionStatus.FAILED,
                        true);

                check("Failure reason is recorded",
                        failureMission.getFailureReason() != null
                                && !failureMission.getFailureReason().isBlank(),
                        true);

                check("Agent is released after mission failure",
                        failureEngineer.isAvailable(),
                        true);


                System.out.println("\n===== 23. SECTION 7: FAILED MISSION CANNOT RETRY =====");

                check("Failed mission cannot be started again",
                        failureColony.startMission(
                                failureMission,
                                List.of(failureEngineer),
                                false),
                        false);


                System.out.println("\n===== 24. SECTION 7: INVALID TIME =====");

                boolean invalidTimeRejected = false;

                try {
                        failureColony.advanceTime(0);
                } catch (IllegalArgumentException ex) {
                        invalidTimeRejected = true;
                }

                check("Zero time advancement is rejected",
                        invalidTimeRejected,
                        true);


                // =====================================================
                // MISSION TRACKING TEST
                // =====================================================

                System.out.println("\n===== 25. MISSION TRACKING =====");

                check("Accepted mission is registered",
                        timeColony.getMissions().contains(timedMission),
                        true);

                check("Completed mission is tracked",
                        timeColony.getCompletedMissions().contains(timedMission),
                        true);

                check("Rejected mission is tracked as failed",
                        unsuitableColony.getFailedMissions().contains(unsuitableMission),
                        true);


                System.out.println("\n==============================");

                System.out.println(
                        failures == 0
                                ? "ALL CHECKS PASSED"
                                : "FAILURES: " + failures);

                System.exit(failures == 0 ? 0 : 1);
        }
}
