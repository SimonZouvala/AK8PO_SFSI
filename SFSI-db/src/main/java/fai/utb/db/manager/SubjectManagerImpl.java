package fai.utb.db.manager;

import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
import fai.utb.db.entity.entityEnum.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SubjectManagerImpl extends SubjectManager {

    private final Document document;
    private static final String MAIN_ELEMENT = "subject";


    public SubjectManagerImpl() {
        this.document = getSubjectsDocument();
    }

    private Document getSubjectsDocument() {
        return getData(SUBJECTS_XML);
    }

    @Override
    public void create(Subject subject) {
        Element subjectElement = getItemToXML(
                document,
                getSubjectXmlDomList(),
                subject.getSubjectItems(),
                subject.getId(),
                MAIN_ELEMENT,
                subject.getGroupsIds(),
                "group");
        create(document, subjectElement, SUBJECTS_XML);
    }

    @Override
    public void setSubjectCapacity(Subject subject, int newCapacity) {
        setElement(
                document,
                subject.getId(),
                "capacity_classroom",
                String.valueOf(newCapacity),
                SUBJECTS_XML,
                MAIN_ELEMENT);

        new WorkLabelManagerImpl().generateWorkLabelsAfterUpgrade(getSubject(subject.getId()));

    }

    @Override
    public void remove(Subject subject) {
        remove(document, subject.getId(), MAIN_ELEMENT, SUBJECTS_XML);
    }

    @Override
    public List<Subject> getAllSubject() {
//        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);
        List<Subject> subjects = new ArrayList<>();

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                subjects.add(getSubjectFromXML(element));
            }
        }
        return subjects;
    }


    @Override
    public Subject getSubjectByAcronym(String acronym) {
        return null;
    }

    @Override
    public Subject getSubject(UUID id) {
//        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (UUID.fromString(element.getAttribute("id")) == id) {
                    return getSubjectFromXML(element);
                }
            }
        }
        return null;
    }

    private Subject getSubjectFromXML(Element element) {
        return new Subject(
                UUID.fromString(element.getAttribute("id")),
                element.getElementsByTagName("name").item(0).getTextContent(),
                element.getElementsByTagName("acronym").item(0).getTextContent(),
                element.getElementsByTagName("teacher").item(0).getTextContent(),
                Integer.parseInt(element.getElementsByTagName("capacity_lecture").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("capacity_seminar").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("capacity_exercise").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("capacity_classroom").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("number_of_weeks").item(0).getTextContent()),
                Completion.valueOf(element.getElementsByTagName("completion").item(0).getTextContent()),
                Language.valueOf(element.getElementsByTagName("language").item(0).getTextContent()),
                getGroups((Element) element.getElementsByTagName("groups").item(0)));

    }

    private List<Group> getGroups(Element element) {
        List<Group> groups = new ArrayList<>();
        NodeList nodeList = element.getElementsByTagName("group");

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element group = (Element) node;
                groups.add(new GroupManagerImpl().getGroup(UUID.fromString(group.getAttribute("id"))));
            }
        }
        return groups;
    }


    @Override
    public void removeGroupFromSubjects(Group group) {
        List<Subject> currentSubjects = getAllSubject()
                .stream()
                .filter(subject -> subject.getGroups().contains(group))
                .toList();
        for (Subject subject: currentSubjects) {
            remove(subject);
            List<Group> oldGroup = subject.getGroups();
            oldGroup.remove(group);
            subject.setGroups(oldGroup);
            create(subject);
        }
    }

    private List<String> getSubjectXmlDomList() {
        return Arrays.asList(
                "name",
                "acronym",
                "teacher",
                "capacity_lecture",
                "capacity_seminar",
                "capacity_exercise",
                "capacity_classroom",
                "number_of_weeks",
                "completion",
                "language",
                "groups");
    }

}
