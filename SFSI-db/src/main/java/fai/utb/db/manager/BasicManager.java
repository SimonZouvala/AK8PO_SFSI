package fai.utb.db.manager;

import fai.utb.db.exception.IOXmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Class represent basic manage for all entities in XML.
 *
 * @author Šimon Zouvala
 */
public class BasicManager {

    protected static final String WORK_LABELS_XML = "xml/WorkLabels.xml";
    protected static final String SUBJECTS_XML = "xml/Subjects.xml";
    protected static final String GROUPS_XML = "xml/Groups.xml";
    protected static final String EMPLOYEES_XML = "xml/Employees.xml";

    /**
     * Return DOM of required xml file.
     *
     * @param xml path to required xml file
     * @return DOM of required xml file
     */
    public Document getData(String xml) {
        Document document;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(xml));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new IOXmlException("Error due to read file " + xml, e);
        }

        if (document != null) {
            document.getDocumentElement().normalize();
        }
        return document;
    }

    /**
     * Remove required item of entity (element in DOM).
     *
     * @param document DOM
     * @param id of item of entity to remove
     * @param entity required type of entity to remove
     * @param file where entity is saved
     */
    public void remove(Document document, UUID id, String entity, String file) {
       try {
        NodeList nodeList = document.getElementsByTagName(entity);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (UUID.fromString(element.getAttribute("id")).equals(id)) {
                    document.getDocumentElement().removeChild(node);
                }
            }
        }
        saveChangesToXML(document, file);
       }catch (Exception e){
           throw new IllegalArgumentException("Try to remove something what not exist", e);
       }
    }

    /**
     * Create new element to DOM.
     *
     * @param document DOM
     * @param element which will be saved
     * @param file where entity to save
     */
    public void create(Document document, Element element, String file) {
        Element root = document.getDocumentElement();
        root.appendChild(element);
        saveChangesToXML(document, file);
    }

    /**
     * Save all changes in DOM to required xml file.
     *
     * @param document DOM
     * @param xml file to update
     */
    public void saveChangesToXML(Document document, String xml) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;

        try {
            transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xml));
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            throw new IOXmlException("Error due to save changes to file " + xml, e);
        }
    }

    /**
     * Set new value to required entity (element in DOM).
     *
     * @param document DOM
     * @param id of item of entity to set
     * @param elementToSet which will be set
     * @param value to set
     * @param file where entity is saved
     * @param mainElement where is entity saved
     */
    public void setElement(Document document, UUID id, String elementToSet,
                           String value, String file, String mainElement) {
        NodeList nodeList = document.getElementsByTagName(mainElement);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (UUID.fromString(element.getAttribute("id")).equals(id)) {
                    element.getElementsByTagName(elementToSet).item(0).setTextContent(value);
                    break;
                }
            }
        }
        document.getDocumentElement().normalize();
        saveChangesToXML(document, file);
    }

    /**
     * Create new element to DOM.
     *
     * @param document DOM
     * @param xmlDom list of all type of items in element
     * @param items list of all value of items in element
     * @param mainElement where will be element saved
     * @return new created element in DOM
     */
    public Element getItemToXML(Document document, List<String> xmlDom,
                                List<String> items, String mainElement) {
        Element element = document.createElement(mainElement);
        element.setAttribute("id", UUID.randomUUID().toString());

        for (int index = 0; index < xmlDom.size(); index++) {
            Element newElement = document.createElement(xmlDom.get(index));
            newElement.appendChild(document.createTextNode(items.get(index)));
            element.appendChild(newElement);
        }
        return element;
    }

    /**
     * Create new element to DOM.
     *
     * @param document DOM
     * @param xmlDom list of all type of items in element
     * @param items list of all value of items in element
     * @param mainElement where will be element saved
     * @param idList list of ides which will be saved as a reference to another entity
     * @param entityName name of reference to another entity
     * @return new created element in DOM
     */
    public Element getItemToXML(Document document, List<String> xmlDom,
                                List<String> items, String mainElement,
                                List<UUID> idList, String entityName) {
        Element element = document.createElement(mainElement);
        element.setAttribute("id", UUID.randomUUID().toString());

        for (int index = 0; index < xmlDom.size(); index++) {
            Element newElement = document.createElement(xmlDom.get(index));

            if (index == xmlDom.size() - 1) {
                if (items.size() > 0) {

                    for (UUID entityId : idList) {
                        Element label = document.createElement(entityName);
                        label.setAttribute("id", entityId.toString());
                        newElement.appendChild(label);
                    }
                    element.appendChild(newElement);
                }
            } else {
                newElement.appendChild(document.createTextNode(items.get(index)));
                element.appendChild(newElement);
            }
        }
        return element;
    }
}
