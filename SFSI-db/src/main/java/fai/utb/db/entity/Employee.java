package fai.utb.db.entity;

import java.util.Objects;

public class Employee {

    private Long id;
    public String jmeno;
    public String prijmeni;
    public String telefon;
    public String email;
    public Double uvazek;
    public Boolean zamestnanec;
    public int pracovniBody;
    public int pracovniBodyBezEN;
    public WorkLabel stitek;

    public Employee(String jmeno, String prijmeni, String telefon, String email, Double uvazek, Boolean zamestnanec) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.telefon = telefon;
        this.email = email;
        this.uvazek = uvazek;
        this.zamestnanec = zamestnanec;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getUvazek() {
        return uvazek;
    }

    public void setUvazek(Double uvazek) {
        this.uvazek = uvazek;
    }

    public Boolean getZamestnanec() {
        return zamestnanec;
    }

    public void setZamestnanec(Boolean zamestnanec) {
        this.zamestnanec = zamestnanec;
    }

    public int getPracovniBody() {
        return pracovniBody;
    }

    public void setPracovniBody(int pracovniBody) {
        this.pracovniBody = pracovniBody;
    }

    public int getPracovniBodyBezEN() {
        return pracovniBodyBezEN;
    }

    public void setPracovniBodyBezEN(int pracovniBodyBezEN) {
        this.pracovniBodyBezEN = pracovniBodyBezEN;
    }

    public WorkLabel getStitek() {
        return stitek;
    }

    public void setStitek(WorkLabel stitek) {
        this.stitek = stitek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee that = (Employee) o;
        return Objects.equals(jmeno, that.jmeno) && Objects.equals(prijmeni, that.prijmeni) && Objects.equals(telefon, that.telefon) && Objects.equals(email, that.email) && Objects.equals(uvazek, that.uvazek) && Objects.equals(zamestnanec, that.zamestnanec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jmeno, prijmeni, telefon, email, uvazek, zamestnanec);
    }
}
