package edu.uob.GameEntity;

import edu.uob.Utils;

import java.util.HashMap;
import java.util.Map;

public class PlayerEntity extends GameEntity{

    private int health;
    private final Map<String, GameEntity> inventory = new HashMap<>();

    //class that extends GameEntity to include a modified description, setLocationName and adds
    //health and inventory (unique to players)
    public PlayerEntity(String name, String locationName) {
        super(name, "", locationName);
        this.setPlayerDescription();
        this.health = 3;
    }

    //adds a description to players that includes their name
    private void setPlayerDescription() {
        StringBuilder buildDescription = new StringBuilder();
        buildDescription.append("a player called ")
                .append(Utils.removePlayerTag(this.entityName));
        this.description = buildDescription.toString();
    }

    //overrides the super class to ensure that all inventory also change location
    @Override
    public void setLocationName(String newLocationName) {
        this.locationName = newLocationName;
        for (GameEntity entity : this.inventory.values()) {
            entity.setLocationName(newLocationName);
        }
    }

    //method modifies health and returns false if player has died
    public boolean modifyHealth(int changeInHealth) {
        if (changeInHealth > 0) {
            this.health = Math.min(3, this.health + changeInHealth);
            return true;
        } else {
            this.health = Math.max(0, this.health + changeInHealth);
            return this.health > 0;
        }
    }

    public int getHealth() {
        return this.health;
    }

    public Map<String, GameEntity> getInventory() {
        return inventory;
    }

    public void addInventory(GameEntity entity) {
        this.inventory.put(entity.getName(), entity);
    }

    public void removeInventory(String entityName) {
        this.inventory.remove(entityName);
    }
}
