package edu.uob.Entity;

// TODO this was previously abstract - changed so I could directly instantiate game entities
public class Entity
{
    private final String name;
    private final String description;
    private String locationName;

    public Entity(String name, String description, String locationName)
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
