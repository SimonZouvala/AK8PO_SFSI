package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.reader.DataReaderXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class BasicManager {

    public void remove(Document document, Long id, String entity, String file){
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


    public void saveChangesToXML(Document document, String xml){
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

}
