/**
 * Represents a student in the student management system.
 */
public class Student {
    private String id;
    private String name;

    /**
     * Constructs a new Student object with the given ID and name.
     *
     * @param id   the student's ID
     * @param name the student's name
     */
    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the student's ID.
     *
     * @return the student's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the student's ID.
     *
     * @param id the new student ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the student's name.
     *
     * @return the student's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the student's name.
     *
     * @param name the new student name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the Student object in the format "name (id)".
     *
     * @return the string representation of the Student object
     */
    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}