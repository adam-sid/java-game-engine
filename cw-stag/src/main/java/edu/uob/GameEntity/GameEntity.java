package edu.uob.GameEntity;

public class GameEntity
{
    protected final String name;
    protected final String description;
    protected String locationName;

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
