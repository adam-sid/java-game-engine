package edu.uob.GameEntity;

import java.util.HashMap;
// TODO this was previously abstract - changed so I could directly instantiate game entities
public class GameEntity
{
    private String name;
    private String description;
    private String locationName;

    public GameEntity(String name, String description, String locationName)
    {
        this.name = name;
        this.description = description;
        this.locationName = locationName;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String newLocationName) {
        this.locationName = newLocationName;
    }
}
