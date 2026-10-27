package facilities;

public class StorageFacility extends Facility {
    public StorageFacility(String name, String location, int capacity, boolean operational) {
        super(name, location, capacity, operational);
    }
    @Override
    public String getFacility(){
        return "StorageFacility";
    }
}
