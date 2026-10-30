
import agent.Agent;
import agent.BaseAgent;
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


import facilities.Habitat;
import resource.ResourceType;

import emergencies.EmergencyStatus;
import emergencies.Fire;
import emergencies.OxygenLeak;
import emergencies.RoverBreakdown;
import emergencies.Medical;

import mission.Mission;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/** Smoke test. Note there is no instanceof and no cast anywhere: everything goes through Agent. */
public class Main {

    static int failures = 0;

    static void check(String label, boolean actual, boolean expected) {
        boolean ok = actual == expected;

        if (!ok) {
            failures++;
        }

        System.out.println((ok ? "PASS " : "FAIL ") + label);
    }

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

    public static void main(String[] args) {
        // ================================================================
        // 1. SETUP CORE FACILITY & AGENTS
        // ================================================================
        System.out.println("--- 1. AGENT & MISSION TESTS ---");

        // Real Habitat that satisfies both Facility and Location
        Habitat habitatA = new Habitat("Habitat A", "Sector-1", 10, true);
        habitatA.addResource(ResourceType.OXYGEN, 500);
        habitatA.addResource(ResourceType.MEDICAL_SUPPLIES, 10);

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

        System.out.println("\\n===== 1. CAPABILITY TESTS =====");

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


        System.out.println("\\n===== 2. SKILL TESTS =====");

        Scientist noSkill = new Scientist(
                7, "Newbie", habitatA,
                HealthStatus.HEALTHY,
                EnumSet.noneOf(Skill.class), 0);

        check("Scientist with GEOLOGY can explore",
                sci.canPerform(explore), true);

        check("Scientist without GEOLOGY cannot explore",
                noSkill.canPerform(explore), false);


        System.out.println("\\n===== 3. BATTERY TESTS =====");

        check("Rover battery 80 is enough for mission requiring 30",
                rover.canPerform(explore), true);

        check("Drone battery 5 is not enough for mission requiring 30",
                drone.canPerform(aerialMission), false);


        System.out.println("\\n===== 4. TEAM CAPABILITY TESTS =====");

        check("Scientist and Rover team covers exploration",
                explore.isCoveredBy(List.of(sci, rover)), true);

        check("Engineer alone does not cover exploration",
                explore.isCoveredBy(List.of(eng)), false);

        check("Empty team does not cover exploration",
                explore.isCoveredBy(List.of()), false);


        System.out.println("\\n===== 5. AGENT AVAILABILITY TESTS =====");

        eng.assign();

        check("Assigned engineer is not available",
                eng.isAvailable(), false);

        check("Assigned engineer cannot handle emergency",
                eng.canHandle(EmergencyType.OXYGEN_LEAK), false);

        eng.release();

        check("Released engineer is available again",
                eng.isAvailable(), true);


        System.out.println("\\n===== 6. EMERGENCY TESTS =====");

        check("Engineer handles oxygen leak",
                eng.canHandle(EmergencyType.OXYGEN_LEAK), true);

        check("Repair robot handles oxygen leak",
                fixer.canHandle(EmergencyType.OXYGEN_LEAK), true);

        check("Medical officer handles medical emergency",
                med.canHandle(EmergencyType.MEDICAL_EMERGENCY), true);

        check("Rover cannot handle oxygen leak",
                rover.canHandle(EmergencyType.OXYGEN_LEAK), false);


        System.out.println("\\n===== 7. AGENT USABILITY TESTS =====");

        fixer.breakDown();

        check("Broken robot is not available",
                fixer.isAvailable(), false);

        fixer.repair();
        check("repaired robot available", fixer.isAvailable(), true);

        // Fatigue check (Start from 0 so 0 + 100 = 100 without throwing > 100 exception!)
        Scientist tiredSci = new Scientist(8, "Tired", habitatA, HealthStatus.HEALTHY,
                EnumSet.noneOf(Skill.class), 0);
        tiredSci.addFatigue(100);
        check("exhausted scientist not available", tiredSci.isAvailable(), false);

        check("Repaired robot is available",
                fixer.isAvailable(), true);

        sci.addFatigue(100);

        check("Exhausted scientist is not available",
                sci.isAvailable(), false);


        System.out.println("\\n===== 8. HEALTH TESTS =====");

        med.worsenHealth();
        med.worsenHealth();

        check("Critical medical officer is not usable",
                med.isUsable(), false);

        med.improveHealth();
        med.improveHealth();

        check("Treated medical officer is usable again",
                med.isUsable(), true);


        System.out.println("\\n===== 9. MOVEMENT AND BREAKDOWN TESTS =====");

        rover.moveTo(Surface.INSTANCE);
        rover.breakDown();
        check("broken rover stays on surface", rover.getLocation() == Surface.INSTANCE, true);
        rover.repair(); // Restore rover for emergency testing
        rover.moveTo(habitatA);

        // ================================================================
        // 2. EMERGENCY SYSTEM TESTS
        // ================================================================
        System.out.println("\n--- 2. EMERGENCY SYSTEM EXECUTION TESTS ---");

        List<BaseAgent> fullRoster = new ArrayList<>(List.of(eng, med, sci, fixer, rover, drone));

        // --- TEST A: OXYGEN LEAK (Double penalty failure when missing robot) ---
        Habitat leakHabitat = new Habitat("Leak-Habitat", "Sector-2", 10, true);
        leakHabitat.addResource(ResourceType.OXYGEN, 500);

        List<BaseAgent> onlyHumans = List.of(eng, med, sci);
        int oxygenBefore = leakHabitat.getResourceAmount(ResourceType.OXYGEN);

        OxygenLeak leakFail = new OxygenLeak(leakHabitat, 50); // deducts 50 initial
        check("leak fails when only humans available", leakFail.resolve(onlyHumans), false);
        check("unresolved leak has status UNRESOLVED", leakFail.getStatus() == EmergencyStatus.UNRESOLVED, true);
        check("unresolved leak took double penalty (100 total)",
                leakHabitat.getResourceAmount(ResourceType.OXYGEN) == (oxygenBefore - 100), true);

        // --- TEST B: OXYGEN LEAK (Successful resolution with Engineer + Fixer) ---
        OxygenLeak leakPass = new OxygenLeak(leakHabitat, 30);
        check("leak resolved with full team", leakPass.resolve(fullRoster), true);
        check("resolved leak has status RESOLVED", leakPass.getStatus() == EmergencyStatus.RESOLVED, true);

        // --- TEST C: ROVER BREAKDOWN ---
        RoverBreakdown breakdown = new RoverBreakdown(rover);
        check("breakdown resolved by engineer + fixer", breakdown.resolve(fullRoster), true);
        check("breakdown status is RESOLVED", breakdown.getStatus() == EmergencyStatus.RESOLVED, true);
        check("rover operational after repair", rover.isUsable(), true);

        check("Broken rover remains on the surface",
                rover.getLocation() == Surface.INSTANCE, true);


        System.out.println("\\n==============================");

        System.out.println(
                failures == 0
                        ? "ALL CHECKS PASSED"
                        : "FAILURES: " + failures
        );

        // --- TEST D: MEDICAL EMERGENCY ---
        // Patient Dr. Sara treated by Dr. Lina in habitatA
        Medical medicalIncident = new Medical(habitatA, sci);
        check("medical emergency resolved", medicalIncident.resolve(fullRoster), true);
        check("medical status is RESOLVED", medicalIncident.getStatus() == EmergencyStatus.RESOLVED, true);

        // ================================================================
        // RESULTS
        // ================================================================
        System.out.println(failures == 0 ? "\nALL CHECKS PASSED!" : "\nFAILURES: " + failures);
        System.exit(failures == 0 ? 0 : 1);
    }
}
