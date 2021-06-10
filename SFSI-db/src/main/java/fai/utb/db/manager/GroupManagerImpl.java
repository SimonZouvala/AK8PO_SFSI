package fai.utb.db.manager;

import fai.utb.db.entity.Group;
import fai.utb.db.entity.entityEnum.Degree;
import fai.utb.db.entity.entityEnum.FormOfStudy;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.Semester;
import fai.utb.db.exception.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of abstract class {@link GroupManager}
 * @author Šimon Zouvala
 */
public class GroupManagerImpl extends GroupManager {

    private final Document document;
    private static final String MAIN_ELEMENT = "group";
    /**
     * Constructor for get current DOM of Groups.xml.
     */
    public GroupManagerImpl() {
        this.document = getGroupsDocument();
    }

    /**
     * Constructor for get current DOM of relevant XML file.
     */
    public GroupManagerImpl(String xml) {
        this.document = getData(xml);
    }

    private Document getGroupsDocument() {
        return getData(GROUPS_XML);
    }

    @Override
    public void create(Group group) {
        validate(group);

        if (isSameGroupInXml(group)) {
            throw new ValidationException(
                    "Group " + group.getFieldOfStudy() + " with form of study " + group.getFormOfStudy() + ", "
                            + group.getGrade()+   ". grade, "+ "semester " + group.getSemester()
                            + " and language " + group.getLanguage()+ "is already exist");

        }

        Element groupElement = getItemToXML(
                document,
                getGroupXmlDomList(),
                group.getGroupItems(),
                MAIN_ELEMENT);
        create(document, groupElement, GROUPS_XML);
    }

    private void validate(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("group is null");
        }
        if (group.getQuantity() <= 0) {
            throw new ValidationException("Quantity is negative number");
        }
        if (group.getFieldOfStudy().equals("") || group.getFieldOfStudy() == null) {
            throw new ValidationException("Field of study is empty");
        }
        if (group.getDegree() == null) {
            throw new ValidationException("Degree is not set");
        }
        if (group.getFormOfStudy() == null) {
            throw new ValidationException("Form of study is not set");
        }
        if (group.getLanguage() == null) {
            throw new ValidationException("Language is not set");
        }
        if (group.getSemester() == null) {
            throw new ValidationException("Semester is not set");
        }
        if (group.getGrade() < 1) {
            throw new ValidationException("Grade is negative number or zero");
        }

    }

    private boolean isSameGroupInXml(Group newGroup) {
        List<Group> groups = getAllGroup();

        for (Group oldGroup : groups) {
            if (oldGroup.equals(newGroup)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void remove(Group group) {
        validate(group);
        remove(document, group.getId(), MAIN_ELEMENT, GROUPS_XML);
    }

    @Override
    public void setQuantity(Group group, int quantity) {
        if (quantity < 0){
            throw new ValidationException("Quantity to set is negative");
        }
        validate(group);
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
        if (id == null){
            throw new IllegalArgumentException("Employee id is null");
        }
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
