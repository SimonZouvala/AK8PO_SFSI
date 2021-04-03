package fai.utb.db.manager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class BasicManager {

    protected static final String WORK_LABELS_XML = "WorkLabels.xml";
    protected static final String SUBJECTS_XML = "Subjects.xml";
    protected static final String GROUPS_XML = "Groups.xml";
    protected static final String EMPLOYEES_XML = "Employees.xml";


    public Document getData(String xml) {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();


            document = builder.parse(new File(xml));
        } catch (SAXException saxException) {
            saxException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        if (document != null) {
            document.getDocumentElement().normalize();
        }
        return document;
    }

    public Document getEmployeesDocument() {
        return getData(EMPLOYEES_XML);
    }

    public Document getGroupsDocument() {
        return getData(GROUPS_XML);
    }

    public Document getSubjectsDocument() {
        return getData(SUBJECTS_XML);
    }

    public Document getWorklabelsDocument() {
        return getData(WORK_LABELS_XML);
    }


    public void remove(Document document, Long id, String entity, String file) {
        NodeList nodeList = document.getElementsByTagName(entity);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (Long.parseLong(element.getAttribute("id")) == id) {
                    document.getDocumentElement().removeChild(node);
                }
            }
        }
        saveChangesToXML(document, file);
    }

    public void create(Document document, Element element, String file) {
        Element root = document.getDocumentElement();
        root.appendChild(element);
        saveChangesToXML(document, file);
    }


    public void saveChangesToXML(Document document, String xml) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xml));
            transformer.transform(domSource, streamResult);
            System.out.println("Done save XML File: " + xml);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void setElement(Document document, Long id, String elementToSet, String value, String file, String mainElement) {
        NodeList nodeList = document.getElementsByTagName(mainElement);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (Long.parseLong(element.getAttribute("id")) == id) {
                    element.getElementsByTagName(elementToSet).item(0).setTextContent(String.valueOf(value));
                    break;
                }
            }
        }
        document.getDocumentElement().normalize();
        saveChangesToXML(document, file);


    }

    public Element getItemToXML(Document document, List<String> xmlDom, List<String> items, Long id, String mainElement) {
        Element element = document.createElement(mainElement);
        element.setAttribute("id", id.toString());

        for (int index = 0; index < xmlDom.size(); index++) {
            Element newElement = document.createElement(xmlDom.get(index));
            newElement.appendChild(document.createTextNode(items.get(index)));
            element.appendChild(newElement);
        }
        return element;
    }

    public Element getItemToXML(Document document, List<String> xmlDom, List<String> items, Long id, String mainElement, List<Long> idList, String entityName) {
        Element element = document.createElement(mainElement);
        element.setAttribute("id", id.toString());

        for (int index = 0; index < xmlDom.size(); index++) {
            Element newElement = document.createElement(xmlDom.get(index));

            if (index == xmlDom.size() - 1) {
                if (items.size() > 0) {

                    for (Long entityId : idList) {
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
