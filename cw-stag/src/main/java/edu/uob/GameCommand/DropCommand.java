package edu.uob.GameCommand;

import edu.uob.GameState;

public class DropCommand {
    //executes the drop command by calling the moveFromInventory method
    public static String executeCommand(GameState gameState, String playerName, String artefactName) {
        gameState.moveFromInventory(playerName, artefactName);
        StringBuilder buildMessage = new StringBuilder("You removed ");
        buildMessage.append(artefactName);
        buildMessage.append(" from inventory");
        return buildMessage.toString();
    }
}
