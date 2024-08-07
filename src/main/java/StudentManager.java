import java.util.ArrayList;
import java.util.List;

public class StudentManager {
    private List<Student> students;
    private List<Course> courses;
    private List<Grade> grades;

    public StudentManager() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        grades = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void updateStudent(String oldId, Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(oldId)) {
                students.set(i, updatedStudent);
                return;
            }
        }
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void enrollStudent(String studentId, String courseCode) {
        grades.add(new Grade(studentId, courseCode, null)); // Add a grade with null initially
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Grade> getGrades(String studentId) {
        List<Grade> studentGrades = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId)) {
                studentGrades.add(grade);
            }
        }
        return studentGrades;
    }
}
