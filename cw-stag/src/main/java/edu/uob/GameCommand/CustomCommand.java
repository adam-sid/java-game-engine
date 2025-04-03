package edu.uob.GameCommand;

import edu.uob.GameAction.GameAction;
import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.PlayerEntity;
import edu.uob.GameState;
import edu.uob.ResponseList;

import java.util.Map;

public class CustomCommand {
    //executes a custom command by first producing any entities in the player location,
    // then consuming any entities. If the player dies this is performed last with the
    // resetPlayer command
    public static String executeCommand(GameState gameState, GameAction gameAction, String playerName) {
        PlayerEntity player = gameState.getPlayer(playerName);
        String playerLocation = player.getLocationName();
        int changeInHealth = gameAction.getChangeInHealth();
        boolean playerIsAlive = player.modifyHealth(changeInHealth);
        Map<String, GameEntity> producedEntities = gameAction.getProducedEntities();
        for (String entityName : producedEntities.keySet()) {
            gameState.produceEntity(playerLocation, entityName, player);
        }
        Map<String, GameEntity> consumedEntities = gameAction.getConsumedEntities();
        for (String entityName : consumedEntities.keySet()) {
            gameState.consumeEntity(playerLocation, entityName, player);
        }
        if (!playerIsAlive) {
            gameState.resetPlayer(player);
            return ResponseList.playerDeath();
        }
        return gameAction.getNarration();
    }
}
