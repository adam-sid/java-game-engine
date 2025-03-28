package edu.uob.Commands;

import edu.uob.GameAction.GameAction;
import edu.uob.GameEntity.GameEntity;
import edu.uob.GameState;

import java.util.Map;

public class CustomCommand {

    public static String execute(GameState gameState, GameAction action, String playerName) {
        Map<String, GameEntity> consumedEntities = action.getConsumedEntities();
        String playerLocation = gameState.getEntityMap("player").get(playerName).getLocationName();
        for (String entityName : consumedEntities.keySet()) {
            gameState.consumeEntity(entityName);
        }
        Map<String, GameEntity> producedEntities = action.getProducedEntities();
        for (String entityName : producedEntities.keySet()) {
            gameState.produceEntity(playerLocation, entityName);
        }
        return action.getNarration();
    }
}
