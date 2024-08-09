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
        gradeManagementButton.addActionListener(e -> {
            refreshGradeManagementComboBoxes();
            cardLayout.show(contentPanel, "Grade Management");
        });

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

            // Check for duplicate Student ID
            boolean isDuplicate = studentManager.getStudents().stream()
                    .anyMatch(student -> student.getId().equals(studentId));

            if (isDuplicate) {
                JOptionPane.showMessageDialog(frame, "Student ID already exists", "Error", JOptionPane.ERROR_MESSAGE);
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
            refreshGradeManagementComboBoxes();
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
        JLabel studentIdField = new JLabel();
        JTextField studentNameField = new JTextField();

        JLabel studentIdLabel = new JLabel("Student ID:");
        JLabel studentNameLabel = new JLabel("Student Name:");
        JButton updateButton = new JButton("Update");

        updateStudentComboBox.addActionListener(e -> {
            Student selectedStudent = (Student) updateStudentComboBox.getSelectedItem();
            if (selectedStudent != null && "(Select)".equals(selectedStudent.toString())) {
                studentIdField.setText("");
                studentNameField.setText("");
            } else if (selectedStudent != null) {
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
        JComboBox<Course> courseComboBox = new JComboBox<>();
        JLabel studentLabel = new JLabel("Select Student:");
        JComboBox<Student> studentComboBox = new JComboBox<>();
        JButton enrollButton = new JButton("Enroll");

        // Initialize courseComboBox with "(Select)" option
        DefaultComboBoxModel<Course> courseModel = new DefaultComboBoxModel<>();
        courseModel.addElement(new Course(null, "(Select)"));
        for (Course course : studentManager.getCourses()) {
            courseModel.addElement(course);
        }
        courseComboBox.setModel(courseModel);

        // Add ActionListener to courseComboBox to update studentComboBox
        courseComboBox.addActionListener(e -> {
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();
            if (selectedCourse == null || selectedCourse.getCode() == null) {
                // If "(Select)" is chosen, clear the student combo box
                studentComboBox.setModel(new DefaultComboBoxModel<>(new Student[0]));
            } else {
                List<Student> unEnrolledStudents = studentManager.getUnEnrolledStudents(selectedCourse.getCode());
                DefaultComboBoxModel<Student> studentModel = new DefaultComboBoxModel<>();
                studentModel.addElement(new Student(null, "(Select)")); // Add "(Select)" option
                for (Student student : unEnrolledStudents) {
                    studentModel.addElement(student);
                }
                studentComboBox.setModel(studentModel);
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
            if (selectedStudent == null || selectedCourse == null || selectedStudent.getId() == null || selectedCourse.getCode() == null) {
                JOptionPane.showMessageDialog(frame, "Please select a student and a course", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            studentManager.enrollStudent(selectedStudent.getId(), selectedCourse.getCode());
            JOptionPane.showMessageDialog(frame, "Student enrolled successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            updateStudentTable();
            refreshEnrollStudentComboBoxes();
            refreshGradeManagementComboBoxes();
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
        JLabel selectedStudentLabel = new JLabel("Select Student:");
        JComboBox<Student> gradeStudentComboBox = new JComboBox<>();
        gradeStudentComboBox.addItem(new Student(null, "(Select)"));

        // Table for enrolled course names
        String[] courseColumnNames = { "Enrolled Course" };
        DefaultTableModel courseTableModel = new DefaultTableModel(courseColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the cells non-editable
                return false;
            }
        };
        JTable courseTable = new JTable(courseTableModel);
        JScrollPane courseScrollPane = new JScrollPane(courseTable);

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

        // Panel for course selection
        JLabel selectCourseLabel = new JLabel("Select Course:");
        JComboBox<Course> gradeCourseComboBox = new JComboBox<>();
        gradeCourseComboBox.addItem(new Course(null, "(Select)"));

        // Panel for grade input
        JLabel updateGradeLabel = new JLabel("Update Grade:");
        JTextField gradeField = new JTextField();

        // Button to assign grades
        JButton assignGradeButton = new JButton("Assign Grade");

        panel.add(selectedStudentLabel);
        panel.add(gradeStudentComboBox);
        panel.add(courseScrollPane);
        panel.add(gradeScrollPane);
        panel.add(selectCourseLabel);
        panel.add(gradeCourseComboBox);
        panel.add(updateGradeLabel);
        panel.add(gradeField);
        panel.add(new JLabel()); // Empty cell
        panel.add(assignGradeButton);

        // Action listener for the "Assign Grade" button
        assignGradeButton.addActionListener(e -> {
            Student selectedStudent = (Student) gradeStudentComboBox.getSelectedItem();
            Course selectedCourse = (Course) gradeCourseComboBox.getSelectedItem();
            String newGrade = gradeField.getText();

            if (selectedStudent == null || "(Select)".equals(selectedStudent.getName())) {
                JOptionPane.showMessageDialog(frame, "Please select a student", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedCourse == null || "(Select)".equals(selectedCourse.getName())) {
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

            // Clear text fields
            gradeField.setText("");
        });

        // Action listener for the student combo box
        gradeStudentComboBox.addActionListener(e -> {
            Student selectedStudent = (Student) gradeStudentComboBox.getSelectedItem();
            if (selectedStudent != null && !"(Select)".equals(selectedStudent.getName())) {
                List<Course> enrolledCourses = studentManager.getEnrolledCourses(selectedStudent.getId());
                courseTableModel.setRowCount(0); // Clear previous data
                gradeTableModel.setRowCount(0); // Clear previous data
                for (Course course : enrolledCourses) {
                    String currentGrade = studentManager.getGrade(selectedStudent.getId(), course.getCode());
                    courseTableModel.addRow(new Object[] { course.getName() });
                    gradeTableModel.addRow(new Object[] { currentGrade });
                }

                // Update courseComboBox
                DefaultComboBoxModel<Course> courseModel = new DefaultComboBoxModel<>();
                courseModel.addElement(new Course(null, "(Select)"));
                for (Course course : enrolledCourses) {
                    courseModel.addElement(course);
                }
                gradeCourseComboBox.setModel(courseModel);
            } else {
                // Clear course table and combo box if "(Select)" is chosen
                courseTableModel.setRowCount(0);
                gradeTableModel.setRowCount(0);
                gradeCourseComboBox.setModel(new DefaultComboBoxModel<>(new Course[] { new Course(null, "(Select)") }));
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
        JPanel updateStudentPanel = (JPanel) contentPanel.getComponent(1); // Assuming "Update Student" panel is at index 1
        JComboBox<Student> updateStudentComboBox = (JComboBox<Student>) updateStudentPanel.getComponent(1);

        // Create a new DefaultComboBoxModel with "(Select)" as the first item
        DefaultComboBoxModel<Student> model = new DefaultComboBoxModel<>();
        model.addElement(new Student(null, null));
        for (Student student : studentManager.getStudents()) {
            model.addElement(student);
        }
        updateStudentComboBox.setModel(model);
    }

    /**
     * Refreshes the combo boxes for enrolling students with the latest data.
     */
    private void refreshEnrollStudentComboBoxes() {
        JPanel enrollStudentPanel = (JPanel) contentPanel.getComponent(4); // Assuming "Enroll Student" panel is at index 4
        JComboBox<Course> courseComboBox = (JComboBox<Course>) enrollStudentPanel.getComponent(1);
        JComboBox<Student> studentComboBox = (JComboBox<Student>) enrollStudentPanel.getComponent(3);

        // Refresh course combo box with "(Select)" as the first option
        DefaultComboBoxModel<Course> courseModel = new DefaultComboBoxModel<>();
        courseModel.addElement(new Course(null, null)); // Assuming null values represent "(Select)"
        for (Course course : studentManager.getCourses()) {
            courseModel.addElement(course);
        }
        courseComboBox.setModel(courseModel);

        // Set student combo box to initially have only "(Select)" option
        DefaultComboBoxModel<Student> studentModel = new DefaultComboBoxModel<>();
        studentComboBox.setModel(studentModel);

    }

    /**
     * Refreshes the combo boxes in the grade management panel with the latest data.
     */
    private void refreshGradeManagementComboBoxes() {
        // Access the "Grade Management" panel
        JPanel gradeManagementPanel = (JPanel) contentPanel.getComponent(5);

        // Access the JComboBoxes within the panel directly
        JComboBox<Student> gradeStudentComboBox = (JComboBox<Student>) gradeManagementPanel.getComponent(1);
        JComboBox<Course> gradeCourseComboBox = (JComboBox<Course>) gradeManagementPanel.getComponent(5);

        // Refresh the studentComboBox with the latest student data
        DefaultComboBoxModel<Student> studentModel = new DefaultComboBoxModel<>();
        studentModel.addElement(new Student(null, "(Select)"));
        for (Student student : studentManager.getStudents()) {
            studentModel.addElement(student);
        }
        gradeStudentComboBox.setModel(studentModel);

        // Clear the courseComboBox initially
        gradeCourseComboBox.setModel(new DefaultComboBoxModel<>(new Course[] { new Course(null, "(Select)") }));

        // Add an action listener to update the course combo box when a student is selected
        gradeStudentComboBox.addActionListener(e -> {
            Student selectedStudent = (Student) gradeStudentComboBox.getSelectedItem();
            if (selectedStudent != null && !"(Select)".equals(selectedStudent.getName())) {
                List<Course> enrolledCourses = studentManager.getEnrolledCourses(selectedStudent.getId());
                DefaultComboBoxModel<Course> courseModel = new DefaultComboBoxModel<>();
                courseModel.addElement(new Course(null, "(Select)"));
                for (Course course : enrolledCourses) {
                    courseModel.addElement(course);
                }
                gradeCourseComboBox.setModel(courseModel);
            } else {
                // Clear courseComboBox if no student or "(Select)" is selected
                gradeCourseComboBox.setModel(new DefaultComboBoxModel<>(new Course[] { new Course(null, "(Select)") }));
            }
        });
    }

}