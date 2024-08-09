import java.util.ArrayList;
import java.util.List;

/**
 * Manages students, courses, and their associated grades.
 */
public class StudentManager {
    private List<Student> students;
    private List<Course> courses;
    private List<Grade> grades;

    /**
     * Constructs a new StudentManager object, initializing the lists for students, courses, and grades.
     */
    public StudentManager() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        grades = new ArrayList<>();
    }

    /**
     * Adds a new student to the system.
     *
     * @param student the Student object to add
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Updates the information of an existing student.
     *
     * @param oldId      the ID of the student to update
     * @param updatedStudent the updated Student object
     */
    public void updateStudent(String oldId, Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(oldId)) {
                students.set(i, updatedStudent);
                return;
            }
        }
    }

    /**
     * Adds a new course to the system.
     *
     * @param course the Course object to add
     */
    public void addCourse(Course course) {
        courses.add(course);
    }

    /**
     * Enrolls a student in a course.
     *
     * @param studentId the ID of the student to enroll
     * @param courseCode the code of the course to enroll in
     */
    public void enrollStudent(String studentId, String courseCode) {
        grades.add(new Grade(studentId, courseCode, null)); // Add a grade with null initially
    }

    /**
     * Returns a list of all students in the system.
     *
     * @return a list of Student objects
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Returns a list of all courses in the system.
     *
     * @return a list of Course objects
     */
    public List<Course> getCourses() {
        return courses;
    }

//    /**
//     * Returns a list of grades for a specific student.
//     *
//     * @param studentId the ID of the student
//     * @return a list of Grade objects for the student
//     */
//    public List<Grade> getGradesForStudent(String studentId) {
//        List<Grade> studentGrades = new ArrayList<>();
//        for (Grade grade : grades) {
//            if (grade.getStudentId().equals(studentId)) {
//                studentGrades.add(grade);
//            }
//        }
//        return studentGrades;
//    }

//    /**
//     * Returns a list of grades for a specific course.
//     *
//     * @param courseCode the code of the course
//     * @return a list of Grade objects for the course
//     */
//    public List<Grade> getGradesForCourse(String courseCode) {
//        List<Grade> courseGrades = new ArrayList<>();
//        for (Grade grade : grades) {
//            if (grade.getCourseCode().equals(courseCode)) {
//                courseGrades.add(grade);
//            }
//        }
//        return courseGrades;
//    }

    /**
     * Returns a list of students who are not enrolled in the specified course.
     *
     * @param courseCode the code of the course
     * @return a list of Student objects who are not enrolled in the course
     */
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

    /**
     * Returns a list of courses that a specific student is enrolled in.
     *
     * @param studentId the ID of the student
     * @return a list of Course objects that the student is enrolled in
     */
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

    /**
     * Assigns a grade to a student for a specific course.
     *
     * @param studentId the ID of the student
     * @param courseCode the code of the course
     * @param grade      the grade to assign
     */
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

    /**
     * Returns the grade of a student in a specific course.
     *
     * @param studentId the ID of the student
     * @param courseCode the code of the course
     * @return the grade of the student in the course, or an empty string if no grade is found
     */
    public String getGrade(String studentId, String courseCode) {
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId) && grade.getCourseCode().equals(courseCode)) {
                return grade.getGrade();
            }
        }
        return ""; // Return empty string if no grade found
    }

//    /**
//     * Returns the Course object with the given name, or null if no such course exists.
//     *
//     * @param courseName the name of the course to find
//     * @return the Course object with the given name, or null if no such course exists
//     */
//    public Course getCourseByName(String courseName) {
//        for (Course course : courses) {
//            if (course.getName().equalsIgnoreCase(courseName)) {
//                return course;
//            }
//        }
//        return null; // Return null if no course with that name is found
//    }
}