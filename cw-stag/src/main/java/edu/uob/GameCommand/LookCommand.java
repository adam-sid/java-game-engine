package edu.uob.GameCommand;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.LocationEntity;
import edu.uob.GameState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LookCommand {

    public static String execute(GameState gameState, String playerName) {
        StringBuilder messageBuilder = new StringBuilder();
        LocationEntity playerLocation = gameState.getPlayerLocation(playerName);
        String locationName = playerLocation.getName();
        messageBuilder.append("You are in ").append(playerLocation.getDescription());
        messageBuilder.append("\n\nYou can see:");
        Map<String, GameEntity> visibleEntities = new HashMap<>(gameState
                .getEntitiesFromLocation("all", locationName));
        visibleEntities.remove(locationName);
        visibleEntities.remove(playerName);
        int lineWidth = LookCommand.findLongestString(visibleEntities.keySet()) ;
        LookCommand.addVisibleEntities(visibleEntities, messageBuilder, lineWidth);
        LookCommand.addPaths(messageBuilder, gameState, playerLocation);
        return messageBuilder.toString();
    }

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

    private static void addPaths(StringBuilder messageBuilder, GameState gameState,
                                 LocationEntity playerLocation) {
        HashMap<String, LocationEntity> availablePaths = playerLocation.getPaths();
        messageBuilder.append("\n\nYou can also access the following locations:");
        for (String path : availablePaths.keySet()) {
            messageBuilder.append("\n");
            messageBuilder.append(path);
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


