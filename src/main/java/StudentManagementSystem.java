import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * A graphical user interface for managing students, courses, and grades.
 * This class provides a simple GUI for performing operations like adding,
 * updating,
 * viewing, enrolling, and managing grades for students and courses.
 */
public class StudentManagementSystem {
    private JFrame frame;
    private JPanel actionPanel, contentPanel;
    private CardLayout cardLayout;
    private JTable studentTable;
    private StudentManager studentManager;

    /**
     * Constructs a new StudentManagementSystem object.
     */
    public StudentManagementSystem() {
        studentManager = new StudentManager();
        initialize();
    }

    /**
     * Initializes the GUI components and sets up the main frame.
     */
    private void initialize() {
        frame = new JFrame("Student Management System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Left panel for action buttons
        actionPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        frame.add(actionPanel, BorderLayout.WEST);

        // Center panel for content
        contentPanel = new JPanel(cardLayout = new CardLayout());
        frame.add(contentPanel, BorderLayout.CENTER);

        // Add buttons to the action panel
        addActionButtons();

        // Add the initial content panels
        addContentPanels();

        frame.setVisible(true);
    }

    /**
     * Adds action buttons to the left panel.
     */
    private void addActionButtons() {
        JButton addStudentButton = new JButton("Add Student");
        addStudentButton.addActionListener(e -> cardLayout.show(contentPanel, "Add Student"));

        JButton updateStudentButton = new JButton("Update Student");
        updateStudentButton.addActionListener(e -> {
            refreshUpdateStudentComboBox();
            cardLayout.show(contentPanel, "Update Student");
        });

        JButton viewDetailsButton = new JButton("View Student Details");
        viewDetailsButton.addActionListener(e -> {
            viewStudentDetails();
            cardLayout.show(contentPanel, "View Student Details");
        });

        JButton addCourseButton = new JButton("Add Course");
        addCourseButton.addActionListener(e -> cardLayout.show(contentPanel, "Add Course"));

        JButton enrollStudentButton = new JButton("Enroll Student");
        enrollStudentButton.addActionListener(e -> {
            refreshEnrollStudentComboBoxes();
            cardLayout.show(contentPanel, "Enroll Student");
        });

        JButton gradeManagementButton = new JButton("Grade Management");
        gradeManagementButton.addActionListener(e -> cardLayout.show(contentPanel, "Grade Management"));

        actionPanel.add(addStudentButton);
        actionPanel.add(updateStudentButton);
        actionPanel.add(viewDetailsButton);
        actionPanel.add(addCourseButton);
        actionPanel.add(enrollStudentButton);
        actionPanel.add(gradeManagementButton);
    }

    /**
     * Adds content panels for different operations to the center panel.
     */
    private void addContentPanels() {
        contentPanel.add(createAddStudentPanel(), "Add Student");
        contentPanel.add(createUpdateStudentPanel(), "Update Student");
        contentPanel.add(createViewStudentDetailsPanel(), "View Student Details");
        contentPanel.add(createAddCoursePanel(), "Add Course");
        contentPanel.add(createEnrollStudentPanel(), "Enroll Student");
        contentPanel.add(createGradeManagementPanel(), "Grade Management");
    }

    /**
     * Creates a panel for adding a new student.
     *
     * @return the panel for adding a new student
     */
    private JPanel createAddStudentPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Student ID:"));
        JTextField studentIdField = new JTextField();
        panel.add(studentIdField);
        panel.add(new JLabel("Student Name:"));
        JTextField studentNameField = new JTextField();
        panel.add(studentNameField);
        JButton addButton = new JButton("Add");
        panel.add(new JLabel()); // Empty cell
        panel.add(addButton);

        addButton.addActionListener(e -> {
            String studentId = studentIdField.getText();
            String studentName = studentNameField.getText();
            if (studentId.isEmpty() || studentName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "ID and Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student student = new Student(studentId, studentName);
            studentManager.addStudent(student);
            JOptionPane.showMessageDialog(frame, "Student added successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            studentIdField.setText(""); // Clear text fields
            studentNameField.setText("");
            updateStudentTable();
            refreshUpdateStudentComboBox();
            refreshEnrollStudentComboBoxes();
            refreshGradeManagementComboBox();
        });

        return panel;
    }

    /**
     * Creates a panel for updating an existing student.
     *
     * @return the panel for updating an existing student
     */
    private JPanel createUpdateStudentPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel selectStudentLabel = new JLabel("Select Student:");
        JComboBox<Student> updateStudentComboBox = new JComboBox<>(
                studentManager.getStudents().toArray(new Student[0]));
        JTextField studentIdField = new JTextField();
        JTextField studentNameField = new JTextField();
        studentIdField.setEditable(false);
        JLabel studentIdLabel = new JLabel("Student ID:");
        JLabel studentNameLabel = new JLabel("Student Name:");
        JButton updateButton = new JButton("Update");

        updateStudentComboBox.addActionListener(e -> {
            Student selectedStudent = (Student) updateStudentComboBox.getSelectedItem();
            if (selectedStudent != null) {
                studentIdField.setText(selectedStudent.getId());
                studentNameField.setText(selectedStudent.getName());
            }
        });

        panel.add(selectStudentLabel);
        panel.add(updateStudentComboBox);
        panel.add(studentIdLabel);
        panel.add(studentIdField);
        panel.add(studentNameLabel);
        panel.add(studentNameField);
        panel.add(new JLabel()); // Empty cell
        panel.add(updateButton);

        updateButton.addActionListener(e -> {
            Student selectedStudent = (Student) updateStudentComboBox.getSelectedItem();
            if (selectedStudent != null) {
                String newId = studentIdField.getText();
                String newName = studentNameField.getText();
                if (newId.isEmpty() || newName.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "ID and Name cannot be empty", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                studentManager.updateStudent(selectedStudent.getId(), new Student(newId, newName));
                JOptionPane.showMessageDialog(frame, "Student updated successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                updateStudentTable();
            }
        });

        return panel;
    }

    /**
     * Creates a panel for viewing student details in a table.
     *
     * @return the panel for viewing student details
     */
    private JPanel createViewStudentDetailsPanel() {
        studentTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(studentTable);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Creates a panel for adding a new course.
     *
     * @return the panel for adding a new course
     */
    private JPanel createAddCoursePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel courseCodeLabel = new JLabel("Course Code:");
        JTextField courseCodeField = new JTextField();
        JLabel courseNameLabel = new JLabel("Course Name:");
        JTextField courseNameField = new JTextField();
        JButton addButton = new JButton("Add");

        panel.add(courseCodeLabel);
        panel.add(courseCodeField);
        panel.add(courseNameLabel);
        panel.add(courseNameField);
        panel.add(new JLabel()); // Empty cell
        panel.add(addButton);

        addButton.addActionListener(e -> {
            String courseCode = courseCodeField.getText();
            String courseName = courseNameField.getText();
            if (courseCode.isEmpty() || courseName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Code and Name cannot be empty", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Course course = new Course(courseCode, courseName);
            studentManager.addCourse(course);
            JOptionPane.showMessageDialog(frame, "Course added successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            courseCodeField.setText(""); // Clear text fields
            courseNameField.setText("");
        });

        return panel;
    }

    /**
     * Creates a panel for enrolling a student in a course.
     *
     * @return the panel for enrolling a student in a course
     */
    private JPanel createEnrollStudentPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel courseLabel = new JLabel("Select Course:");
        JComboBox<Course> courseComboBox = new JComboBox<>(studentManager.getCourses().toArray(new Course[0]));
        JLabel studentLabel = new JLabel("Select Student:");
        JComboBox<Student> studentComboBox = new JComboBox<>();
        JButton enrollButton = new JButton("Enroll");

        courseComboBox.addActionListener(e -> {
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();
            if (selectedCourse != null) {
                List<Student> unEnrolledStudents = studentManager.getUnEnrolledStudents(selectedCourse.getCode());
                studentComboBox.setModel(new DefaultComboBoxModel<>(unEnrolledStudents.toArray(new Student[0])));
            }
        });

        panel.add(courseLabel);
        panel.add(courseComboBox);
        panel.add(studentLabel);
        panel.add(studentComboBox);
        panel.add(new JLabel()); // Empty cell
        panel.add(enrollButton);

        enrollButton.addActionListener(e -> {
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();
            if (selectedStudent == null || selectedCourse == null) {
                JOptionPane.showMessageDialog(frame, "Please select a student and a course", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            studentManager.enrollStudent(selectedStudent.getId(), selectedCourse.getCode());
            JOptionPane.showMessageDialog(frame, "Student enrolled successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            updateStudentTable();
            refreshEnrollStudentComboBoxes();
            refreshGradeManagementComboBox();
        });

        return panel;
    }

    /**
     * Creates a panel for managing grades.
     *
     * @return the panel for managing grades
     */
    private JPanel createGradeManagementPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10)); // GridLayout with gaps

        // Panel for student selection
        JComboBox<Student> studentComboBox = new JComboBox<>(studentManager.getStudents().toArray(new Student[0]));
        panel.add(new JLabel("Select Student:"));
        panel.add(studentComboBox);

        // Table for course names
        String[] courseColumnNames = { "Course Name" };
        DefaultTableModel courseTableModel = new DefaultTableModel(courseColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the cells non-editable
                return false;
            }
        };
        JTable courseTable = new JTable(courseTableModel);
        JScrollPane courseScrollPane = new JScrollPane(courseTable);
        panel.add(courseScrollPane);

        // Table for grades
        String[] gradeColumnNames = { "Grade" };
        DefaultTableModel gradeTableModel = new DefaultTableModel(gradeColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the cells non-editable
                return false;
            }
        };
        JTable gradeTable = new JTable(gradeTableModel);
        JScrollPane gradeScrollPane = new JScrollPane(gradeTable);
        panel.add(gradeScrollPane);

        // Panel for course selection and grade input
        JComboBox<Course> courseComboBox = new JComboBox<>();
        panel.add(new JLabel("Select Course:"));
        panel.add(courseComboBox);

        JTextField gradeField = new JTextField();
        panel.add(new JLabel("Update Grade:"));
        panel.add(gradeField);

        // Button to assign grades
        JButton assignGradeButton = new JButton("Assign Grade");
        assignGradeButton.addActionListener(e -> {
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();
            String newGrade = gradeField.getText();

            if (selectedStudent == null) {
                JOptionPane.showMessageDialog(frame, "Please select a student", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(frame, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newGrade.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a grade", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update the grade
            studentManager.assignGrade(selectedStudent.getId(), selectedCourse.getCode(), newGrade);

            JOptionPane.showMessageDialog(frame, "Grade assigned successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh the grade table
            refreshGradeTable(selectedStudent, gradeTableModel);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(assignGradeButton);
        panel.add(buttonPanel);

        // Update tables when a student is selected
        studentComboBox.addActionListener(e -> {
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();
            if (selectedStudent != null) {
                List<Course> enrolledCourses = studentManager.getEnrolledCourses(selectedStudent.getId());
                courseTableModel.setRowCount(0); // Clear previous data
                gradeTableModel.setRowCount(0); // Clear previous data
                for (Course course : enrolledCourses) {
                    String currentGrade = studentManager.getGrade(selectedStudent.getId(), course.getCode());
                    courseTableModel.addRow(new Object[] { course.getName() });
                    gradeTableModel.addRow(new Object[] { currentGrade });
                }

                // Update courseComboBox
                courseComboBox.setModel(new DefaultComboBoxModel<>(enrolledCourses.toArray(new Course[0])));
            }
        });

        return panel;
    }

    /**
     * Refreshes the grade table for a given student.
     *
     * @param student         the student whose grades to display
     * @param gradeTableModel the model of the grade table
     */
    private void refreshGradeTable(Student student, DefaultTableModel gradeTableModel) {
        if (student != null) {
            List<Course> enrolledCourses = studentManager.getEnrolledCourses(student.getId());
            gradeTableModel.setRowCount(0); // Clear previous data
            for (Course course : enrolledCourses) {
                String currentGrade = studentManager.getGrade(student.getId(), course.getCode());
                gradeTableModel.addRow(new Object[] { currentGrade });
            }
        }
    }

    /**
     * Updates the student table with the latest data.
     */
    private void updateStudentTable() {
        if (studentTable != null) {
            viewStudentDetails();
        }
    }

    /**
     * Populates the student table with student data.
     */
    private void viewStudentDetails() {
        String[] columns = { "ID", "Name" };
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        studentTable.setModel(tableModel);

        for (Student student : studentManager.getStudents()) {
            Object[] row = { student.getId(), student.getName() };
            tableModel.addRow(row);
        }
    }

    /**
     * Refreshes the combo box for updating students with the latest data.
     */
    private void refreshUpdateStudentComboBox() {
        JPanel updateStudentPanel = (JPanel) contentPanel.getComponent(1); // Assuming "Update Student" panel is at
                                                                           // index 1
        JComboBox<Student> updateStudentComboBox = (JComboBox<Student>) updateStudentPanel.getComponent(1);
        updateStudentComboBox
                .setModel(new DefaultComboBoxModel<>(studentManager.getStudents().toArray(new Student[0])));
    }

    /**
     * Refreshes the combo boxes for enrolling students with the latest data.
     */
    private void refreshEnrollStudentComboBoxes() {
        JPanel enrollStudentPanel = (JPanel) contentPanel.getComponent(4); // Assuming "Enroll Student" panel is at
                                                                           // index 4
        JComboBox<Course> courseComboBox = (JComboBox<Course>) enrollStudentPanel.getComponent(1);
        JComboBox<Student> studentComboBox = (JComboBox<Student>) enrollStudentPanel.getComponent(3);

        courseComboBox.setModel(new DefaultComboBoxModel<>(studentManager.getCourses().toArray(new Course[0])));
        studentComboBox.setModel(new DefaultComboBoxModel<>(new Student[0])); // Initially empty until course is
                                                                              // selected
    }

    /**
     * Refreshes the combo boxes in the grade management panel with the latest data.
     */
    private void refreshGradeManagementComboBox() {
        // Assuming the "Grade Management" panel is at index 2 of contentPanel
        JPanel gradeManagementPanel = (JPanel) contentPanel.getComponent(2);

        // Access the JComboBoxes within the panel
        JComboBox<Student> studentComboBox = (JComboBox<Student>) ((JPanel) gradeManagementPanel.getComponent(0))
                .getComponent(1);
        JComboBox<Course> courseComboBox = (JComboBox<Course>) ((JPanel) gradeManagementPanel.getComponent(1))
                .getComponent(1);

        // Refresh studentComboBox
        studentComboBox.setModel(new DefaultComboBoxModel<>(studentManager.getStudents().toArray(new Student[0])));

        // Refresh courseComboBox - Assuming we need to refresh it when a student is
        // selected
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent != null) {
            List<Course> enrolledCourses = studentManager.getEnrolledCourses(selectedStudent.getId());
            courseComboBox.setModel(new DefaultComboBoxModel<>(enrolledCourses.toArray(new Course[0])));
        } else {
            // Clear courseComboBox if no student is selected
            courseComboBox.setModel(new DefaultComboBoxModel<>(new Course[0]));
        }
    }
}