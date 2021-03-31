package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.WorkLabel;
import fai.utb.db.entity.entityEnum.Degree;
import fai.utb.db.entity.entityEnum.FormOfStudy;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.Semester;
import fai.utb.db.reader.DataReaderXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GroupManagerImpl extends GroupManager {


    private Document document;

    public GroupManagerImpl(Document document) {
        this.document = document;
    }

    @Override
    public void create(Group group) {

        Element root = document.getDocumentElement();
        Element groupElement = getGroupNodeToXML(group);
        root.appendChild(groupElement);



    }

    @Override
    public void remove(Group group) {
        remove(document, group.getId(),"group", "Groups.xml");
    }

    @Override
    public void setQuantity(Group group, int quantity) {

    }

    @Override
    public List<Group> getAllGroup() {

        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName("group");
        List<Group> groups = new ArrayList<>();

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                groups.add(getGroupFromXML(element));
            }
        }

        return groups;
    }

    private Group getGroupFromXML(Element element) {
        return new Group(Long.parseLong(element.getAttribute("id")),
                Degree.valueOf(element.getElementsByTagName("degree").item(0).getTextContent()),
                element.getElementsByTagName("field_of_study").item(0).getTextContent(),
                FormOfStudy.valueOf(element.getElementsByTagName("form_of_study").item(0).getTextContent()),
                Semester.valueOf(element.getElementsByTagName("semester").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("grade").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("quantity").item(0).getTextContent()),
                Language.valueOf(element.getElementsByTagName("language").item(0).getTextContent()));

    }

    @Override
    public Group getGroup(Long id) {
        NodeList nodeList = document.getElementsByTagName("group");

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (Long.parseLong(element.getAttribute("id")) == id) {
                    return getGroupFromXML(element);
                }
            }
        }
        return null;
    }

    private Element getGroupNodeToXML(Group group) {
        List<String> xmlDom = Arrays.asList(
                "degree",
                "field_of_study",
                "form_of_study",
                "semester",
                "grade",
                "quantity",
                "language");
        
        List<String> groupItems = group.getGroupItems();
        Element groupElement = document.createElement("group");
        groupElement.setAttribute("id", group.getId().toString());

        for (int index = 0; index < xmlDom.size(); index++) {
            Element newElement = document.createElement(xmlDom.get(index));

            newElement.appendChild(document.createTextNode(groupItems.get(index)));
            groupElement.appendChild(newElement);
        }
        return groupElement;
    }
}
