package fai.utb.db.entity;

import fai.utb.db.entity.entityEnum.Degree;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.Semester;
import fai.utb.db.entity.entityEnum.FormOfStudy;

import java.util.Objects;

public class Group {

    public Long id;
    public Degree degree;
    public String fieldOfStudy;
    public FormOfStudy formOfStudy;
    public Semester semester;
    public int grade;
    public int quantity;
    public Language language;

    public Group(Degree degree, String fieldOfStudy, FormOfStudy formOfStudy, Semester semester, int grade, int quantity, Language language) {
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.formOfStudy = formOfStudy;
        this.semester = semester;
        this.grade = grade;
        this.quantity = quantity;
        this.language = language;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public FormOfStudy getFormOfStudy() {
        return formOfStudy;
    }

    public void setFormOfStudy(FormOfStudy formOfStudy) {
        this.formOfStudy = formOfStudy;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return grade == group.grade && quantity == group.quantity && degree == group.degree && Objects.equals(fieldOfStudy, group.fieldOfStudy) && formOfStudy == group.formOfStudy && semester == group.semester && language == group.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, fieldOfStudy, formOfStudy, semester, grade, quantity, language);
    }
}
