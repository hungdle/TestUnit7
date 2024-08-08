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

    // Add a new student
    public void addStudent(Student student) {
        students.add(student);
    }

    // Update existing student information
    public void updateStudent(String oldId, Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(oldId)) {
                students.set(i, updatedStudent);
                return;
            }
        }
    }

    // Add a new course
    public void addCourse(Course course) {
        courses.add(course);
    }

    // Enroll a student in a course
    public void enrollStudent(String studentId, String courseCode) {
        grades.add(new Grade(studentId, courseCode, null)); // Add a grade with null initially
    }

    // Get all students
    public List<Student> getStudents() {
        return students;
    }

    // Get all courses
    public List<Course> getCourses() {
        return courses;
    }

    // Get grades for a specific student
    public List<Grade> getGradesForStudent(String studentId) {
        List<Grade> studentGrades = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId)) {
                studentGrades.add(grade);
            }
        }
        return studentGrades;
    }

    // Get grades for a specific course
    public List<Grade> getGradesForCourse(String courseCode) {
        List<Grade> courseGrades = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getCourseCode().equals(courseCode)) {
                courseGrades.add(grade);
            }
        }
        return courseGrades;
    }

    // Get unenrolled students for a specific course
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

    // Get enrolled courses for a specific student
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

    // Assign a grade to a student for a specific course
// Assign a grade to a student for a specific course
    public void assignGrade(String studentId, String courseCode, String grade) {
        for (Grade g : grades) {
            if (g.getStudentId().equals(studentId) && g.getCourseCode().equals(courseCode)) {
                g.setGrade(grade);
                return;
            }
        }
        // If not found, add a new grade entry
        grades.add(new Grade(studentId, courseCode, grade));
    }

    // Get a grade for a student in a specific course
    public String getGrade(String studentId, String courseCode) {
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId) && grade.getCourseCode().equals(courseCode)) {
                return grade.getGrade();
            }
        }
        return ""; // Return empty string if no grade found
    }

    // Get a course by its name
    public Course getCourseByName(String courseName) {
        for (Course course : courses) {
            if (course.getName().equalsIgnoreCase(courseName)) {
                return course;
            }
        }
        return null; // Return null if no course with that name is found
    }
}
