package edu.uob;

import edu.uob.Entity.Entity;
import edu.uob.Entity.LocationEntity;
import java.util.HashSet;

public class GameState {
    //TODO would using a List to store these be simpler?
    public final HashSet<LocationEntity> locations;
    public final HashSet<Entity> furniture;
    public HashSet<Entity> artefacts;
    public HashSet<Entity> characters;

    public GameState(HashSet<LocationEntity> locations, HashSet<Entity> furniture,
                     HashSet<Entity> artefacts, HashSet<Entity> characters ) {
        this.locations = locations;
        this.furniture = furniture;
        this.artefacts = artefacts;
        this.characters = characters;
    }

    public HashSet<Entity> getEntitiesFromLocation(String entityType, String locationName) {
        HashSet<Entity> filteredEntities = new HashSet<>();
        switch (entityType) {
            case "furniture":
                for (Entity entity : furniture) {
                    if (entity.getLocationName().equals(locationName)) {
                        filteredEntities.add(entity);
                    }
                }
                break;
            case "character":
                for (Entity entity : characters) {
                    if (entity.getLocationName().equals(locationName)) {
                        filteredEntities.add(entity);
                    }
                }
                break;
            case "artefact":
                for (Entity entity : artefacts) {
                    if (entity.getLocationName().equals(locationName)) {
                        filteredEntities.add(entity);
                    }
                }
                break;
            case "location":
                for (Entity entity : locations) {
                    if (entity.getLocationName().equals(locationName)) {
                        filteredEntities.add(entity);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return filteredEntities;
    }

    public void moveEntity(Entity entity, String locationName, String entityType) {
        switch (entityType) {
            case "furniture":
                if (furniture.contains(entity)) {
                    furniture.remove(entity);
                    entity.setLocationName(locationName);
                    furniture.add(entity);
                }
                break;
            case "character":
                if (characters.contains(entity)) {
                    characters.remove(entity);
                    entity.setLocationName(locationName);
                    characters.add(entity);
                }
                break;
            case "artefact":
                if (artefacts.contains(entity)) {
                    artefacts.remove(entity);
                    entity.setLocationName(locationName);
                    artefacts.add(entity);
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
