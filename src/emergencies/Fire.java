package emergencies;

import core.EmergencyType;
import facilities.Habitat;
import agent.BaseAgent;
import java.util.List;

// DON'T FORGET TO ADD RELEASE AFTER TIME DURATION
// DON'T FORGET TO ADD RELEASE AFTER TIME DURATION
// DON'T FORGET TO ADD RELEASE AFTER TIME DURATION

public class Fire extends emergency {
    private Habitat affectedHabitat;


    public Fire(EmergencyType type, Habitat affectedHabitat) {
        super(type,affectedHabitat);
        this.affectedHabitat=affectedHabitat;
        this.affectedHabitat.setOperationalStatus(false);
        System.out.println( "FIRE OCCURED IN  : " + affectedHabitat.getName() +"!!");
    }
    @Override
    public boolean resolve(List<BaseAgent> candidateResponders) {
            if(!dispatchTeam(candidateResponders)){
                return false;
            }

            markResolved();
        System.out.println("FIRE HAS BEEN PUT OUT");

        return true;
    }

    public Habitat getAffectedHabitat() {
        return affectedHabitat;
    }
}