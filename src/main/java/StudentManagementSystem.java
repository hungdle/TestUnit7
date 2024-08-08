import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentManagementSystem {
    private JFrame frame;
    private JPanel actionPanel, contentPanel;
    private CardLayout cardLayout;
    private JTable studentTable;
    private StudentManager studentManager;

    public StudentManagementSystem() {
        studentManager = new StudentManager();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Student Management System");
        frame.setSize(800, 600);
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

    private void addContentPanels() {
        contentPanel.add(createAddStudentPanel(), "Add Student");
        contentPanel.add(createUpdateStudentPanel(), "Update Student");
        contentPanel.add(createViewStudentDetailsPanel(), "View Student Details");
        contentPanel.add(createAddCoursePanel(), "Add Course");
        contentPanel.add(createEnrollStudentPanel(), "Enroll Student");
        contentPanel.add(createGradeManagementPanel(), "Grade Management");
    }

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
            JOptionPane.showMessageDialog(frame, "Student added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            studentIdField.setText(""); // Clear text fields
            studentNameField.setText("");
            updateStudentTable();
        });

        return panel;
    }

    private JPanel createUpdateStudentPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel selectStudentLabel = new JLabel("Select Student:");
        JComboBox<Student> updateStudentComboBox = new JComboBox<>(studentManager.getStudents().toArray(new Student[0]));
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
                    JOptionPane.showMessageDialog(frame, "ID and Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                studentManager.updateStudent(selectedStudent.getId(), new Student(newId, newName));
                JOptionPane.showMessageDialog(frame, "Student updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateStudentTable();
            }
        });

        return panel;
    }

    private JPanel createViewStudentDetailsPanel() {
        studentTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(studentTable);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

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
                JOptionPane.showMessageDialog(frame, "Code and Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Course course = new Course(courseCode, courseName);
            studentManager.addCourse(course);
            JOptionPane.showMessageDialog(frame, "Course added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            courseCodeField.setText(""); // Clear text fields
            courseNameField.setText("");
        });

        return panel;
    }

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
                JOptionPane.showMessageDialog(frame, "Please select a student and a course", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            studentManager.enrollStudent(selectedStudent.getId(), selectedCourse.getCode());
            JOptionPane.showMessageDialog(frame, "Student enrolled successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateStudentTable();
        });

        return panel;
    }

    private JPanel createGradeManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton gradeManagementButton = new JButton("Grade Management");
        gradeManagementButton.addActionListener(e -> showGradeManagementDialog());
        panel.add(gradeManagementButton, BorderLayout.CENTER);
        return panel;
    }

    private void showGradeManagementDialog() {
        JDialog dialog = new JDialog(frame, "Grade Management", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel studentLabel = new JLabel("Select Student:");
        JComboBox<Student> studentComboBox = new JComboBox<>(studentManager.getStudents().toArray(new Student[0]));

        JLabel courseLabel = new JLabel("Select Course:");
        JComboBox<Course> courseComboBox = new JComboBox<>();

        JLabel gradeLabel = new JLabel("Assign Grade:");
        JTextField gradeField = new JTextField();

        studentComboBox.addActionListener(e -> {
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();
            if (selectedStudent != null) {
                List<Course> enrolledCourses = studentManager.getEnrolledCourses(selectedStudent.getId());
                courseComboBox.setModel(new DefaultComboBoxModel<>(enrolledCourses.toArray(new Course[0])));
            }
        });

        JButton assignGradeButton = new JButton("Assign Grade");
        assignGradeButton.addActionListener(e -> {
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();
            String grade = gradeField.getText();

            if (selectedStudent == null || selectedCourse == null || grade.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please select a student, a course, and enter a grade", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            studentManager.assignGrade(selectedStudent.getId(), selectedCourse.getCode(), grade);
            JOptionPane.showMessageDialog(dialog, "Grade assigned successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        dialog.add(studentLabel);
        dialog.add(studentComboBox);
        dialog.add(courseLabel);
        dialog.add(courseComboBox);
        dialog.add(gradeLabel);
        dialog.add(gradeField);
        dialog.add(new JLabel()); // Empty cell
        dialog.add(assignGradeButton);

        dialog.setVisible(true);
    }


    private void updateStudentTable() {
        if (studentTable != null) {
            viewStudentDetails();
        }
    }

    private void viewStudentDetails() {
        String[] columns = {"ID", "Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        studentTable.setModel(tableModel);

        for (Student student : studentManager.getStudents()) {
            Object[] row = {student.getId(), student.getName()};
            tableModel.addRow(row);
        }
    }

    private void refreshUpdateStudentComboBox() {
        JPanel updateStudentPanel = (JPanel) contentPanel.getComponent(1); // Assuming "Update Student" panel is at index 1
        JComboBox<Student> updateStudentComboBox = (JComboBox<Student>) updateStudentPanel.getComponent(1);
        updateStudentComboBox.setModel(new DefaultComboBoxModel<>(studentManager.getStudents().toArray(new Student[0])));
    }

    private void refreshEnrollStudentComboBoxes() {
        JPanel enrollStudentPanel = (JPanel) contentPanel.getComponent(4); // Assuming "Enroll Student" panel is at index 4
        JComboBox<Course> courseComboBox = (JComboBox<Course>) enrollStudentPanel.getComponent(1);
        JComboBox<Student> studentComboBox = (JComboBox<Student>) enrollStudentPanel.getComponent(3);

        courseComboBox.setModel(new DefaultComboBoxModel<>(studentManager.getCourses().toArray(new Course[0])));
        studentComboBox.setModel(new DefaultComboBoxModel<>(new Student[0])); // Initially empty until course is selected
    }

}
