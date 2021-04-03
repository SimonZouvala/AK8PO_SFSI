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

public class SubjectManagerImpl extends SubjectManager {

    private final GroupManagerImpl groupManager;
    private Document document;
    private static final String MAIN_ELEMENT = "subject";


    public SubjectManagerImpl() {
        this.document = getSubjectsDocument();
        groupManager = new GroupManagerImpl();
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
                "capacity-classroom",
                String.valueOf(newCapacity),
                SUBJECTS_XML,
                MAIN_ELEMENT);
    }

    @Override
    public void remove(Subject subject) {
        remove(document, subject.getId(), MAIN_ELEMENT, SUBJECTS_XML);
    }

    @Override
    public List<Subject> getAllSubject() {
        System.out.println("In XML are " + document.getDocumentElement().getTagName());
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
    public Subject getSubject(Long id) {
        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (Long.parseLong(element.getAttribute("id")) == id) {
                    return getSubjectFromXML(element);
                }
            }
        }
        return null;
    }

    private Subject getSubjectFromXML(Element element) {
        return new Subject(
                Long.parseLong(element.getAttribute("id")),
                element.getElementsByTagName("name").item(0).getTextContent(),
                element.getElementsByTagName("acronym").item(0).getTextContent(),
                element.getElementsByTagName("teacher").item(0).getTextContent(),
                Integer.parseInt(element.getElementsByTagName("capacity-lecture").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("capacity-seminar").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("capacity-exercise").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("capacity-classroom").item(0).getTextContent()),
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
                groups.add(groupManager.getGroup(Long.parseLong(group.getAttribute("id"))));
            }
        }
        return groups;
    }

    private List<String> getSubjectXmlDomList() {
        return Arrays.asList(
                "name",
                "surname",
                "phone",
                "email",
                "jobtime",
                "isemployee",
                "workpoint",
                "workpoint_without_en",
                "worklabels");
//        List<String> subjectItems = subject.getSubjectItems();
//        Element subjectElement = document.createElement("subject");
//        subjectElement.setAttribute("id", subject.getId().toString());
//
//
//        for (int index = 0; index < xmlDom.size(); index++) {
//            Element newElement = document.createElement(xmlDom.get(index));
//
//            if (index == xmlDom.size() - 1) {
//                if (subject.getGroupList().size() > 0) {
//
//                    for (Group group : subject.getGroupList()) {
//                        Element label = document.createElement("group");
//                        label.setAttribute("id", group.getId().toString());
//                        newElement.appendChild(label);
//                    }
//                    subjectElement.appendChild(newElement);
//                }
//            }else {
//                newElement.appendChild(document.createTextNode(subjectItems.get(index)));
//                subjectElement.appendChild(newElement);
//            }
//        }
//        return subjectElement;
//

    }

}
