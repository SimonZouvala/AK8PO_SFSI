package fai.utb.db.entity;
/**
 * Class represent all item of PointWeights entity. This class is for count number of points for Employee.
 *
 * @author Šimon Zouvala
 */
public class PointWeights {

    private final double lecture;
    private final double seminar;
    private final double exercise;
    private final double credit;
    private final double gradedCredit;
    private final double examination;
    private final double lectureEn;
    private final double seminarEn;
    private final double exerciseEn;
    private final double creditEn;
    private final double gradedCreditEn;
    private final double examinationEN;

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

    public double getSeminar() {
        return seminar;
    }

    public double getExercise() {
        return exercise;
    }

    public double getCredit() {
        return credit;
    }

    public double getGradedCredit() {
        return gradedCredit;
    }

    public double getExamination() {
        return examination;
    }

    public double getLectureEn() {
        return lectureEn;
    }

    public double getSeminarEn() {
        return seminarEn;
    }

    public double getExerciseEn() {
        return exerciseEn;
    }

    public double getCreditEn() {
        return creditEn;
    }

    public double getGradedCreditEn() {
        return gradedCreditEn;
    }

    public double getExaminationEN() {
        return examinationEN;
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
