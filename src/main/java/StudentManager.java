import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentManager {
    private List<Student> students = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private Map<String, List<String>> enrollments = new HashMap<>();
    private List<Grade> grades = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getStudents() {
        return students;
    }

    public Student getStudentById(String id) {
        return students.stream()
                       .filter(student -> student.getId().equals(id))
                       .findFirst()
                       .orElse(null);
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void enrollStudent(String studentId, String courseCode) {
        enrollments.computeIfAbsent(studentId, k -> new ArrayList<>()).add(courseCode);
    }

    public List<String> getCoursesForStudent(String studentId) {
        return enrollments.getOrDefault(studentId, new ArrayList<>());
    }

    public void assignGrade(String studentId, String courseCode, String grade) {
        grades.add(new Grade(studentId, courseCode, grade));
    }

    public List<Grade> getGradesForStudent(String studentId) {
        List<Grade> studentGrades = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId)) {
                studentGrades.add(grade);
            }
        }
        return studentGrades;
    }

    public List<Grade> getGradesForCourse(String courseCode) {
        List<Grade> courseGrades = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getCourseCode().equals(courseCode)) {
                courseGrades.add(grade);
            }
        }
        return courseGrades;
    }

    public void updateStudent(String oldId, String newId, String newName) {
        Student student = getStudentById(oldId);
        if (student != null) {
            student.setId(newId);
            student.setName(newName);
        }
    }
}
