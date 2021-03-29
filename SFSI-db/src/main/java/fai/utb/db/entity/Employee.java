package fai.utb.db.entity;

import java.util.List;
import java.util.Objects;

public class Employee {

    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private Double jobTime;
    private Boolean isEmployee;
    private int workPoint;
    private int workPointWithoutEN;
    private List<WorkLabel> workLabels;

    public Employee(Long id, String name, String surname, String phone, String email, Double jobTime, Boolean isEmployee, int workPoint, int workPointWithoutEN, List<WorkLabel> workLabels) {
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

    public Employee(Long id, String name, String surname, String phone, String email, Double jobTime, Boolean isEmployee) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.jobTime = jobTime;
        this.isEmployee = isEmployee;
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

    public int getWorkPoint() {
        return workPoint;
    }

    public void setWorkPoint(int workPoint) {
        this.workPoint = workPoint;
    }

    public int getWorkPointWithoutEN() {
        return workPointWithoutEN;
    }

    public void setWorkPointWithoutEN(int workPointWithoutEN) {
        this.workPointWithoutEN = workPointWithoutEN;
    }

    public List<WorkLabel> getWorkLabels() {
        return workLabels;
    }

    public void setWorkLabels(List<WorkLabel> workLabels) {
        this.workLabels = workLabels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee that = (Employee) o;
        return Objects.equals(name, that.name) && Objects.equals(surname, that.surname) && Objects.equals(phone, that.phone) && Objects.equals(email, that.email) && Objects.equals(jobTime, that.jobTime) && Objects.equals(isEmployee, that.isEmployee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, phone, email, jobTime, isEmployee);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", jobTime=" + jobTime +
                ", isEmployee=" + isEmployee +
                ", workPoint=" + workPoint +
                ", workPointWithoutEN=" + workPointWithoutEN +
                ", workLabels=" + workLabels +
                '}';
    }
}
