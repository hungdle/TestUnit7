public class Grade {
    private String studentId;
    private String courseCode;
    private String grade;

    public Grade(String studentId, String courseCode, String grade) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.grade = grade;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return courseCode + ": " + grade;
    }
}
