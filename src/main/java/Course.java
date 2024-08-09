/**
 * Represents a course in the student management system.
 */
public class Course {
    private String code;
    private String name;

    /**
     * Constructs a new Course object with the given code and name.
     *
     * @param code the course code
     * @param name the course name
     */
    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Returns the course code.
     *
     * @return the course code
     */
    public String getCode() {
        return code;
    }


    /**
     * Returns the course name.
     *
     * @return the course name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the Course object in the format "name (code)".
     *
     * @return the string representation of the Course object
     */
    @Override
    public String toString() {
        if (code == null || name == null) {
            return "(Select)";
        }
        return name + " (" + code + ")";
    }
}