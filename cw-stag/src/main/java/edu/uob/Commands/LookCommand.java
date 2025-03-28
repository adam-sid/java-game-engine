package edu.uob.Commands;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameState;
import java.util.Map;

public class LookCommand {

    public static String execute(GameState gameState, String playerName) {
        StringBuilder builder = new StringBuilder();
        String playerLocation = gameState.getEntityMap("player").get(playerName).getLocationName();
        Map<String, GameEntity> visibleEntities = gameState.getEntitiesFromLocation("all", playerLocation);

        for (String visibleEntity : visibleEntities.keySet()) {
            if (!builder.isEmpty()) {
                builder.append(", "); // Add a comma and space between entity names
            }
            builder.append(visibleEntity);
        }

        return builder.toString();
    }
}
