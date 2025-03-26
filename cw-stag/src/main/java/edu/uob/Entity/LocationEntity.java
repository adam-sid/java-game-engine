package edu.uob.Entity;

import java.util.HashSet;

public class LocationEntity extends Entity {

    private HashSet<LocationEntity> paths;

    public LocationEntity(String name, String description, String locationName) {
        super(name, description, locationName);
        this.paths = new HashSet<>();
    }

    public HashSet<LocationEntity> getPaths() {
        return this.paths;
    }

    public void addPath(LocationEntity location) {
        this.paths.add(location);
    }
}
