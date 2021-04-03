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

    private SubjectManager subjectManager;
    private Document document;
    private PointWeights pointWeights;
    private static final String MAIN_ELEMENT = "worklabel";

    public WorkLabelManagerImpl() {
        this.document = getWorklabelsDocument();
        this.pointWeights = getPointWeights();
        subjectManager = new SubjectManagerImpl();
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
        List<Subject> subjectList = subjectManager.getAllSubject();

        int numberOfLectures = 0;
        int numberOfSeminars = 0;
        int numberOfExercises = 0;

        for (Subject subject : subjectList) {
            int totalNumberOfStudentsInSubject = 0;

            for (Group group : subject.getGroups()) {
                totalNumberOfStudentsInSubject += group.getQuantity();
            }

            List<WorkLabel> currentWorkLabels =
                    allWorkLabels
                            .stream()
                            .filter(workLabel -> workLabel.getSubject().equals(subject))
                            .toList();

            if (subject.getLectureCapacity() > 0) {
                numberOfLectures = 1;
                checkOrCreateWorkLabelsByLessonType(
                        currentWorkLabels,
                        subject,
                        LessonType.LECTURE,
                        numberOfLectures,
                        totalNumberOfStudentsInSubject);
            }
            if (subject.getSeminarCapacity() > 0) {
                numberOfSeminars = (int) Math.ceil(
                        (double) totalNumberOfStudentsInSubject / subject.getClassroomCapacity());
                checkOrCreateWorkLabelsByLessonType(
                        currentWorkLabels,
                        subject,
                        LessonType.SEMINAR,
                        numberOfSeminars,
                        totalNumberOfStudentsInSubject);
            }
            if (subject.getExerciseCapacity() > 0) {
                numberOfExercises = (int) Math.ceil(
                        (double) totalNumberOfStudentsInSubject / subject.getClassroomCapacity());
                checkOrCreateWorkLabelsByLessonType(
                        currentWorkLabels,
                        subject,
                        LessonType.EXERCISE,
                        numberOfExercises,
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
                .filter(workLabel -> workLabel.getLessonType().equals(lessonType))
                .toList()
                .size();
    }

    private int getNumberOfLabelsByCompletion(List<WorkLabel> workLabels, Completion completion) {
        return workLabels
                .stream()
                .filter(workLabel -> workLabel.getCompletion().equals(completion))
                .toList()
                .size();
    }

    private void checkOrCreateWorkLabelsByLessonType(List<WorkLabel> workLabels, Subject subject,
                                                     LessonType lessonType, int numberOfLessonType,
                                                     int totalNumberOfStudents) {
        int numberOfLabels = getNumberOfLabelsByLessonType(workLabels, lessonType);


        if (numberOfLessonType != numberOfLabels) {
            totalNumberOfStudents = totalNumberOfStudents - subject.getClassroomCapacity() * numberOfLabels;
            for (int index = 0; index < numberOfLessonType - numberOfLabels; index++) {

                WorkLabel newWorkLabel = new WorkLabel(
                        UUID.randomUUID().getMostSignificantBits(),
                        lessonType.toString() + " " + subject.getName() + " " + numberOfLabels,
                        subject,
                        subject.getLanguage(),
                        lessonType,
                        subject.getNumberOfWeeks());
                if (subject.getClassroomCapacity() >= totalNumberOfStudents || lessonType.equals(LessonType.LECTURE)) {
                    newWorkLabel.setNumberOfStudents(totalNumberOfStudents);
                } else {
                    int numberOfStudents = (int) Math.ceil(
                            (double) totalNumberOfStudents / (numberOfLessonType - numberOfLabels));
                    newWorkLabel.setNumberOfStudents(numberOfStudents);
                }
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
            WorkLabel newWorkLabel = new WorkLabel(UUID.randomUUID().getMostSignificantBits(),
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
    public void generateWorkLabelsAfterUpgrade() {
    }

    @Override
    public void createWorkLabel(WorkLabel workLabel) {
        Element worklabelElement = getItemToXML(
                document,
                getWorkLabelXmlDomList(),
                workLabel.getWorklabelsItemsOrIds(),
                workLabel.getId(),
                MAIN_ELEMENT);
        create(document, worklabelElement, WORK_LABELS_XML);
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
        System.out.println("In XML are " + document.getDocumentElement().getTagName());
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
    public WorkLabel getWorkLabel(Long id) {
        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (Long.parseLong(element.getAttribute("id")) == id) {
                    return getWorkLabelFromXML(element);
                }
            }
        }
        return null;
    }

    private WorkLabel getWorkLabelFromXML(Element element) {

        WorkLabel workLabel = new WorkLabel(
                Long.parseLong(element.getAttribute("id")),
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

    private Long getEmployeeIdFromXml(Element element) {
        String employeeId = element.getElementsByTagName("employee").item(0).getTextContent();
        if (employeeId.equals("")) {
            return null;
        }
        return Long.parseLong(employeeId);

    }

    private Subject getSubjectFormXml(Element element) {
        String subject = element.getElementsByTagName("subject").item(0).getTextContent();
        if (subject.equals("")) {
            return null;
        }
        return subjectManager.getSubject(
                Long.parseLong(
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

        if (lessonType.equals(LessonType.LECTURE)) {
            return workLabel.getSubject().getLectureCapacity();
        }
        if (lessonType.equals(LessonType.SEMINAR)) {
            return workLabel.getSubject().getSeminarCapacity();
        }
        if (lessonType.equals(LessonType.EXERCISE)) {
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
