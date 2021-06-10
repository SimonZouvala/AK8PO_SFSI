package fai.utb.db.entity;

import fai.utb.db.entity.entityEnum.Completion;
import fai.utb.db.entity.entityEnum.Language;

import java.util.*;

/**
 * Class represent all item of Subject entity.
 *
 * @author Šimon Zouvala
 */
public class Subject {


    private UUID id;
    private final String acronym;
    private String name;
    private final String teacher;
    private final int lectureCapacity;
    private final int seminarCapacity;
    private final int exerciseCapacity;
    private final int numberOfWeeks;
    private Completion completion;
    private final int classroomCapacity;
    private Language language;
    private List<Group> groups;

    public Subject(UUID id, String acronym, String name, String teacher, int lectureCapacity, int seminarCapacity,
                   int exerciseCapacity, int numberOfWeeks, Completion completion, int classroomCapacity,
                   Language language) {
        this.id = id;
        this.acronym = acronym;
        this.name = name;
        this.teacher = teacher;
        this.lectureCapacity = lectureCapacity;
        this.seminarCapacity = seminarCapacity;
        this.exerciseCapacity = exerciseCapacity;
        this.numberOfWeeks = numberOfWeeks;
        this.completion = completion;
        this.classroomCapacity = classroomCapacity;
        this.language = language;
    }

    public Subject(UUID id, String name, String acronym, String teacher, int lectureCapacity, int seminarCapacity,
                   int exerciseCapacity, int classroomCapacity, int numberOfWeeks, Completion completion,
                   Language language, List<Group> groups) {
        this.id = id;
        this.acronym = acronym;
        this.name = name;
        this.teacher = teacher;
        this.lectureCapacity = lectureCapacity;
        this.seminarCapacity = seminarCapacity;
        this.exerciseCapacity = exerciseCapacity;
        this.numberOfWeeks = numberOfWeeks;
        this.completion = completion;
        this.groups = groups;
        this.classroomCapacity = classroomCapacity;
        this.language = language;
    }

    public Subject(String acronym, String name, String teacher, int lectureCapacity, int seminarCapacity,
                   int exerciseCapacity, int numberOfWeeks, Completion completion, int classroomCapacity,
                   Language language, List<Group> groups) {
        this.acronym = acronym;
        this.name = name;
        this.teacher = teacher;
        this.lectureCapacity = lectureCapacity;
        this.seminarCapacity = seminarCapacity;
        this.exerciseCapacity = exerciseCapacity;
        this.numberOfWeeks = numberOfWeeks;
        this.completion = completion;
        this.classroomCapacity = classroomCapacity;
        this.language = language;
        this.groups = groups;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getLectureCapacity() {
        return lectureCapacity;
    }

    public int getSeminarCapacity() {
        return seminarCapacity;
    }

    public int getExerciseCapacity() {
        return exerciseCapacity;
    }

    public int getNumberOfWeeks() {
        return numberOfWeeks;
    }

    public Completion getCompletion() {
        return completion;
    }

    public void setCompletion(Completion completion) {
        this.completion = completion;
    }

    public int getClassroomCapacity() {
        return classroomCapacity;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<UUID> getGroupsIds() {
        List<UUID> ids = new ArrayList<>();
        for (Group group : getGroups()) {
            ids.add(group.getId());
        }
        return ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(acronym, subject.acronym) && Objects.equals(name, subject.name) &&
                Objects.equals(teacher, subject.teacher) && completion == subject.completion &&
                language == subject.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(acronym, name, teacher, completion, language);
    }

    public List<String> getSubjectItems() {
        return Arrays.asList(
                getName(),
                getAcronym(),
                getTeacher(),
                String.valueOf(getLectureCapacity()),
                String.valueOf(getSeminarCapacity()),
                String.valueOf(getExerciseCapacity()),
                String.valueOf(getClassroomCapacity()),
                String.valueOf(getNumberOfWeeks()),
                getCompletion().toString(),
                getLanguage().toString());
    }

    @Override
    public String toString() {
        return "---Předmět--- \n" +
                "Název: \t\t " + name + " \n" +
                "Zkratka: \t\t " + acronym + " \n" +
                "Vyučující: \t\t " + teacher + " \n" +
                "Kapacity:\n" +
                " Přednáška: \t\t " + lectureCapacity + " \n" +
                " Seminář: \t\t " + seminarCapacity + " \n" +
                " Cvičení: \t\t " + exerciseCapacity + " \n" +
                "Počet týdnů: \t\t " + numberOfWeeks + " \n" +
                "Ukončení: \t\t " + completion + " \n" +
                "Velikost třídy: \t " + classroomCapacity + " \n" +
                "Jazyk: \t\t " + language + " \n" +
                groups + " \n";

    }

    public String toStringWithoutGroups(){
        return "---Předmět--- \n" +
                "Název: \t\t " + name + " \n" +
                "Zkratka: \t\t " + acronym + " \n" +
                "Vyučující: \t\t " + teacher + " \n" +
                "Kapacity:\n" +
                " Přednáška:\t\t  " + lectureCapacity + " \n" +
                " Seminář: \t\t " + seminarCapacity + " \n" +
                " Cvičení: \t\t " + exerciseCapacity + " \n" +
                "Počet týdnů: \t\t " + numberOfWeeks + " \n" +
                "Ukončení: \t\t " + completion + " \n" +
                "Velikost třídy: \t " + classroomCapacity + " \n" +
                "Jazyk: \t\t " + language + " \n";
    }
}
