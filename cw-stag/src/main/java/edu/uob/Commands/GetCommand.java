package edu.uob.Commands;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameState;

public class GetCommand {

    public static String execute(GameState gameState, String playerName, String artefactName) {
        gameState.moveToInventory(playerName, artefactName);
        return "You added an item to inventory!";
    }
}
