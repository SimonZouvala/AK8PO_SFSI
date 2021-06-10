package fai.utb.db.entity;

import fai.utb.db.entity.entityEnum.Degree;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.Semester;
import fai.utb.db.entity.entityEnum.FormOfStudy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Class represent all item of Group entity
 *
 * @author Šimon Zouvala
 */
public class Group {

    private UUID id;
    private final Degree degree;
    private final String fieldOfStudy;
    private final FormOfStudy formOfStudy;
    private final Semester semester;
    private final int grade;
    private final int quantity;
    private Language language;

    public Group(UUID id, Degree degree, String fieldOfStudy, FormOfStudy formOfStudy, Semester semester, int grade,
                 int quantity, Language language) {
        this.id = id;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.formOfStudy = formOfStudy;
        this.semester = semester;
        this.grade = grade;
        this.quantity = quantity;
        this.language = language;
    }

    public Group(Degree degree, String fieldOfStudy, FormOfStudy formOfStudy, Semester semester, int grade,
                 int quantity, Language language) {
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.formOfStudy = formOfStudy;
        this.semester = semester;
        this.grade = grade;
        this.quantity = quantity;
        this.language = language;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Degree getDegree() {
        return degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public FormOfStudy getFormOfStudy() {
        return formOfStudy;
    }

    public Semester getSemester() {
        return semester;
    }

    public int getGrade() {
        return grade;
    }

    public int getQuantity() {
        return quantity;
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
        return grade == group.grade && degree == group.degree
                && Objects.equals(fieldOfStudy, group.fieldOfStudy) && formOfStudy == group.formOfStudy
                && semester == group.semester && language == group.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, fieldOfStudy, formOfStudy, semester, grade, quantity, language);
    }

    public List<String> getGroupItems() {
        return Arrays.asList(
                getDegree().toString(),
                getFieldOfStudy(),
                getFormOfStudy().toString(),
                getSemester().toString(),
                String.valueOf(getGrade()),
                String.valueOf(getQuantity()),
                getLanguage().toString());
    }

    @Override
    public String toString() {
        return "---Skupina---: \n" +
                "Obor: \t\t " + fieldOfStudy + " \n" +
                "Stupeň: \t\t " + degree + " \n" +
                "Forma: \t\t " + formOfStudy + " \n" +
                "Semestr: \t\t " + semester + " \n" +
                "Ročník: \t\t " + grade + " \n" +
                "Počet studentů: \t " + quantity + " \n" +
                "Jazky: \t\t " + language + " \n";
    }
}
