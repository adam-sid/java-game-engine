package edu.uob.Commands;

import edu.uob.GameEntity.PlayerEntity;
import edu.uob.GameState;

public class HealthCommand {
    public static String execute(GameState gameState, String playerName) {
        PlayerEntity currentPlayer = gameState.getPlayer(playerName);
        return String.valueOf(currentPlayer.getHealth());
    }
}
