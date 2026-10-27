package facilities;

public class Laboratory extends Facility {
    public Laboratory(String name, String location, int capacity, boolean operational) {
        super(name, location, capacity, operational);
    }
    @Override
    public String getFacility(){
        return "Laboratory";
    }
}
