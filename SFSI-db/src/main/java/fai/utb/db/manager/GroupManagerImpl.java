package fai.utb.db.manager;

import fai.utb.db.entity.Group;
import fai.utb.db.entity.entityEnum.Degree;
import fai.utb.db.entity.entityEnum.FormOfStudy;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.Semester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GroupManagerImpl extends GroupManager {

    private final Document document;
    private static final String MAIN_ELEMENT = "group";

    public GroupManagerImpl() {
        this.document = getGroupsDocument();
    }

    @Override
    public void create(Group group) {
        Element groupElement = getItemToXML(
                document,
                getGroupXmlDomList(),
                group.getGroupItems(),
                group.getId(),
                MAIN_ELEMENT);
        create(document, groupElement, GROUPS_XML);
    }

    @Override
    public void remove(Group group) {
        remove(document, group.getId(), MAIN_ELEMENT, GROUPS_XML);
    }

    @Override
    public void setQuantity(Group group, int quantity) {
        setElement(
                document,
                group.getId(),
                "quantity",
                String.valueOf(quantity),
                GROUPS_XML,
                MAIN_ELEMENT);
       new WorkLabelManagerImpl().generateWorkLabelsAfterUpgrade(getGroup(group.getId()));
    }

    @Override
    public List<Group> getAllGroup() {
//        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);
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
        return new Group(
                UUID.fromString(element.getAttribute("id")),
                Degree.valueOf(element.getElementsByTagName("degree").item(0).getTextContent()),
                element.getElementsByTagName("field_of_study").item(0).getTextContent(),
                FormOfStudy.valueOf(element.getElementsByTagName("form_of_study").item(0).getTextContent()),
                Semester.valueOf(element.getElementsByTagName("semester").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("grade").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("quantity").item(0).getTextContent()),
                Language.valueOf(element.getElementsByTagName("language").item(0).getTextContent()));

    }

    @Override
    public Group getGroup(UUID id) {
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (UUID.fromString(element.getAttribute("id")).equals(id)) {
                    return getGroupFromXML(element);
                }
            }
        }
        return null;
    }

    private List<String> getGroupXmlDomList() {
        return Arrays.asList(
                "degree",
                "field_of_study",
                "form_of_study",
                "semester",
                "grade",
                "quantity",
                "language");

    }
}
