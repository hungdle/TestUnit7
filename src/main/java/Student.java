public class Student {
    private String id;
    private String name;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }

    public void setId(String newId) {
        this.id = newId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
