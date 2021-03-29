package fai.utb.db.reader;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DataReaderXML implements DataReader {

    private final String xml;

    public DataReaderXML(String xml) {
        this.xml = xml;
    }

    @Override
    public Document getData() {
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
}
