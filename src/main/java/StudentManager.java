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

    public void assignGrade(String studentId, String courseCode, String grade) {
        for (Grade g : grades) {
            if (g.getStudentId().equals(studentId) && g.getCourseCode().equals(courseCode)) {
                g.setGrade(grade);
                return;
            }
        }
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

    public List<Student> getUnEnrolledStudents(String courseCode) {
        List<Student> unEnrolledStudents = new ArrayList<>();
        for (Student student : students) {
            boolean enrolled = false;
            for (Grade grade : grades) {
                if (grade.getStudentId().equals(student.getId()) && grade.getCourseCode().equals(courseCode)) {
                    enrolled = true;
                    break;
                }
            }
            if (!enrolled) {
                unEnrolledStudents.add(student);
            }
        }
        return unEnrolledStudents;
    }

    public List<Course> getEnrolledCourses(String studentId) {
        List<Course> enrolledCourses = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId)) {
                for (Course course : courses) {
                    if (course.getCode().equals(grade.getCourseCode())) {
                        enrolledCourses.add(course);
                        break;
                    }
                }
            }
        }
        return enrolledCourses;
    }
}
