package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
import fai.utb.db.entity.entityEnum.*;
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
 * Implementation of abstract class {@link SubjectManager}
 *
 * @author Šimon Zouvala
 */
public class SubjectManagerImpl extends SubjectManager {

    private final Document document;
    private static final String MAIN_ELEMENT = "subject";

    /**
     * Constructor for get current DOM of Subjects.xml.
     */
    public SubjectManagerImpl() {
        this.document = getSubjectsDocument();
    }

    /**
     * Constructor for get current DOM of relevant XML file.
     */
    public SubjectManagerImpl(String xml) {
        this.document = getData(xml);
    }

    private Document getSubjectsDocument() {
        return getData(SUBJECTS_XML);
    }

    @Override
    public void create(Subject subject) {
        validate(subject);

        if (isSameSubjectInXml(subject)) {
            throw new ValidationException(
                    "Subject " + subject.getName() + " (" + subject.getAcronym() + ")"
                            + " with teacher " + subject.getTeacher() + ", completion " + subject.getCompletion()
                            + ", language " + subject.getLanguage() + " and number of weeks "
                            + subject.getNumberOfWeeks() + " is already exist.");
        }

        Element subjectElement = getItemToXML(
                document,
                getSubjectXmlDomList(),
                subject.getSubjectItems(),
                MAIN_ELEMENT,
                subject.getGroupsIds(),
                "group");
        create(document, subjectElement, SUBJECTS_XML);
    }

    private void validate(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("subject is null");
        }
        if (subject.getName() == null) {
            throw new ValidationException("Name is not set");
        }
        if (subject.getAcronym() == null) {
            throw new ValidationException("Acronym is not set");
        }
        if (subject.getTeacher() == null) {
            throw new ValidationException("Teacher is not set");
        }
        if (subject.getLectureCapacity() < 0) {
            throw new ValidationException("Lecture capacity is negative");
        }
        if (subject.getSeminarCapacity() < 0) {
            throw new ValidationException("Seminar capacity is negative");
        }
        if (subject.getExerciseCapacity() < 0) {
            throw new ValidationException("Exercise capacity is negative");
        }
        if (subject.getNumberOfWeeks() < 0) {
            throw new ValidationException("Number of weeks capacity is negative");
        }
        if (subject.getCompletion() == null) {
            throw new ValidationException("Completion is not set");
        }
        if (subject.getClassroomCapacity() < 0) {
            throw new ValidationException("Classroom capacity is negative");
        }
        if (subject.getLanguage() == null) {
            throw new ValidationException("Language is not set");
        }
        if (subject.getGroups() == null || subject.getGroups().size() == 0) {
            throw new ValidationException("Group is zero or not set");
        }


    }

    private boolean isSameSubjectInXml(Subject newSubject) {
        List<Subject> subjects = getAllSubject();

        for (Subject oldSubject : subjects) {
            if (oldSubject.equals(newSubject)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setSubjectCapacity(Subject subject, int newCapacity) {
        if (newCapacity < 0) {
            throw new ValidationException("new capacity to set is negative");
        }
        validate(subject);
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
        validate(subject);
        remove(document, subject.getId(), MAIN_ELEMENT, SUBJECTS_XML);
    }

    @Override
    public List<Subject> getAllSubject() {
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
    public Subject getSubject(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Employee id is null");
        }
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (UUID.fromString(element.getAttribute("id")).equals(id)) {
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

    //    @Override
//    public void removeGroupFromSubjects(Group group) {
//        List<Subject> currentSubjects = getAllSubject()
//                .stream()
//                .filter(subject -> subject.getGroups().contains(group))
//                .toList();
//        for (Subject subject : currentSubjects) {
//            remove(subject);
//            List<Group> oldGroup = subject.getGroups();
//            oldGroup.remove(group);
//            subject.setGroups(oldGroup);
//            create(subject);
//        }
//    }
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
