package fai.utb.db.entity;

import fai.utb.db.entity.entityEnum.Completion;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.LessonType;
import fai.utb.db.manager.EmployeeManagerImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Šimon Zouvala
 */
public class WorkLabel {

    private UUID id;
    private String name;
    private UUID employeeId;
    private Subject subject;
    private Language language;
    private double points;
    private int numberOfStudents;
    private LessonType lessonType;
    private Completion completion;
    private int numberOfWeeks;
    private int numberOfHours;

    public WorkLabel(UUID id, String name, Language language, int numberOfStudents) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.numberOfStudents = numberOfStudents;
    }

    public WorkLabel(UUID id, String name, Subject subject, Language language,
                     LessonType lessonType, int numberOfWeeks) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.language = language;
        this.lessonType = lessonType;
        this.numberOfWeeks = numberOfWeeks;
    }

    public WorkLabel(UUID id, String name, Subject subject, Language language,
                     int numberOfStudents, Completion completion) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.language = language;
        this.numberOfStudents = numberOfStudents;
        this.completion = completion;
    }

    public WorkLabel(String name, Language language, Double points, int numberOfStudents, LessonType lessonType,
                     Completion completion, int numberOfWeeks, int numberOfHours) {

        this.name = name;
        this.language = language;
        this.points = points;
        this.numberOfStudents = numberOfStudents;
        this.lessonType = lessonType;
        this.completion = completion;
        this.numberOfWeeks = numberOfWeeks;
        this.numberOfHours = numberOfHours;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public Completion getCompletion() {
        return completion;
    }

    public void setCompletion(Completion completion) {
        this.completion = completion;
    }

    public int getNumberOfWeeks() {
        return numberOfWeeks;
    }

    public void setNumberOfWeeks(int numberOfWeeks) {
        this.numberOfWeeks = numberOfWeeks;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public List<String> getWorklabelsItemsOrIds() {
        return Arrays.asList(
                getName(),
                getEmployeeId() == null ? "" : getEmployeeId().toString(),
                getSubject() == null ? "" : String.valueOf(getSubject().getId()),
                getLanguage().toString(),
                String.valueOf(getPoints()),
                String.valueOf(getNumberOfStudents()),
                getLessonType() == null ? "" : getLessonType().toString(),
                getCompletion() == null ? "" : getCompletion().toString(),
                String.valueOf(getNumberOfWeeks()),
                String.valueOf(getNumberOfHours()));

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkLabel workLabel = (WorkLabel) o;
        return Objects.equals(name, workLabel.name) && language == workLabel.language
                && lessonType == workLabel.lessonType && completion == workLabel.completion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, language);
    }

    @Override
    public String toString() {
        String employeeString = "";
        String subjectString = "";

        if (getEmployeeId() != null) {
            System.out.println(getEmployeeId());
            employeeString = new EmployeeManagerImpl().getEmployee(getEmployeeId()).toStringOnlyName();
        }
        if (getSubject() != null){
            subjectString = getSubject().toStringWithoutGroups();
        }
        return "---Pracovní štítek--- \n" +
                "Název: \t\t " + name + " \n" +
                "Jazky: \t\t " + language + " \n" +
                "Počet studentů: \t " + numberOfStudents + " \n" +
                "Druh výuky: \t\t " + lessonType + " \n" +
                "Ukončení: \t\t " + completion + " \n" +
                "Počet týdnů: \t\t " + numberOfWeeks + " \n" +
                "Počet hodin: \t\t " + numberOfHours + " \n" +
                "Body: \t\t " + points + " \n" +
                employeeString +
                 subjectString;
    }
}
