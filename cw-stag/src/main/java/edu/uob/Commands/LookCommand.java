package edu.uob.Commands;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.LocationEntity;
import edu.uob.GameEntity.PlayerEntity;
import edu.uob.GameState;

import java.io.File;
import java.util.Map;

public class LookCommand {

    public static String execute(GameState gameState, String playerName) {
        StringBuilder builder = new StringBuilder();
        PlayerEntity currentPlayer =  (PlayerEntity) gameState.getEntityMap("player").get(playerName);
        String locationName = currentPlayer.getLocationName();
        LocationEntity playerLocation = (LocationEntity) gameState.getEntityMap("location").get(locationName);
        Map<String, GameEntity> visibleEntities = gameState.getEntitiesFromLocation("all", locationName);
        for (GameEntity visibleEntity : visibleEntities.values()) {
            if (visibleEntity != currentPlayer) {
                if (!builder.isEmpty()) {
                    builder.append("\n");
                }
                builder.append(visibleEntity.getDescription());
            }
        }
        Map<String, LocationEntity> pathsFromLocation = playerLocation.getPaths();
        for (String pathName : pathsFromLocation.keySet() ) {
            if (!builder.isEmpty()) {
                builder.append("\n");
            }
            builder.append(pathName);
        }
        String path = LookCommand.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println("Current class path: " + path);
        return builder.toString();
    }
}
