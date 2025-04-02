package edu.uob.GameParsing;

import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameAction.GameAction;
import edu.uob.GameState;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ActionFileParser {

    private static int changeInHealth = 0;

    //main method - calls all helpers and ensures file can be found in config
    public static void parseActionFile(File file, GameState gameState) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(file);
            Element root = document.getDocumentElement();
            NodeList actions = root.getChildNodes();
            ActionFileParser.extractActions(gameState, actions);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
    //creates GameAction objects from the XML file and adds to game state
    private static void extractActions(GameState gameState, NodeList actions) {
        for (int actionIndex = 1; actionIndex < actions.getLength(); actionIndex+=2) {
            Element action = (Element) actions.item(actionIndex);
            NodeList actionChildren = action.getChildNodes();
            Set<String> triggers = new HashSet<>();
            List<String> subjects = new LinkedList<>();
            List<String> produced = new LinkedList<>();
            List<String> consumed = new LinkedList<>();
            changeInHealth = 0;
            String narration = extractActionProperties(actionChildren, triggers, subjects, produced, consumed);
            Map<String, GameEntity> subjectEntities = lookUpEntities(gameState, subjects);
            Map<String, GameEntity> producedEntities = lookUpEntities(gameState, produced);
            Map<String, GameEntity> consumedEntities = lookUpEntities(gameState, consumed);
            GameAction gameAction = new GameAction(subjectEntities, consumedEntities, producedEntities, narration);
            gameAction.setChangeInHealth(changeInHealth);
            for (String trigger: triggers) {
                gameState.addGameAction(trigger, gameAction);
            }
        }
    }

    private static String extractActionProperties(NodeList actionChildren, Set<String> triggers,
                                              List<String> subjects, List<String> produced, List<String> consumed) {
        String narration = null;
        for (int actionChildrenIndex = 0; actionChildrenIndex < actionChildren.getLength(); actionChildrenIndex++) {
            Node actionChild = actionChildren.item(actionChildrenIndex);
            String type = actionChild.getNodeName().toLowerCase();
            switch (type) {
                case "triggers": {
                    triggers.addAll(extractLeafContent(actionChild, null));
                    break;
                }
                case "subjects": {
                    subjects.addAll(extractLeafContent(actionChild, null));
                    break;
                }
                case "produced": {
                    produced.addAll(extractLeafContent(actionChild, true));
                    break;
                }
                case "consumed": {
                    consumed.addAll(extractLeafContent(actionChild, false));
                    break;
                }
                case "narration": {
                    narration = actionChild.getTextContent().toLowerCase();
                    break;
                }
                default:
                    break;
            }
        }
        return narration;
    }

    //checks whether entities exist in the game state, if not throws exception as action file is faulty
    private static Map<String, GameEntity> lookUpEntities(GameState gameState, List<String> subjects) {
        Map<String, GameEntity> entities = new HashMap<>();
        for (String entityName : subjects) {
            if (!Objects.equals(entityName, "health")) {
                GameEntity subjectEntity = gameState.getEntityMap("all").get(entityName);
                if (subjectEntity == null) {
                    throw new RuntimeException(entityName);
                }
                entities.put(entityName, subjectEntity);
            }
        }
        return entities;
    }

    //gets the trigger and entity names from the XML
    private static Set<String> extractLeafContent(Node actionChild, Boolean addHealth) {
        NodeList triggerChildren = actionChild.getChildNodes();
        Set<String> leafContents = new HashSet<>();
        for (int i = 0; i < triggerChildren.getLength(); i++) {
            if (triggerChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element node = (Element) triggerChildren.item(i);
                String content = node.getTextContent().toLowerCase();
                setHealth(content, addHealth);
                leafContents.add(content);
            }
        }
        return leafContents;
    }

    private static void setHealth(String content, Boolean addHealth) {
        if (addHealth == null || !content.trim().equals("health")) {
            return;
        }
        if (addHealth) {
            changeInHealth++;
        } else {
            changeInHealth--;
        }

    }
}
