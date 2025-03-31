package edu.uob.GameEntity;

import edu.uob.Utils;

import java.util.HashMap;
import java.util.Map;

public class PlayerEntity extends GameEntity{

    private int health;
    private final Map<String, GameEntity> inventory = new HashMap<>();

    public PlayerEntity(String name, String locationName) {
        super(name, "", locationName);
        this.setPlayerDescription();
        this.health = 3;
    }

    private void setPlayerDescription() {
        StringBuilder buildDescription = new StringBuilder();
        buildDescription.append("a player called ")
                .append(Utils.removePlayerTag(this.name));
        this.description = buildDescription.toString();
    }

    @Override
    public void setLocationName(String newLocationName) {
        this.locationName = newLocationName;
        for (GameEntity invEntity : this.inventory.values()) {
            invEntity.setLocationName(newLocationName);
        }
    }

    //method modifies health and returns false if player has died
    public boolean modifyHealth(int changeInHealth) {
        if (changeInHealth >= 0) {
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

    public void addInventory(GameEntity invEntity) {
        this.inventory.put(invEntity.getName(), invEntity);
    }

    public void removeInventory(String invName) {
        this.inventory.remove(invName);
    }
}
