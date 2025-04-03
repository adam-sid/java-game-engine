package edu.uob.GameEntity;

public class GameEntity
{
    protected final String entityName;
    protected String description;
    protected String locationName;

    //super class that holds all entities in game
    public GameEntity(String entityName, String description, String locationName)
    {
        this.entityName = entityName;
        this.description = description;
        this.locationName = locationName;
    }

    public String getName()
    {
        return this.entityName;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public void setLocationName(String newLocationName) {
        this.locationName = newLocationName;
    }
}
