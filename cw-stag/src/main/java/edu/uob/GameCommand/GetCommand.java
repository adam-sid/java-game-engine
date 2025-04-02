package edu.uob.GameCommand;

import edu.uob.GameState;

public class GetCommand {

    public static String execute(GameState gameState, String playerName, String artefactName) {
        gameState.moveToInventory(playerName, artefactName);
        StringBuilder buildMessage = new StringBuilder("You added ");
        buildMessage.append(artefactName);
        buildMessage.append(" to inventory");
        return buildMessage.toString();
    }
}
