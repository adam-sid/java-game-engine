package edu.uob.GameCommand;

import edu.uob.GameEntity.PlayerEntity;
import edu.uob.GameState;

public class GoToCommand {
    //executes goto command with moveEntity
    public static String executeCommand(GameState gameState, String playerName, String nextLocationName) {
        PlayerEntity player = gameState.getPlayer(playerName);
        gameState.moveEntity(nextLocationName, player.getName() );
        return LookCommand.executeCommand(gameState, playerName);
    }
}
