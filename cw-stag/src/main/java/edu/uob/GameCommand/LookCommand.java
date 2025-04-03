package edu.uob.GameCommand;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.LocationEntity;
import edu.uob.GameState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LookCommand {

    //executes look and builds a string containing current location, entities in location and paths
    public static String executeCommand(GameState gameState, String playerName) {
        StringBuilder buildMessage = new StringBuilder();
        LocationEntity playerLocation = gameState.getPlayerLocation(playerName);
        String locationName = playerLocation.getName();
        buildMessage.append("You are in ").append(playerLocation.getDescription());
        buildMessage.append(" (").append(playerLocation.getName()).append(")");
        buildMessage.append("\n\nYou can see:");
        Map<String, GameEntity> visibleEntities = new HashMap<>(gameState
                .getEntitiesFromLocation("all", locationName));
        visibleEntities.remove(locationName);
        visibleEntities.remove(playerName);
        int lineWidth = LookCommand.findLongestString(visibleEntities.keySet()) ;
        LookCommand.addVisibleEntities(visibleEntities, buildMessage, lineWidth);
        LookCommand.addPaths(buildMessage, playerLocation);
        return buildMessage.toString();
    }

    //adds to a string builder all entities visible to player
    private static void addVisibleEntities(Map<String, GameEntity> visibleEntities,
                                           StringBuilder messageBuilder, int lineWidth) {
        for (GameEntity visibleEntity : visibleEntities.values()) {
            messageBuilder.append("\n");
            messageBuilder.append(visibleEntity.getName());
            int padding = lineWidth - visibleEntity.getName().length() + 2;
            messageBuilder.append(" ".repeat(Math.max(0, padding)));
            messageBuilder.append(visibleEntity.getDescription());
        }
    }

    //adds to a string builder all path names from a location
    private static void addPaths(StringBuilder messageBuilder, LocationEntity playerLocation) {
        HashMap<String, LocationEntity> availablePaths = playerLocation.getPaths();
        messageBuilder.append("\n\nYou can also access the following locations:");
        for (String pathName : availablePaths.keySet()) {
            messageBuilder.append("\n");
            messageBuilder.append(pathName);
        }
    }

    private static int findLongestString(Set<String> stringSet) {
        int maxLength = 0;
        for (String string : stringSet) {
            int bufferLength = string.length();
            maxLength = Math.max(maxLength, bufferLength);
        }
        return maxLength;
    }
}


