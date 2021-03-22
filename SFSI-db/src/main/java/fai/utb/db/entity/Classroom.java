package fai.utb.db.entity;

import fai.utb.db.entity.entityEnum.Completion;
import fai.utb.db.entity.entityEnum.Language;

import java.util.List;
import java.util.Objects;

public class Classroom {

    private Long id;
    public String acronym;
    public String name;
    public String teacher;
    public int lectureCapacity;
    public int seminarCapacity;
    public int exerciseCapacity;
    public int numberOfWeeks;
    public Completion completion;
    public int classroomCapacity;
    public Language language;
    public List<Group> groupList = null;

    public Classroom(String acronym, String name, String teacher, int lectureCapacity, int seminarCapacity, int exerciseCapacity, int numberOfWeeks, Completion completion, int classroomCapacity, Language language) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classroom aClassroom = (Classroom) o;
        return lectureCapacity == aClassroom.lectureCapacity && seminarCapacity == aClassroom.seminarCapacity && exerciseCapacity == aClassroom.exerciseCapacity && numberOfWeeks == aClassroom.numberOfWeeks && classroomCapacity == aClassroom.classroomCapacity && Objects.equals(acronym, aClassroom.acronym) && Objects.equals(name, aClassroom.name) && Objects.equals(teacher, aClassroom.teacher) && completion == aClassroom.completion && language == aClassroom.language && Objects.equals(groupList, aClassroom.groupList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acronym, name, teacher, lectureCapacity, seminarCapacity, exerciseCapacity, numberOfWeeks, completion, classroomCapacity, language, groupList);
    }
}
