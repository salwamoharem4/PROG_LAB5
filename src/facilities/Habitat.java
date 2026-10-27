package facilities;

public class Habitat extends Facility {
    public Habitat(String name, String location, int capacity, boolean operational) {
        super(name, location, capacity, operational);
    }
    @Override
    public String getFacility(){
        return "Habitat";
    }
}
