package edu.uob.GameEntity;

import java.util.HashMap;
import java.util.Map;

public class PlayerEntity extends GameEntity{

    private int health;
    private final Map<String, GameEntity> inventory = new HashMap<>();

    //player entities are not assigned a description: TODO lets make a random player description generator
    public PlayerEntity(String name, String locationName) {
        super(name, "I'm a player!", locationName);
        this.health = 3;
    }

    @Override
    public void setLocationName(String newLocationName) {
        this.locationName = newLocationName;
        for (GameEntity invEntity : inventory.values()) {
            invEntity.setLocationName(newLocationName);
        }
    }

    public void increaseHealth() {
        this.health++;
    }

    public void decreaseHealth() {
        this.health--;
    }

    public int getHealth() {
        return this.health;
    }

    public Map<String, GameEntity> getInventory() {
        return inventory;
    }

    public void addInventory(GameEntity invEntity) {
        this.inventory.put(invEntity.getName(), invEntity );
    }

    public void removeInventory(GameEntity invEntity) {
        this.inventory.remove(invEntity.getName());
    }
}
