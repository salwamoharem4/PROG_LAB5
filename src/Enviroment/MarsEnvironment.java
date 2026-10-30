package environment;

import Enviroment.EnvironmentCondition;

public class MarsEnvironment {

    private EnvironmentCondition condition;

    public MarsEnvironment(EnvironmentCondition condition) {
        if (condition == null) {
            throw new IllegalArgumentException(
                    "Environment condition cannot be null");
        }

        this.condition = condition;
    }

    public EnvironmentCondition getCondition() {
        return condition;
    }

    public void setCondition(EnvironmentCondition condition) {
        if (condition == null) {
            throw new IllegalArgumentException(
                    "Environment condition cannot be null"
            );
        }
        this.condition = condition;
        System.out.println(
                "Mars environment condition became: " + condition
        );
    }

    public boolean canStartExploration() {
        return condition != EnvironmentCondition.DUST_STORM;
    }
}