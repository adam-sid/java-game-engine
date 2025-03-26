package edu.uob;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
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
    private final HashMap<String, HashSet<GameAction>> actions;

    public GameActionParser(String filePath) {
        this.fileName = filePath;
        this.actions = parseActionFile(this.fileName);
    }


    public HashMap<String, HashSet<GameAction>> parseActionFile(String fileName) {
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
                for (int actionChildrenIndex = 0; actionChildrenIndex < actionChildren.getLength(); actionChildrenIndex++) {
                    Node actionChild = actionChildren.item(actionChildrenIndex);
                    String type = actionChild.getNodeName();
                    switch (type) {
                        case "triggers": {
                            NodeList triggerChildren = actionChild.getChildNodes();
                            for (int i = 0; i < triggerChildren.getLength(); i++) {
                                if (triggerChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
                                    Element keyphraseNode = (Element) triggerChildren.item(i);
                                    String keyphrase = keyphraseNode.getTextContent();
                                    System.out.println(keyphrase);
                                }
                            }
                            break;
                        }
                        case "subjects": {
                            NodeList triggerChildren = actionChild.getChildNodes();
                            for (int i = 0; i < triggerChildren.getLength(); i++) {
                                if (triggerChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
                                    Element keyphraseNode = (Element) triggerChildren.item(i);
                                    String keyphrase = keyphraseNode.getTextContent();
                                    System.out.println(keyphrase);
                                }
                            }

                            break;
                        }
                        case "produced": {
                            NodeList triggerChildren = actionChild.getChildNodes();
                            for (int i = 0; i < triggerChildren.getLength(); i++) {
                                if (triggerChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
                                    Element keyphraseNode = (Element) triggerChildren.item(i);
                                    String keyphrase = keyphraseNode.getTextContent();
                                    System.out.println(keyphrase);
                                }
                            }
                            break;
                        }
                        case "consumed": {
                            NodeList triggerChildren = actionChild.getChildNodes();
                            for (int i = 0; i < triggerChildren.getLength(); i++) {
                                if (triggerChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
                                    Element keyphraseNode = (Element) triggerChildren.item(i);
                                    String keyphrase = keyphraseNode.getTextContent();
                                    System.out.println(keyphrase);
                                }
                            }
                            break;
                        }
                        case "narration":

                            break;
                        default:
                            break;
                    }
                }

            }
            // Get the first trigger phrase
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return null;
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
