package edu.uob.GameEntity;

import java.util.HashMap;

public class LocationEntity extends GameEntity {

    private final HashMap<String, LocationEntity> paths;

    //locationName for locations will just be their name!
    public LocationEntity(String name, String description) {
        super(name, description, name);
        this.paths = new HashMap<>();
    }

    public HashMap<String, LocationEntity> getPaths() {
        return this.paths;
    }

    public void addPath(LocationEntity location) {
        this.paths.put(location.getName(), location);
    }

    public void removePath(String locationName) {
        this.paths.remove(locationName);
    }

}
