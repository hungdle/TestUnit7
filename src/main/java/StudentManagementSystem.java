import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentManagementSystem {
    private JFrame frame;
    private JTextField studentIdField;
    private JTextField studentNameField;
    private JTextField courseCodeField;
    private JTextField courseNameField;
    private JTextField gradeField;
    private JComboBox<Student> studentComboBox;
    private JComboBox<Course> courseComboBox;
    private JTable studentTable;
    private JTable gradeTable;
    private StudentManager studentManager;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public StudentManagementSystem() {
        studentManager = new StudentManager();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Student Management System");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Top menu to select between student and course actions
        JPanel menuPanel = new JPanel();
        String[] options = {"Student Actions", "Course Actions"};
        JComboBox<String> actionSelector = new JComboBox<>(options);
        menuPanel.add(actionSelector);

        // Student Actions Panel
        JPanel studentPanel = new JPanel(new GridLayout(4, 2));
        studentPanel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        studentPanel.add(studentIdField);

        studentPanel.add(new JLabel("Student Name:"));
        studentNameField = new JTextField();
        studentPanel.add(studentNameField);

        JButton addStudentButton = new JButton("Add Student");
        studentPanel.add(addStudentButton);

        JButton enrollButton = new JButton("Enroll Student");
        studentPanel.add(enrollButton);

        // Course Actions Panel
        JPanel coursePanel = new JPanel(new GridLayout(4, 2));
        coursePanel.add(new JLabel("Course Code:"));
        courseCodeField = new JTextField();
        coursePanel.add(courseCodeField);

        coursePanel.add(new JLabel("Course Name:"));
        courseNameField = new JTextField();
        coursePanel.add(courseNameField);

        JButton addCourseButton = new JButton("Add Course");
        coursePanel.add(addCourseButton);

        JButton assignGradeButton = new JButton("Assign Grade");
        coursePanel.add(assignGradeButton);

        // Add both panels to the main panel with CardLayout
        mainPanel.add(studentPanel, "Student Actions");
        mainPanel.add(coursePanel, "Course Actions");

        // Add the main panel and the menu panel to the frame
        frame.add(menuPanel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        // Table and combo boxes
        studentTable = new JTable();
        JScrollPane studentScrollPane = new JScrollPane(studentTable);
        frame.add(studentScrollPane, BorderLayout.CENTER);

        gradeTable = new JTable();
        JScrollPane gradeScrollPane = new JScrollPane(gradeTable);
        frame.add(gradeScrollPane, BorderLayout.SOUTH);

        studentComboBox = new JComboBox<>();
        courseComboBox = new JComboBox<>();
        studentPanel.add(new JLabel("Select Student:"));
        studentPanel.add(studentComboBox);
        coursePanel.add(new JLabel("Select Course:"));
        coursePanel.add(courseComboBox);

        // Event listener for action selection
        actionSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAction = (String) actionSelector.getSelectedItem();
                cardLayout.show(mainPanel, selectedAction);
            }
        });

        // Button actions
        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCourse();
            }
        });

        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enrollStudent();
            }
        });

        assignGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignGrade();
            }
        });

        updateStudentTable();
        updateComboBoxes();
        frame.setVisible(true);
    }

    private void addStudent() {
        String studentId = studentIdField.getText();
        String studentName = studentNameField.getText();

        if (studentId.isEmpty() || studentName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "ID and Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = new Student(studentId, studentName);
        studentManager.addStudent(student);
        updateStudentTable();
        updateComboBoxes();
        studentIdField.setText("");
        studentNameField.setText("");
    }

    private void addCourse() {
        String courseCode = courseCodeField.getText();
        String courseName = courseNameField.getText();

        if (courseCode.isEmpty() || courseName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Code and Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Course course = new Course(courseCode, courseName);
        studentManager.addCourse(course);
        updateComboBoxes();
        courseCodeField.setText("");
        courseNameField.setText("");
    }

    private void enrollStudent() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        Course selectedCourse = (Course) courseComboBox.getSelectedItem();

        if (selectedStudent == null || selectedCourse == null) {
            JOptionPane.showMessageDialog(frame, "Please select a student and a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        studentManager.enrollStudent(selectedStudent.getId(), selectedCourse.getCode());
        updateStudentTable();
    }

    private void assignGrade() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        Course selectedCourse = (Course) courseComboBox.getSelectedItem();
        String grade = gradeField.getText();

        if (selectedStudent == null || selectedCourse == null || grade.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please select a student, a course, and enter a grade", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        studentManager.assignGrade(selectedStudent.getId(), selectedCourse.getCode(), grade);
        updateGradeTable(selectedStudent.getId());
        gradeField.setText("");
    }

    private void updateStudentTable() {
        List<Student> students = studentManager.getStudents();
        String[] columnNames = {"Student ID", "Student Name"};
        Object[][] data = new Object[students.size()][2];

        for (int i = 0; i < students.size(); i++) {
            data[i][0] = students.get(i).getId();
            data[i][1] = students.get(i).getName();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        studentTable.setModel(model);
    }

    private void updateGradeTable(String studentId) {
        List<Grade> grades = studentManager.getGradesForStudent(studentId);
        String[] columnNames = {"Course Code", "Grade"};
        Object[][] data = new Object[grades.size()][2];

        for (int i = 0; i < grades.size(); i++) {
            data[i][0] = grades.get(i).getCourseCode();
            data[i][1] = grades.get(i).getGrade();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        gradeTable.setModel(model);
    }

    private void updateComboBoxes() {
        studentComboBox.removeAllItems();
        for (Student student : studentManager.getStudents()) {
            studentComboBox.addItem(student);
        }

        courseComboBox.removeAllItems();
        for (Course course : studentManager.getCourses()) {
            courseComboBox.addItem(course);
        }
    }
}
