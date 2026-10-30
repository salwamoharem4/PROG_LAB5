package emergencies;

import agent.Agent;

import java.util.List;

public class Fire extends emergency {

    public Fire() {
        super(EmergencyType.FIRE); // Done! Parent handles the rest
    }

    @Override
    public boolean resolve(List<Agent> candidateResponders) {

        for (Agent agent : candidateResponders) {
            if (agent.canHandle(getType())) {

            }
        }
        return false;

    }
}