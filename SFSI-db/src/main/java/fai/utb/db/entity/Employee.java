package fai.utb.db.entity;

import fai.utb.db.entity.entityEnum.Language;

import java.util.*;

/**
 * @author Šimon Zouvala
 */
public class Employee {

    private UUID id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private Double jobTime;
    private Boolean isEmployee;
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

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getJobTime() {
        return jobTime;
    }

    public void setJobTime(Double jobTime) {
        this.jobTime = jobTime;
    }

    public Boolean getIsEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(Boolean isEmployee) {
        this.isEmployee = isEmployee;
    }

    public double getWorkPoint() {
        workPoint = 0.0;
        for (WorkLabel workLabel : workLabels) {
            workPoint += workLabel.getPoints();
        }
        return workPoint;
    }

    public void setWorkPoint(double workPoint) {
        this.workPoint = workPoint;
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

    public void setWorkPointWithoutEN(double workPointWithoutEN) {
        this.workPointWithoutEN = workPointWithoutEN;
    }

    public List<WorkLabel> getWorkLabels() {
        return workLabels;
    }

    public void setWorkLabels(List<WorkLabel> workLabels) {
        this.workLabels = workLabels;
    }

    public List<UUID> getWorkLablesId() {
        List<UUID> ids = new ArrayList<>();
        for (WorkLabel workLabel : getWorkLabels()) {
            ids.add(workLabel.getId());
        }
        return ids;
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
        return "" +
                name + " " + surname + " \n" +
                phone + " \n" +
                email + " \n" +
                jobTime + " \n" +
                isEmployee + " \n" +
                getWorkPoint() + " \n" +
                getWorkPointWithoutEN() + " \n";
    }
}
