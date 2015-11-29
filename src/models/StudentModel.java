package models;

public class StudentModel {

    private int studentId;

    private String studentName;

    private int studentGroup;

    private int numberOfGradebook;

    private String studentSex;

    public String getStudentSex() {
        return studentSex;
    }

    public void setStudentSex(String studentSex) {
        this.studentSex = studentSex;
    }

    public int getNumberOfGradebook() {
        return numberOfGradebook;
    }

    public void setNumberOfGradebook(int numberOfGradebook) {
        this.numberOfGradebook = numberOfGradebook;
    }

    public int getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(int studentGroup) {
        this.studentGroup = studentGroup;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
