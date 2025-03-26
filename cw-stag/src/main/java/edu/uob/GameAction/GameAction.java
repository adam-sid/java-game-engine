package edu.uob.GameAction;

import edu.uob.Entity.Entity;

import java.util.HashSet;

public class GameAction
{
    //TODO the trigger word should be stored as the key in a HashMap that pairs with a CustomAction
    private final HashSet<Entity> subjectEntities;
    private final HashSet<Entity> consumedEntities;
    private final HashSet<Entity> producedEntities;
    private final String narration;

    public GameAction(HashSet<Entity> subjectEntities, HashSet<Entity> consumedEntities,
                      HashSet<Entity> producedEntities, String narration) {
        this.subjectEntities = subjectEntities;
        this.consumedEntities = consumedEntities;
        this.producedEntities = producedEntities;
        this.narration = narration;
    }

}
