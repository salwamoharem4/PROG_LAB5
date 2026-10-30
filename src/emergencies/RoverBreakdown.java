package emergencies;

import core.EmergencyType;
import core.Location;
import facilities.Habitat;
import agent.BaseAgent;
import agent.robot.Rover;
import mission.BaseMission;

import java.util.ArrayList;
import java.util.List;


// DON'T FORGET TO ADD RELEASE AFTER TIME DURATION


public class RoverBreakdown extends emergency {
private Rover damagedRover;
private int reqParts=5;

    public RoverBreakdown(Rover damagedRover, BaseMission ongoingMission) {
        super(EmergencyType.ROVER_BREAKDOWN , damagedRover.getLocation());
        this.damagedRover=damagedRover;
        this.damagedRover.breakDown();
        if(ongoingMission!= null){
            ongoingMission.fail("Rover [" + damagedRover.getName() +" ] broke down at ["+ damagedRover.getLocation()+"] during operation " );
        }

        System.out.println(this.damagedRover.getName()+"  ROVER BROKE DOWN AT  "+ this.damagedRover.getLocation());



    }
    public RoverBreakdown(Rover damagedRover) {   // if it broke down not during a mission
        this(damagedRover, null);
    }


    @Override
    public boolean resolve(List<BaseAgent> candidateResponders) {
        List<BaseAgent> candidates = new ArrayList<>(candidateResponders);
        candidates.remove(this.damagedRover);

        if(!dispatchTeam(candidateResponders)){
            return false;
        }
        this.damagedRover.repair();
        markResolved();
        System.out.println("ROVER"+ this.damagedRover.getName()+"SUCCESSFULLY REPAIRED !!");

        return true;
    }
    public int getRequiredSpareParts() {
        return reqParts;
    }
    public Rover getDamagedRover() {
        return damagedRover;
    }
}
