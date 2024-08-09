/**
 * Represents a grade assigned to a student for a specific course.
 */
public class Grade {
    private String studentId;
    private String courseCode;
    private String grade;

    /**
     * Constructs a new Grade object with the given student ID, course code, and grade.
     *
     * @param studentId  the ID of the student
     * @param courseCode the code of the course
     * @param grade      the grade assigned
     */
    public Grade(String studentId, String courseCode, String grade) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.grade = grade;
    }

    /**
     * Returns the student ID.
     *
     * @return the student ID
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Returns the course code.
     *
     * @return the course code
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Returns the grade assigned.
     *
     * @return the grade assigned
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Sets the grade assigned.
     *
     * @param grade the new grade assigned
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }
}