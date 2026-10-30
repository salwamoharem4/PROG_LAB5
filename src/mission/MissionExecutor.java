package mission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MissionExecutor {

    private final List<BaseMission> missions = new ArrayList<>();

    // Register a mission for execution.
    public void addMission(BaseMission mission) {
        if (mission == null) {
            throw new IllegalArgumentException("Mission cannot be null");
        }
        if (missions.contains(mission)) {
            throw new IllegalArgumentException("Mission is already registered");
        }
        missions.add(mission);
    }

    // Return a read-only view of registered missions.
    public List<BaseMission> getMissions() {
        return Collections.unmodifiableList(missions);
    }

    // Advance every mission that is currently in progress.
    public void advanceTime(int units) {
        if (units <= 0) {
            throw new IllegalArgumentException("Time units must be positive");
        }

        for (BaseMission mission : missions) {
            if (mission.getStatus() == MissionStatus.IN_PROGRESS) {
                mission.advanceTime(units);
            }
        }
    }

    // Return all missions that have completed.
    public List<BaseMission> getCompletedMissions() {
        List<BaseMission> completed = new ArrayList<>();

        for (BaseMission mission : missions) {
            if (mission.getStatus() == MissionStatus.COMPLETED) {
                completed.add(mission);
            }
        }
        return completed;
    }

    // Return all failed missions.
    public List<BaseMission> getFailedMissions() {
        List<BaseMission> failed = new ArrayList<>();

        for (BaseMission mission : missions) {
            if (mission.getStatus() == MissionStatus.FAILED) {
                failed.add(mission);
            }
        }

        return failed;
    }
}