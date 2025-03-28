package edu.uob;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameAction.GameAction;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ActionFileParser {

    //main method - calls all helpers and ensures file can be found in config
    public static void parseActionFile(File file, GameState gameState) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(file);
            Element root = document.getDocumentElement();
            NodeList actions = root.getChildNodes();
            extractActions(gameState, actions);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
    //TODO shorten this method
    //creates GameAction objects from the XML file and adds to game state
    private static void extractActions(GameState gameState, NodeList actions) {
        for (int actionIndex = 1; actionIndex < actions.getLength(); actionIndex+=2) {
            Element action = (Element) actions.item(actionIndex);
            NodeList actionChildren = action.getChildNodes();
            Set<String> triggers = new HashSet<>();
            Set<String> subjects = new HashSet<>();
            Set<String> produced = new HashSet<>();
            Set<String> consumed = new HashSet<>();
            String narration = null;
            for (int actionChildrenIndex = 0; actionChildrenIndex < actionChildren.getLength(); actionChildrenIndex++) {
                Node actionChild = actionChildren.item(actionChildrenIndex);
                String type = actionChild.getNodeName().toLowerCase();
                switch (type) {
                    case "triggers": {
                        triggers.addAll(extractLeafContent(actionChild));
                        break;
                    }
                    case "subjects": {
                        subjects.addAll(extractLeafContent(actionChild));
                        break;
                    }
                    case "produced": {
                        produced.addAll(extractLeafContent(actionChild));
                        break;
                    }
                    case "consumed": {
                        consumed.addAll(extractLeafContent(actionChild));
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
            Map<String, GameEntity> subjectEntities = lookUpEntities(gameState, subjects);
            Map<String, GameEntity> producedEntities = lookUpEntities(gameState, produced);
            Map<String, GameEntity> consumedEntities = lookUpEntities(gameState, consumed);
            GameAction gameAction = new GameAction(subjectEntities, consumedEntities, producedEntities, narration);
            for (String trigger: triggers) {
                gameState.addGameAction(trigger, gameAction);
            }
        }
    }

    //checks whether entities exist in the game state, if not throws exception as action file is faulty
    private static Map<String, GameEntity> lookUpEntities(GameState gameState, Set<String> subjects) {
        Map<String, GameEntity> entities = new HashMap<>();
        for (String entityName : subjects) {
            GameEntity subjectEntity = gameState.getEntityMap("all").get(entityName);
            if (subjectEntity == null) {
                throw new RuntimeException(entityName);
            }
            entities.put(entityName, subjectEntity);
        }
        return entities;
    }

    //gets the trigger and entity names from the XML
    private static Set<String> extractLeafContent(Node actionChild) {
        NodeList triggerChildren = actionChild.getChildNodes();
        Set<String> leafContents = new HashSet<>();
        for (int i = 0; i < triggerChildren.getLength(); i++) {
            if (triggerChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element node = (Element) triggerChildren.item(i);
                String content = node.getTextContent().toLowerCase();
                leafContents.add(content);
            }
        }
        return leafContents;
    }
}
