package edu.uob.Commands;

import edu.uob.GameState;

public class DropCommand {

    public static String execute(GameState gameState, String playerName, String artefactName) {
        gameState.moveFromInventory(playerName, artefactName);
        StringBuilder buildMessage = new StringBuilder("You removed ");
        buildMessage.append(artefactName);
        buildMessage.append(" from inventory");
        return buildMessage.toString();
    }
}
