package fai.utb.db.entity;

import fai.utb.db.entity.entityEnum.Completion;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.LessonType;

import java.util.Objects;

public class WorkLabel {

    private Long id;
    private String name;
    private Employee employee;
    private Subject subject;
    private Language language;
    private int points;
    private int NumberOfStudents;
    private LessonType lessonType;
    private Completion completion;
    private int numberOfWeeks;
    private int numberOfHours;

    public WorkLabel(String name, Subject subject, Language language) {
        this.name = name;
        this.subject = subject;
        this.language = language;
    }

    public WorkLabel(String name, Language language, LessonType lessonType, int numberOfWeeks, int numberOfHours) {
        this.name = name;
        this.language = language;
        this.lessonType = lessonType;
        this.numberOfWeeks = numberOfWeeks;
        this.numberOfHours = numberOfHours;
    }

    public WorkLabel(String name, Language language, int numberOfStudents, Completion completion) {
        this.name = name;
        this.language = language;
        this.NumberOfStudents = numberOfStudents;
        this.completion = completion;
        this.numberOfHours = 0;
        this.numberOfWeeks = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Subject getClassroom() {
        return subject;
    }

    public void setClassroom(Subject subject) {
        this.subject = subject;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getNumberOfStudents() {
        return NumberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        NumberOfStudents = numberOfStudents;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkLabel workLabel = (WorkLabel) o;
        return Objects.equals(id, workLabel.id) && Objects.equals(name, workLabel.name) && language == workLabel.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, language);
    }
}
