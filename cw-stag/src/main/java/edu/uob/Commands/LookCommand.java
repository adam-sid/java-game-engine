package edu.uob.Commands;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.LocationEntity;
import edu.uob.GameEntity.PlayerEntity;
import edu.uob.GameState;

import java.util.HashMap;
import java.util.Map;

public class LookCommand {

    public static String execute(GameState gameState, String playerName) {
        StringBuilder messageBuilder = new StringBuilder();
        PlayerEntity currentPlayer = gameState.getPlayer(playerName);
        LocationEntity playerLocation = gameState.getPlayerLocation(playerName);
        String locationName = playerLocation.getName();

        messageBuilder.append("You are in ");
        messageBuilder.append(playerLocation.getDescription());
        messageBuilder.append("You can see ");
        Map<String, GameEntity> visibleEntities = new HashMap<>(gameState
                .getEntitiesFromLocation("all", locationName));
        visibleEntities.remove(locationName);
        visibleEntities.remove(playerName);
        
        for (GameEntity visibleEntity : visibleEntities.values()) {
            messageBuilder.append("\n");
            messageBuilder.append(visibleEntity.getDescription());
        }

        LookCommand.addPaths(messageBuilder, gameState, locationName);

        Map<String, LocationEntity> pathsFromLocation = playerLocation.getPaths();
        boolean firstPass = true;
        for (String pathName : pathsFromLocation.keySet() ) {
            if (firstPass) {
                firstPass = false;
                messageBuilder.append("\nYou can also go to:");
            }
            if (!messageBuilder.isEmpty()) {
                messageBuilder.append("\n");
            }
            messageBuilder.append(pathName);
        }
        return messageBuilder.toString();
    }

    private static void addPaths(StringBuilder messageBuilder, GameState gameState, String locationName) {
        messageBuilder.append("\nYou also see paths to the following locations:");
    }


}


