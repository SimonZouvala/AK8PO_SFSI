package fai.utb.db.manager;

import fai.utb.db.entity.*;
import fai.utb.db.entity.entityEnum.Completion;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.LessonType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WorkLabelManagerImpl extends WorkLabelManager {

    private final Document document;
    private final PointWeights pointWeights;
    private static final String MAIN_ELEMENT = "worklabel";

    public WorkLabelManagerImpl() {
        this.document = getWorklabelsDocument();
        this.pointWeights = getPointWeights();
    }

    private PointWeights getPointWeights() {
        Document pointDocument = getData("PointWeights.xml");
        XPath xPath = XPathFactory.newInstance().newXPath();
        PointWeights newPointWeights = null;

        try {
            newPointWeights = new PointWeights(
                    Double.parseDouble(
                            xPath.compile("/points/hour/lecture")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()),
                    Double.parseDouble(
                            xPath.compile("/points/hour/seminar")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()),
                    Double.parseDouble(
                            xPath.compile("/points/hour/exercise")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()),
                    Double.parseDouble(
                            xPath.compile("/points/completions/z")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()),
                    Double.parseDouble(
                            xPath.compile("/points/completions/kl")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()),
                    Double.parseDouble(
                            xPath.compile("/points/completions/zk")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()),
                    Double.parseDouble(
                            xPath.compile("/points/en/hour/lecture")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()),
                    Double.parseDouble(
                            xPath.compile("/points/en/hour/seminar")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()),
                    Double.parseDouble(
                            xPath.compile("/points/en/hour/exercise")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()),
                    Double.parseDouble(
                            xPath.compile("/points/en/completions/z")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()),
                    Double.parseDouble(
                            xPath.compile("/points/en/completions/kl")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()),
                    Double.parseDouble(
                            xPath.compile("/points/en/completions/zk")
                                    .evaluate(pointDocument, XPathConstants.STRING).toString()));
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return newPointWeights;
    }

    @Override
    public void generateWorkLabels() {
        List<WorkLabel> allWorkLabels = getAllWorkLabels();
        List<Subject> allSubjects = new SubjectManagerImpl().getAllSubject();

        for (Subject subject : allSubjects) {
            int totalNumberOfStudentsInSubject = 0;

            for (Group group : subject.getGroups()) {
                totalNumberOfStudentsInSubject += group.getQuantity();
            }

            int numberOfLabels =
                    (int) Math.ceil((double) totalNumberOfStudentsInSubject / subject.getClassroomCapacity());

            List<WorkLabel> currentWorkLabels =
                    allWorkLabels
                            .stream()
                            .filter(workLabel -> subject.equals(workLabel.getSubject()))
                            .toList();

            if (subject.getLectureCapacity() > 0) {

                checkOrCreateWorkLabelsByLessonType(
                        currentWorkLabels,
                        subject,
                        LessonType.LECTURE,
                        1,
                        totalNumberOfStudentsInSubject);
            }
            if (subject.getSeminarCapacity() > 0) {

                checkOrCreateWorkLabelsByLessonType(
                        currentWorkLabels,
                        subject,
                        LessonType.SEMINAR,
                        numberOfLabels,
                        totalNumberOfStudentsInSubject);
            }
            if (subject.getExerciseCapacity() > 0) {

                checkOrCreateWorkLabelsByLessonType(
                        currentWorkLabels,
                        subject,
                        LessonType.EXERCISE,
                        numberOfLabels,
                        totalNumberOfStudentsInSubject);
            }

            checkOrCreateWorkLabelsByCompletion(
                    currentWorkLabels,
                    subject,
                    subject.getCompletion(),
                    totalNumberOfStudentsInSubject);
        }
    }

    private int getNumberOfLabelsByLessonType(List<WorkLabel> workLabels, LessonType lessonType) {
        return workLabels
                .stream()
                .filter(workLabel -> lessonType.equals(workLabel.getLessonType()))
                .toList()
                .size();
    }

    private int getNumberOfLabelsByCompletion(List<WorkLabel> workLabels, Completion completion) {
        return workLabels
                .stream()
                .filter(workLabel -> completion.equals(workLabel.getCompletion()))
                .toList()
                .size();
    }

    private void checkOrCreateWorkLabelsByLessonType(List<WorkLabel> workLabels, Subject subject,
                                                     LessonType lessonType, int numberOfLessonType,
                                                     int totalNumberOfStudents) {
        int numberOfLabels = getNumberOfLabelsByLessonType(workLabels, lessonType);
        int insertNumber = (int) Math.ceil((double) totalNumberOfStudents / (numberOfLessonType));

        if (numberOfLessonType != numberOfLabels) {
            int restNumberOfStudents = totalNumberOfStudents - insertNumber * numberOfLabels;

            for (int index = 0; index < numberOfLessonType - numberOfLabels; index++) {
                insertNumber =
                        (int) Math.ceil((double) restNumberOfStudents / (numberOfLessonType - numberOfLabels - index));
                WorkLabel newWorkLabel = new WorkLabel(
                        UUID.randomUUID(),
                        lessonType.toString() + " " + subject.getName() + " " + (numberOfLabels + index + 1),
                        subject,
                        subject.getLanguage(),
                        lessonType,
                        subject.getNumberOfWeeks());

                if (subject.getClassroomCapacity() >= restNumberOfStudents || lessonType.equals(LessonType.LECTURE)) {
                    newWorkLabel.setNumberOfStudents(restNumberOfStudents);
                } else {
                    newWorkLabel.setNumberOfStudents(insertNumber);
                }
                restNumberOfStudents -= insertNumber;

                newWorkLabel.setNumberOfHours(getNumberOfHoursByLessonType(newWorkLabel));
                newWorkLabel.setPoints(generatePoints(newWorkLabel));
                createWorkLabel(newWorkLabel);
            }
        }
    }

    private void checkOrCreateWorkLabelsByCompletion(List<WorkLabel> workLabels, Subject subject,
                                                     Completion completion, int totalNumberOfStudents) {
        int numberOfLabels = getNumberOfLabelsByCompletion(workLabels, completion);

        if (numberOfLabels == 0) {
            WorkLabel newWorkLabel = new WorkLabel(UUID.randomUUID(),
                    completion.toString() + " " + subject.getName(),
                    subject,
                    subject.getLanguage(),
                    totalNumberOfStudents,
                    completion);
            newWorkLabel.setPoints(generatePoints(newWorkLabel));
            createWorkLabel(newWorkLabel);
        }
    }

    @Override
    public void generateWorkLabelsAfterUpgrade(Group group) {
        List<Subject> subjects =
                new SubjectManagerImpl()
                        .getAllSubject()
                        .stream()
                        .filter(subject -> subject.getGroups().contains(group))
                        .toList();
        for (Subject subject : subjects) {
            generateWorkLabelsAfterUpgrade(subject);
        }
    }

    @Override
    public void generateWorkLabelsAfterUpgrade(Subject subject) {

        List<WorkLabel> currentWorkLabels = getAllWorkLabels()
                .stream()
                .filter(workLabel -> workLabel.getSubject().equals(subject))
                .toList();
        int totalNumberOfStudentsInSubject = 0;

        for (Group group : subject.getGroups()) {
            totalNumberOfStudentsInSubject += group.getQuantity();
        }

        int numberOfLabels =
                (int) Math.ceil((double) totalNumberOfStudentsInSubject / subject.getClassroomCapacity());

        System.out.println(numberOfLabels);
        changeNumberOfStudentsInWorkLabels(currentWorkLabels, totalNumberOfStudentsInSubject, numberOfLabels);
    }

    private void changeNumberOfStudentsInWorkLabels(List<WorkLabel> workLabels, int totalNumberOfStudents,
                                                    int numberOfLabels) {
        generateWorkLabels();
        changeNumberOfStudentsInWorkLabel(
                workLabels
                        .stream()
                        .filter(workLabel -> LessonType.LECTURE.equals(workLabel.getLessonType()))
                        .toList(),
                totalNumberOfStudents);
        changeNumberOfStudentsInWorkLabel(
                workLabels
                        .stream()
                        .filter(workLabel -> LessonType.SEMINAR.equals(workLabel.getLessonType()))
                        .toList(),
                totalNumberOfStudents,
                numberOfLabels);
        changeNumberOfStudentsInWorkLabel(
                workLabels
                        .stream()
                        .filter(workLabel -> LessonType.EXERCISE.equals(workLabel.getLessonType()))
                        .toList(),
                totalNumberOfStudents,
                numberOfLabels);
        changeNumberOfStudentsInWorkLabel(
                workLabels
                        .stream()
                        .filter(workLabel -> workLabel.getLessonType() == null)
                        .toList(),
                totalNumberOfStudents);
    }

    private void changeNumberOfStudentsInWorkLabel(List<WorkLabel> workLabels, int totalNumberOfStudents,
                                                   int numberOfLabels) {
        int restNumberOfStudents = totalNumberOfStudents;
        for (int index = 0; index < workLabels.size(); index++) {
            int insetNumberOfStudents = (int) Math.ceil((double) restNumberOfStudents / (numberOfLabels - index));

            setElement(
                    document,
                    workLabels.get(index).getId(),
                    "number_of_students",
                    String.valueOf(insetNumberOfStudents),
                    WORK_LABELS_XML,
                    MAIN_ELEMENT);
            restNumberOfStudents -= insetNumberOfStudents;
            System.out.println(insetNumberOfStudents + "  zbývá  " + restNumberOfStudents + "    ze   " + totalNumberOfStudents);
        }
    }

    private void changeNumberOfStudentsInWorkLabel(List<WorkLabel> workLabels, int totalNumberOfStudents) {

        for (WorkLabel workLabel : workLabels) {
            setElement(
                    document,
                    workLabel.getId(),
                    "number_of_students",
                    String.valueOf(totalNumberOfStudents),
                    WORK_LABELS_XML,
                    MAIN_ELEMENT);
        }
    }

    @Override
    public void createWorkLabel(WorkLabel workLabel) {
        Element workLabelElement = getItemToXML(
                document,
                getWorkLabelXmlDomList(),
                workLabel.getWorklabelsItemsOrIds(),
                workLabel.getId(),
                MAIN_ELEMENT);
        create(document, workLabelElement, WORK_LABELS_XML);
    }

    @Override
    public void removeWorkLabels(WorkLabel workLabel) {
        remove(document, workLabel.getId(), MAIN_ELEMENT, "WorkLabels");
    }

    @Override
    public void addEmployeeToWorkLabel(Employee employee, WorkLabel workLabel) {
        setElement(
                document,
                workLabel.getId(),
                "employee",
                String.valueOf(employee.getId()),
                WORK_LABELS_XML,
                MAIN_ELEMENT);
    }

    @Override
    public void removeEmployeeFromWorkLabel(WorkLabel workLabel) {
        setElement(
                document,
                workLabel.getId(),
                "employee",
                "",
                WORK_LABELS_XML,
                MAIN_ELEMENT);
    }

    @Override
    public List<WorkLabel> getAllWorkLabels() {
//        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);
        List<WorkLabel> workLabels = new ArrayList<>();

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                workLabels.add(getWorkLabelFromXML(element));
            }
        }
        return workLabels;
    }


    @Override
    public WorkLabel getWorkLabel(UUID id) {
//        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (UUID.fromString(element.getAttribute("id")) == id) {
                    return getWorkLabelFromXML(element);
                }
            }
        }
        return null;
    }

    private WorkLabel getWorkLabelFromXML(Element element) {

        WorkLabel workLabel = new WorkLabel(
                UUID.fromString(element.getAttribute("id")),
                element.getElementsByTagName("name").item(0).getTextContent(),
                Language.valueOf(element.getElementsByTagName("language").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("number_of_students").item(0).getTextContent()));

        workLabel.setEmployeeId(getEmployeeIdFromXml(element));
        workLabel.setSubject(getSubjectFormXml(element));
        workLabel.setLessonType(getLessonTypeFromXML(element));
        workLabel.setCompletion(getCompletionFromXML(element));
        workLabel.setNumberOfWeeks(getNumberOfWeeks(workLabel, element));
        workLabel.setNumberOfHours(getNumberOfHours(workLabel, element));
        workLabel.setPoints(getPointsFromXML(workLabel, element));

        return workLabel;
    }

    private UUID getEmployeeIdFromXml(Element element) {
        String employeeId = element.getElementsByTagName("employee").item(0).getTextContent();
        if (employeeId.equals("")) {
            return null;
        }
        return UUID.fromString(employeeId);
    }

    private Subject getSubjectFormXml(Element element) {
        String subject = element.getElementsByTagName("subject").item(0).getTextContent();
        if (subject.equals("")) {
            return null;
        }
        return new SubjectManagerImpl().getSubject(
                UUID.fromString(
                        element.getElementsByTagName("subject").item(0).getTextContent()));
    }


    private double getPointsFromXML(WorkLabel workLabel, Element element) {
        LessonType lessonType = workLabel.getLessonType();
        Completion completion = workLabel.getCompletion();

        if (lessonType == null && completion == null) {
            return Integer.parseInt(element.getElementsByTagName("point").item(0).getTextContent());
        }
        return generatePoints(workLabel);
    }


    private double generatePoints(WorkLabel workLabel) {
        LessonType lessonType = workLabel.getLessonType();
        Completion completion = workLabel.getCompletion();

        if (workLabel.getLanguage().equals(Language.EN)) {
            if (lessonType == null) {
                return getEnCompletionValue(completion) * workLabel.getNumberOfStudents();
            }
            return getEnLessonTypeValue(lessonType) * workLabel.getNumberOfHours() * workLabel.getNumberOfWeeks();
        }

        if (lessonType == null) {
            return getCompletionValue(completion) * workLabel.getNumberOfStudents();
        }
        return getLessonTypeValue(lessonType) * workLabel.getNumberOfHours() * workLabel.getNumberOfWeeks();
    }


    private LessonType getLessonTypeFromXML(Element element) {
        String lessonTypeString = element.getElementsByTagName("lesson_type").item(0).getTextContent();
        if (lessonTypeString.equals("")) {
            return null;
        }
        return LessonType.valueOf(lessonTypeString);
    }

    private Completion getCompletionFromXML(Element element) {
        String completionString = element.getElementsByTagName("completion").item(0).getTextContent();
        if (completionString.equals("")) {
            return null;
        }
        return Completion.valueOf(completionString);
    }

    private int getNumberOfWeeks(WorkLabel workLabel, Element element) {
        if (workLabel.getSubject() == null) {
            return Integer.parseInt(element.getElementsByTagName("number_of_weeks").item(0).getTextContent());
        }
        return workLabel.getSubject().getNumberOfWeeks();
    }

    private int getNumberOfHours(WorkLabel workLabel, Element element) {
        if (workLabel.getSubject() == null) {
            return Integer.parseInt(element.getElementsByTagName("number_of_hours").item(0).getTextContent());
        }
        return getNumberOfHoursByLessonType(workLabel);
    }

    private int getNumberOfHoursByLessonType(WorkLabel workLabel) {
        LessonType lessonType = workLabel.getLessonType();
        if (LessonType.LECTURE.equals(lessonType)) {
            return workLabel.getSubject().getLectureCapacity();
        }
        if (LessonType.SEMINAR.equals(lessonType)) {
            return workLabel.getSubject().getSeminarCapacity();
        }
        if (LessonType.EXERCISE.equals(lessonType)) {
            return workLabel.getSubject().getExerciseCapacity();
        }
        return 0;
    }


    private double getEnCompletionValue(Completion completion) {
        if (completion.equals(Completion.Z)) {
            return pointWeights.getCreditEn();
        } else if (completion.equals(Completion.KL)) {
            return pointWeights.getGradedCreditEn();
        }
        return pointWeights.getExaminationEN();
    }

    private double getCompletionValue(Completion completion) {
        if (completion.equals(Completion.Z)) {
            return pointWeights.getCredit();
        } else if (completion.equals(Completion.KL)) {
            return pointWeights.getGradedCredit();
        }
        return pointWeights.getExamination();
    }


    private double getEnLessonTypeValue(LessonType lessonType) {
        if (lessonType.equals(LessonType.LECTURE)) {
            return pointWeights.getLectureEn();
        } else if (lessonType.equals(LessonType.SEMINAR)) {
            return pointWeights.getSeminarEn();
        }
        return pointWeights.getExerciseEn();
    }

    private double getLessonTypeValue(LessonType lessonType) {
        if (lessonType.equals(LessonType.LECTURE)) {
            return pointWeights.getLecture();
        } else if (lessonType.equals(LessonType.SEMINAR)) {
            return pointWeights.getSeminar();
        }
        return pointWeights.getExercise();
    }

    private List<String> getWorkLabelXmlDomList() {
        return Arrays.asList(
                "name",
                "employee",
                "subject",
                "language",
                "point",
                "number_of_students",
                "lesson_type",
                "completion",
                "number_of_weeks",
                "number_of_hours");
    }
}
