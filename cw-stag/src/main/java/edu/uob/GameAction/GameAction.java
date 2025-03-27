package edu.uob.GameAction;

import edu.uob.GameEntity.GameEntity;

import java.util.Map;

public class GameAction
{
    //TODO the trigger word should be stored as the key in a HashMap that pairs with a CustomAction
    private final Map<String, GameEntity> subjectEntities;
    private final Map<String, GameEntity> consumedEntities;
    private final Map<String, GameEntity> producedEntities;
    private final String narration;

    public GameAction(Map<String, GameEntity> subjectEntities, Map<String, GameEntity> consumedEntities,
                      Map<String, GameEntity> producedEntities, String narration) {
        this.subjectEntities = subjectEntities;
        this.consumedEntities = consumedEntities;
        this.producedEntities = producedEntities;
        this.narration = narration;
    }

    public Map<String, GameEntity> getSubjectEntities() {
        return subjectEntities;
    }

    public Map<String, GameEntity> getConsumedEntities() {
        return consumedEntities;
    }

    public Map<String, GameEntity> getProducedEntities() {
        return producedEntities;
    }

    public String getNarration() {
        return narration;
    }

}
