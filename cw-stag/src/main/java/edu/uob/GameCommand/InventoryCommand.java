package edu.uob.GameCommand;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.PlayerEntity;
import edu.uob.GameState;

import java.util.Map;

public class InventoryCommand {

    //returns the name value of all entities within a given players inventory
    public static String executeCommand(GameState gameState, String playerName) {
        StringBuilder buildMessage = new StringBuilder();
        PlayerEntity player = gameState.getPlayer(playerName);
        Map<String, GameEntity> playerInventory = player.getInventory();
        for (String entity : playerInventory.keySet()) {
            if (!buildMessage.isEmpty()) {
                buildMessage.append("\n");
            }
            buildMessage.append(entity);
        }
        return buildMessage.toString();
    }
}
