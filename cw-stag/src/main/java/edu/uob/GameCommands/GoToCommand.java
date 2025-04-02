package edu.uob.GameCommands;

import edu.uob.GameEntity.PlayerEntity;
import edu.uob.GameState;

public class GoToCommand {

    public static String execute(GameState gameState, String playerName, String nextLocationName) {
        PlayerEntity playerToMove = (PlayerEntity) gameState.getEntityMap("player").get(playerName);
        gameState.moveEntity(nextLocationName, playerToMove.getName() );
        return LookCommand.execute(gameState, playerName);
    }
}
