package fai.utb.db.entity;

import fai.utb.db.entity.entityEnum.Language;

import java.util.*;

/**
 * Class represent all item of Group entity
 *
 * @author Šimon Zouvala
 */
public class Employee {

    private UUID id;
    private String name;
    private final String surname;
    private final String phone;
    private final String email;
    private final Double jobTime;
    private final Boolean isEmployee;
    private double workPoint;
    private double workPointWithoutEN;
    private List<WorkLabel> workLabels = new ArrayList<>();

    public Employee(UUID id, String name, String surname, String phone, String email, Double jobTime,
                    Boolean isEmployee, double workPoint, double workPointWithoutEN, List<WorkLabel> workLabels) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.jobTime = jobTime;
        this.isEmployee = isEmployee;
        this.workPoint = workPoint;
        this.workPointWithoutEN = workPointWithoutEN;
        this.workLabels = workLabels;
    }

    public Employee(String name, String surname, String phone, String email, Double jobTime,
                    Boolean isEmployee) {

        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.jobTime = jobTime;
        this.isEmployee = isEmployee;
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

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Double getJobTime() {
        return jobTime;
    }

    public Boolean getIsEmployee() {
        return isEmployee;
    }

    public double getWorkPoint() {
        workPoint = 0.0;

        for (WorkLabel workLabel : workLabels) {
            workPoint += workLabel.getPoints();
        }
        return workPoint;
    }

    public double getWorkPointWithoutEN() {
        workPointWithoutEN = 0.0;

        for (WorkLabel workLabel : workLabels) {
            if (workLabel.getLanguage() == Language.CZ) {
                workPointWithoutEN += workLabel.getPoints();
            }
        }
        return workPointWithoutEN;
    }

    public List<String> getEmployeeItems() {
        return Arrays.asList(
                getName(),
                getSurname(),
                getPhone(),
                getEmail(),
                getJobTime().toString(),
                getIsEmployee().toString(),
                String.valueOf(getWorkPoint()),
                String.valueOf(getWorkPointWithoutEN()),
                "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee that = (Employee) o;
        return Objects.equals(name, that.name) && Objects.equals(surname, that.surname)
                && Objects.equals(phone, that.phone) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, phone, email, jobTime, isEmployee);
    }

    @Override
    public String toString() {
        return "---Zaměstnanec---: \n" +
                "Jméno: \t\t " + name + " " + surname + " \n" +
                "Tel.: \t\t " + phone + " \n" +
                "Email: \t\t " + email + " \n" +
                "Úvazek: \t\t " + jobTime + " \n" +
                "Zaměstnanec: \t " + isEmployee + " \n" +
                "Body: \t\t " + getWorkPoint() + " \n" +
                "Body bez EN: \t\t " + getWorkPointWithoutEN() + " \n";
    }

    public String toStringOnlyName(){
        return "---Zaměstnanec---: \n" +
                "Jméno: \t\t " + name + " " + surname + " \n";
    }
}
