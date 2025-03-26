package edu.uob.GameAction;

import edu.uob.Entity.Entity;

import java.util.Set;

public class GameAction
{
    //TODO the trigger word should be stored as the key in a HashMap that pairs with a CustomAction
    private final Set<Entity> subjectEntities;
    private final Set<Entity> consumedEntities;
    private final Set<Entity> producedEntities;
    private final String narration;

    public GameAction(Set<Entity> subjectEntities, Set<Entity> consumedEntities,
                      Set<Entity> producedEntities, String narration) {
        this.subjectEntities = subjectEntities;
        this.consumedEntities = consumedEntities;
        this.producedEntities = producedEntities;
        this.narration = narration;
    }

}
