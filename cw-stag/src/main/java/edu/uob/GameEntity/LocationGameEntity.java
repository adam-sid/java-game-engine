package edu.uob.GameEntity;

import java.util.HashMap;

public class LocationGameEntity extends GameEntity {

    private final HashMap<String, LocationGameEntity> paths;

    public LocationGameEntity(String name, String description, String locationName) {
        super(name, description, locationName);
        this.paths = new HashMap<>();
    }

    public HashMap<String, LocationGameEntity> getPaths() {
        return this.paths;
    }

    public void addPath(LocationGameEntity location) {
        this.paths.put(location.getName(), location);
    }
}
