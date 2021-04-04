package fai.utb.db.entity;

public class PointWeights {

    private double lecture;
    private double seminar;
    private double exercise;
    private double credit;
    private double gradedCredit;
    private double examination;
    private double lectureEn;
    private double seminarEn;
    private double exerciseEn;
    private double creditEn;
    private double gradedCreditEn;
    private double examinationEN;

    public PointWeights(double lecture, double seminar, double exercise, double credit, double gradedCredit,
                        double examination, double lectureEn, double seminarEn, double exerciseEn, double creditEn,
                        double gradedCreditEn, double examinationEN) {
        this.lecture = lecture;
        this.seminar = seminar;
        this.exercise = exercise;
        this.credit = credit;
        this.gradedCredit = gradedCredit;
        this.examination = examination;
        this.lectureEn = lectureEn;
        this.seminarEn = seminarEn;
        this.exerciseEn = exerciseEn;
        this.creditEn = creditEn;
        this.gradedCreditEn = gradedCreditEn;
        this.examinationEN = examinationEN;
    }

    public double getLecture() {
        return lecture;
    }

    public void setLecture(double lecture) {
        this.lecture = lecture;
    }

    public double getSeminar() {
        return seminar;
    }

    public void setSeminar(double seminar) {
        this.seminar = seminar;
    }

    public double getExercise() {
        return exercise;
    }

    public void setExercise(double exercise) {
        this.exercise = exercise;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getGradedCredit() {
        return gradedCredit;
    }

    public void setGradedCredit(double gradedCredit) {
        this.gradedCredit = gradedCredit;
    }

    public double getExamination() {
        return examination;
    }

    public void setExamination(double examination) {
        this.examination = examination;
    }

    public double getLectureEn() {
        return lectureEn;
    }

    public void setLectureEn(double lectureEn) {
        this.lectureEn = lectureEn;
    }

    public double getSeminarEn() {
        return seminarEn;
    }

    public void setSeminarEn(double seminarEn) {
        this.seminarEn = seminarEn;
    }

    public double getExerciseEn() {
        return exerciseEn;
    }

    public void setExerciseEn(double exerciseEn) {
        this.exerciseEn = exerciseEn;
    }

    public double getCreditEn() {
        return creditEn;
    }

    public void setCreditEn(double creditEn) {
        this.creditEn = creditEn;
    }

    public double getGradedCreditEn() {
        return gradedCreditEn;
    }

    public void setGradedCreditEn(double gradedCreditEn) {
        this.gradedCreditEn = gradedCreditEn;
    }

    public double getExaminationEN() {
        return examinationEN;
    }

    public void setExaminationEN(double examinationEN) {
        this.examinationEN = examinationEN;
    }

    @Override
    public String toString() {
        return "PointWeights{" +
                "lecture=" + lecture +
                ", seminar=" + seminar +
                ", exercise=" + exercise +
                ", credit=" + credit +
                ", gradedCredit=" + gradedCredit +
                ", examination=" + examination +
                ", lectureEn=" + lectureEn +
                ", seminarEn=" + seminarEn +
                ", exerciseEn=" + exerciseEn +
                ", creditEn=" + creditEn +
                ", gradedCreditEn=" + gradedCreditEn +
                ", examinationEN=" + examinationEN +
                '}';
    }
}
