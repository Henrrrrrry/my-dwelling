package helper_classes_and_methods;
class LocationState {
    private Dwelling.Location location;
    private String state;

    public LocationState(Dwelling.Location location, String state) {
        this.location = location;
        this.state = state;
    }

    public Dwelling.Location getLocation() {
        return location;
    }

    public String getState() {
        return state;
    }
}