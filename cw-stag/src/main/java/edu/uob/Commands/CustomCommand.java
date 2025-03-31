package edu.uob.Commands;

import edu.uob.GameAction.GameAction;
import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.PlayerEntity;
import edu.uob.GameState;
import edu.uob.ResponseList;

import java.util.Map;

public class CustomCommand {

    public static String execute(GameState gameState, GameAction action, String playerName) {
        PlayerEntity player = gameState.getPlayer(playerName);
        String playerLocation = player.getLocationName();
        int changeInHealth = action.getChangeInHealth();
        if (!player.modifyHealth(changeInHealth)) {
            gameState.resetPlayer(player);
            return ResponseList.playerDeath();
        }
        Map<String, GameEntity> producedEntities = action.getProducedEntities();
        for (String entityName : producedEntities.keySet()) {
            gameState.produceEntity(playerLocation, entityName, player);
        }
        Map<String, GameEntity> consumedEntities = action.getConsumedEntities();
        for (String entityName : consumedEntities.keySet()) {
            gameState.consumeEntity(playerLocation, entityName, player);
        }
        return action.getNarration();
    }
}
