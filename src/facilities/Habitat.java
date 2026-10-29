package facilities;

public class Habitat extends Facility {
    private LifeSupportSystem lifeSupportSystem;
    public Habitat(String name, String location, int capacity, boolean operational,LifeSupportSystem lifeSupport) {
        super(name, location, capacity, operational);
        this.lifeSupportSystem = lifeSupport;
    }
    public LifeSupportSystem getLifeSupportSystem() {
        return lifeSupportSystem; //remove this line law feh compsition fe heta tanya
    }
    @Override
    public String getFacility(){
        return "Habitat";
    }
}
