package edu.uob.GameAction;

import edu.uob.GameEntity.GameEntity;

import java.util.Map;

public class GameAction
{
    private final Map<String, GameEntity> subjectEntities;
    private final Map<String, GameEntity> consumedEntities;
    private final Map<String, GameEntity> producedEntities;
    private final String narration;
    private int changeInHealth = 0;

    //class that holds subjects, consumed entities and produced entities
    public GameAction(Map<String, GameEntity> subjectEntities, Map<String, GameEntity> consumedEntities,
                      Map<String, GameEntity> producedEntities, String narration) {
        this.subjectEntities = subjectEntities;
        this.consumedEntities = consumedEntities;
        this.producedEntities = producedEntities;
        this.narration = narration;
    }

    public void setChangeInHealth(int changeInHealth) {
        this.changeInHealth += changeInHealth;
    }

    public int getChangeInHealth() {
        return this.changeInHealth;
    }

    public Map<String, GameEntity> getSubjectEntities() {
        return this.subjectEntities;
    }

    public Map<String, GameEntity> getConsumedEntities() {
        return this.consumedEntities;
    }

    public Map<String, GameEntity> getProducedEntities() {
        return this.producedEntities;
    }

    public String getNarration() {
        return this.narration;
    }

}
