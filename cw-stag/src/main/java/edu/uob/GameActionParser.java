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

import edu.uob.GameAction.GameAction;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GameActionParser {

    private final String fileName;
    private final Map<String, Set<GameAction>> actions;

    public GameActionParser(String filePath) {
        this.fileName = filePath;
        this.actions = parseActionFile(this.fileName);
    }


    public Map<String, Set<GameAction>> parseActionFile(String fileName) {
        try {
            String filePath = new StringBuilder("config")
                    .append(File.separator)
                    .append(this.fileName)
                    .toString();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(filePath);
            Element root = document.getDocumentElement();
            NodeList actions = root.getChildNodes();
            for (int actionIndex = 1; actionIndex < actions.getLength(); actionIndex+=2) {
                Element action = (Element) actions.item(actionIndex);
                NodeList actionChildren = action.getChildNodes();
                Set<String> triggers = new HashSet<>();
                Set<String> subjects = new HashSet<>();
                Set<String> produced = new HashSet<>();
                Set<String> consumed = new HashSet<>();
                String narration;
                for (int actionChildrenIndex = 0; actionChildrenIndex < actionChildren.getLength(); actionChildrenIndex++) {
                    Node actionChild = actionChildren.item(actionChildrenIndex);
                    String type = actionChild.getNodeName();
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
                            narration = actionChild.getNodeValue();
                            break;
                        }
                        default:
                            break;
                    }
                }
                System.out.println("Hello");
            }
            // Get the first trigger phrase
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static Set<String> extractLeafContent(Node actionChild) {
        NodeList triggerChildren = actionChild.getChildNodes();
        Set<String> leafContents = new HashSet<>();
        for (int i = 0; i < triggerChildren.getLength(); i++) {
            if (triggerChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element node = (Element) triggerChildren.item(i);
                String content = node.getTextContent();
                System.out.println(content);
                leafContents.add(content);
            }
        }
        return leafContents;
    }

//    public HashMap<String, HashSet<GameAction>> parseActionFile(String fileName) {
//        try {
//            String filePath = new StringBuilder("config")
//                    .append(File.separator)
//                    .append(this.fileName)
//                    .toString();
//            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//            Document document = builder.parse(filePath);
//            Element root = document.getDocumentElement();
//            NodeList actions = root.getChildNodes();
//            for (int actionIndex = 1; actionIndex < actions.getLength(); actionIndex+=2) {
//                Element action = (Element) actions.item(actionIndex);
//                Element triggers = (Element)action.getElementsByTagName("triggers").item(0);
//                for (int triggerIndex = 0; triggerIndex < triggers.getChildNodes().getLength(); triggerIndex++) {
//                    Node trigger = triggers.getElementsByTagName("keyphrase").item(triggerIndex);
//                    String triggerPhrase = trigger.getTextContent();
//                    System.out.println(triggerPhrase);
//                }
//
//            }
//            // Get the first trigger phrase
//        } catch (ParserConfigurationException | IOException | SAXException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }


    public HashMap<String, HashSet<GameAction>> getActions() {
        return null;
    }
}
