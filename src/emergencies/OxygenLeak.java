package emergencies;

import core.EmergencyType;
import facilities.Habitat;
import agent.BaseAgent;
import resource.ResourceType;

import java.util.List;

 // DON'T FORGET TO ADD RELEASE AFTER TIME DURATION
public class OxygenLeak extends emergency {

    private Habitat affectedHabitat;
    private  int OxygenLoss;

    public OxygenLeak(EmergencyType type , Habitat affectedHabitatHabitat , int OxygenLoss) {
        super(type,affectedHabitatHabitat);
        this.affectedHabitat=affectedHabitatHabitat;
        this.OxygenLoss=OxygenLoss;


    }

    private void applyOxygenLoss(int oxygenLossAmount){


        int current = affectedHabitat.getResourceAmount(ResourceType.OXYGEN);
        affectedHabitat.consumeResource(ResourceType.OXYGEN,oxygenLossAmount);

        System.out.println("Oxygen leak in [" + affectedHabitat.getName() + "]: lost " + (current - affectedHabitat.getResourceAmount(ResourceType.OXYGEN)) + " units. Remaining: " + affectedHabitat.getResourceAmount(ResourceType.OXYGEN));

    }



    @Override
    public boolean resolve(List<BaseAgent> candidateResponders) {
        if(!dispatchTeam(candidateResponders)){
            applyOxygenLoss(this.OxygenLoss);
            return false;
        }


        markResolved();
        System.out.println("Oxygen leak Repaired !!");
        return true;


    }
    public Habitat getAffectedHabitat(){
        return affectedHabitat;
    }
}
