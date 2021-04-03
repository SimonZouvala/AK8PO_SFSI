package fai.utb.db.entity;

import fai.utb.db.entity.entityEnum.Completion;
import fai.utb.db.entity.entityEnum.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Subject {


    private long id;
    private String acronym;
    private String name;
    private String teacher;
    private int lectureCapacity;
    private int seminarCapacity;
    private int exerciseCapacity;
    private int numberOfWeeks;
    private Completion completion;
    private int classroomCapacity;
    private Language language;
    private List<Group> groups = null;

    public Subject(long id, String acronym, String name, String teacher, int lectureCapacity, int seminarCapacity, int exerciseCapacity, int numberOfWeeks, Completion completion, int classroomCapacity, Language language) {
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

    public Subject(long id, String name, String acronym, String teacher, int lectureCapacity, int seminarCapacity, int exerciseCapacity, int classroomCapacity, int numberOfWeeks, Completion completion, Language language, List<Group> groups) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
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

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getLectureCapacity() {
        return lectureCapacity;
    }

    public void setLectureCapacity(int lectureCapacity) {
        this.lectureCapacity = lectureCapacity;
    }

    public int getSeminarCapacity() {
        return seminarCapacity;
    }

    public void setSeminarCapacity(int seminarCapacity) {
        this.seminarCapacity = seminarCapacity;
    }

    public int getExerciseCapacity() {
        return exerciseCapacity;
    }

    public void setExerciseCapacity(int exerciseCapacity) {
        this.exerciseCapacity = exerciseCapacity;
    }

    public int getNumberOfWeeks() {
        return numberOfWeeks;
    }

    public void setNumberOfWeeks(int numberOfWeeks) {
        this.numberOfWeeks = numberOfWeeks;
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

    public void setClassroomCapacity(int classroomCapacity) {
        this.classroomCapacity = classroomCapacity;
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

    public List<Long> getGroupsIds() {
        List<Long> ids = new ArrayList<>();
        for (Group group : getGroups()) {
            ids.add(group.getId());
        }
        return ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject aSubject = (Subject) o;
        return lectureCapacity == aSubject.lectureCapacity && seminarCapacity == aSubject.seminarCapacity && exerciseCapacity == aSubject.exerciseCapacity && numberOfWeeks == aSubject.numberOfWeeks && classroomCapacity == aSubject.classroomCapacity && Objects.equals(acronym, aSubject.acronym) && Objects.equals(name, aSubject.name) && Objects.equals(teacher, aSubject.teacher) && completion == aSubject.completion && language == aSubject.language && Objects.equals(groups, aSubject.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acronym, name, teacher, lectureCapacity, seminarCapacity, exerciseCapacity, numberOfWeeks, completion, classroomCapacity, language, groups);
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
                getCompletion().toString());
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", acronym='" + acronym + '\'' +
                ", name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", lectureCapacity=" + lectureCapacity +
                ", seminarCapacity=" + seminarCapacity +
                ", exerciseCapacity=" + exerciseCapacity +
                ", numberOfWeeks=" + numberOfWeeks +
                ", completion=" + completion +
                ", classroomCapacity=" + classroomCapacity +
                ", language=" + language +
                ", groups=" + groups +
                '}';
    }
}
