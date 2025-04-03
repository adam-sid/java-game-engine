package edu.uob.GameCommand;

import edu.uob.GameEntity.PlayerEntity;
import edu.uob.GameState;

public class HealthCommand {
    //returns the value of players current health level as a string
    public static String executeCommand(GameState gameState, String playerName) {
        PlayerEntity currentPlayer = gameState.getPlayer(playerName);
        return String.valueOf(currentPlayer.getHealth());
    }
}
