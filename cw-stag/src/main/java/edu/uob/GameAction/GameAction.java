package edu.uob.GameAction;

import edu.uob.Entity.Entity;

import java.util.Map;
import java.util.Set;

public class GameAction
{
    //TODO the trigger word should be stored as the key in a HashMap that pairs with a CustomAction
    private final Map<String, Entity> subjectEntities;
    private final Map<String, Entity> consumedEntities;
    private final Map<String, Entity> producedEntities;
    private final String narration;

    public GameAction(Map<String, Entity> subjectEntities, Map<String, Entity> consumedEntities,
                      Map<String, Entity> producedEntities, String narration) {
        this.subjectEntities = subjectEntities;
        this.consumedEntities = consumedEntities;
        this.producedEntities = producedEntities;
        this.narration = narration;
    }

}
