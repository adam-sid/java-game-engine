package edu.uob.Commands;

import edu.uob.GameState;

public class DropCommand {

    public static String execute(GameState gameState, String playerName, String artefactName) {
        gameState.moveFromInventory(playerName, artefactName);
        return "You removed an item from inventory!";
    }
}
