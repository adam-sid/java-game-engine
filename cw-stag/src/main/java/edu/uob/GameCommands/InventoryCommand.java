package edu.uob.GameCommands;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.PlayerEntity;
import edu.uob.GameState;

import java.util.Map;

public class InventoryCommand {

    public static String execute(GameState gameState, String playerName) {
        StringBuilder builder = new StringBuilder();
        PlayerEntity player = (PlayerEntity) gameState.getEntityMap("player").get(playerName);
        Map<String, GameEntity> invEntities = player.getInventory();
        for (String invEntity : invEntities.keySet()) {
            if (!builder.isEmpty()) {
                builder.append("\n");
            }
            builder.append(invEntity);
        }
        return builder.toString();
    }
}
