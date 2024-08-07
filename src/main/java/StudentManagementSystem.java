import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentManagementSystem {
    private JFrame frame;
    private JPanel mainPanel, studentManagementPanel, courseEnrollmentPanel, gradeManagementPanel;
    private JTable studentTable;
    private StudentManager studentManager;

    public StudentManagementSystem() {
        studentManager = new StudentManager();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Student Management System");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Main panel with a GridLayout
        JPanel actionPanel = new JPanel(new GridLayout(1, 3));

        // Panels for different actions
        actionPanel.add(createStudentActionsPanel());
        actionPanel.add(createCourseActionsPanel());
        actionPanel.add(createGradeActionsPanel());

        frame.add(actionPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createStudentActionsPanel() {
        JPanel studentActionsPanel = new JPanel(new GridLayout(3, 1));

        JButton addStudentButton = new JButton("Add Student");
        addStudentButton.addActionListener(e -> openAddStudentDialog());

        JButton updateStudentButton = new JButton("Update Student");
        updateStudentButton.addActionListener(e -> openUpdateStudentDialog());

        JButton viewDetailsButton = new JButton("View Student Details");
        viewDetailsButton.addActionListener(e -> viewStudentDetails());

        studentActionsPanel.add(addStudentButton);
        studentActionsPanel.add(updateStudentButton);
        studentActionsPanel.add(viewDetailsButton);

        return studentActionsPanel;
    }

    private JPanel createCourseActionsPanel() {
        JPanel courseActionsPanel = new JPanel(new GridLayout(2, 1));

        JButton addCourseButton = new JButton("Add Course");
        addCourseButton.addActionListener(e -> openAddCourseDialog());

        JButton enrollButton = new JButton("Enroll Student");
        enrollButton.addActionListener(e -> openEnrollStudentDialog());

        courseActionsPanel.add(addCourseButton);
        courseActionsPanel.add(enrollButton);

        return courseActionsPanel;
    }

    private JPanel createGradeActionsPanel() {
        JPanel gradeActionsPanel = new JPanel(new GridLayout(1, 1));

        JButton gradeManagementButton = new JButton("Grade Management");
        gradeManagementButton.addActionListener(e -> showGradeManagementDialog());

        gradeActionsPanel.add(gradeManagementButton);

        return gradeActionsPanel;
    }

    private JPanel createStudentManagementPanel() {
        studentManagementPanel = new JPanel(new BorderLayout());

        // This panel displays the student table
        studentTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(studentTable);
        studentManagementPanel.add(scrollPane, BorderLayout.CENTER);

        // Update the student table with current data
        updateStudentTable();

        return studentManagementPanel;
    }

    private JPanel createCourseEnrollmentPanel() {
        courseEnrollmentPanel = new JPanel();
        courseEnrollmentPanel.setLayout(new BorderLayout());

        JButton enrollButton = new JButton("Enroll Student");

        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Select Student:"));
        JComboBox<Student> studentComboBox = new JComboBox<>(studentManager.getStudents().toArray(new Student[0]));
        formPanel.add(studentComboBox);

        formPanel.add(new JLabel("Select Course:"));
        JComboBox<Course> courseComboBox = new JComboBox<>(studentManager.getCourses().toArray(new Course[0]));
        formPanel.add(courseComboBox);

        courseEnrollmentPanel.add(formPanel, BorderLayout.CENTER);
        courseEnrollmentPanel.add(enrollButton, BorderLayout.SOUTH);

        enrollButton.addActionListener(e -> {
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();

            if (selectedStudent == null || selectedCourse == null) {
                JOptionPane.showMessageDialog(frame, "Please select a student and a course", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String studentId = selectedStudent.getId();
            String courseCode = selectedCourse.getCode();

            studentManager.enrollStudent(studentId, courseCode);
            JOptionPane.showMessageDialog(frame, "Student enrolled successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        return courseEnrollmentPanel;
    }

    private JPanel createGradeManagementPanel() {
        gradeManagementPanel = new JPanel(new BorderLayout());

        JButton gradeManagementButton = new JButton("Grade Management");
        gradeManagementButton.addActionListener(e -> showGradeManagementDialog());

        gradeManagementPanel.add(gradeManagementButton, BorderLayout.CENTER);

        return gradeManagementPanel;
    }

    private void showGradeManagementDialog() {
        JDialog dialog = new JDialog(frame, "Grade Management", true);
        dialog.setSize(1200, 600);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        JComboBox<Student> studentComboBox = new JComboBox<>(studentManager.getStudents().toArray(new Student[0]));
        panel.add(new JLabel("Select Student:"), BorderLayout.NORTH);
        panel.add(studentComboBox, BorderLayout.CENTER);

        // Table to display courses and grades
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Course", "Grade"}, 0);
        JTable gradeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gradeTable);
        panel.add(scrollPane, BorderLayout.SOUTH);

        dialog.add(panel, BorderLayout.CENTER);

        studentComboBox.addActionListener(e -> {
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();
            if (selectedStudent != null) {
                updateGradeTable(selectedStudent, tableModel);
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void updateGradeTable(Student student, DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Clear existing rows

        List<Grade> grades = studentManager.getGrades(student.getId());
        for (Grade grade : grades) {
            tableModel.addRow(new Object[]{grade.getCourseCode(), grade.getGrade()});
        }
    }

    private void viewStudentDetails() {
        JDialog dialog = new JDialog(frame, "View Student Details", true);
        dialog.setSize(400, 300);

        String[] columns = {"Student ID", "Student Name"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Student student : studentManager.getStudents()) {
            model.addRow(new Object[]{student.getId(), student.getName()});
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openAddStudentDialog() {
        JDialog dialog = new JDialog(frame, "Add Student", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new GridLayout(3, 2));

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField();
        JLabel studentNameLabel = new JLabel("Student Name:");
        JTextField studentNameField = new JTextField();

        JButton addButton = new JButton("Add");

        dialog.add(studentIdLabel);
        dialog.add(studentIdField);
        dialog.add(studentNameLabel);
        dialog.add(studentNameField);
        dialog.add(new JLabel()); // Empty cell
        dialog.add(addButton);

        addButton.addActionListener(e -> {
            String studentId = studentIdField.getText();
            String studentName = studentNameField.getText();

            if (studentId.isEmpty() || studentName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "ID and Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student student = new Student(studentId, studentName);
            studentManager.addStudent(student);
            updateStudentTable();
            dialog.dispose();
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openUpdateStudentDialog() {
        JDialog dialog = new JDialog(frame, "Update Student", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(4, 2));

        JLabel selectStudentLabel = new JLabel("Select Student:");
        JComboBox<Student> updateStudentComboBox = new JComboBox<>();

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

        updateStudentComboBox.setModel(new DefaultComboBoxModel<>(studentManager.getStudents().toArray(new Student[0])));

        dialog.add(selectStudentLabel);
        dialog.add(updateStudentComboBox);
        dialog.add(studentIdLabel);
        dialog.add(studentIdField);
        dialog.add(studentNameLabel);
        dialog.add(studentNameField);
        dialog.add(new JLabel()); // Empty cell
        dialog.add(updateButton);

        updateButton.addActionListener(e -> {
            Student selectedStudent = (Student) updateStudentComboBox.getSelectedItem();
            if (selectedStudent != null) {
                String newId = studentIdField.getText();
                String newName = studentNameField.getText();

                if (newId.isEmpty() || newName.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "ID and Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                studentManager.updateStudent(selectedStudent.getId(), new Student(newId, newName));
                updateStudentTable();
                dialog.dispose();
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openAddCourseDialog() {
        JDialog dialog = new JDialog(frame, "Add Course", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new GridLayout(3, 2));

        JLabel courseCodeLabel = new JLabel("Course Code:");
        JTextField courseCodeField = new JTextField();
        JLabel courseNameLabel = new JLabel("Course Name:");
        JTextField courseNameField = new JTextField();

        JButton addButton = new JButton("Add");

        dialog.add(courseCodeLabel);
        dialog.add(courseCodeField);
        dialog.add(courseNameLabel);
        dialog.add(courseNameField);
        dialog.add(new JLabel()); // Empty cell
        dialog.add(addButton);

        addButton.addActionListener(e -> {
            String courseCode = courseCodeField.getText();
            String courseName = courseNameField.getText();

            if (courseCode.isEmpty() || courseName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Code and Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Course course = new Course(courseCode, courseName);
            studentManager.addCourse(course);
            dialog.dispose();
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openEnrollStudentDialog() {
        JDialog dialog = new JDialog(frame, "Enroll Student", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new GridLayout(3, 2));

        JLabel studentLabel = new JLabel("Select Student:");
        JComboBox<Student> studentComboBox = new JComboBox<>(studentManager.getStudents().toArray(new Student[0]));
        JLabel courseLabel = new JLabel("Select Course:");
        JComboBox<Course> courseComboBox = new JComboBox<>(studentManager.getCourses().toArray(new Course[0]));
        JButton enrollButton = new JButton("Enroll");

        dialog.add(studentLabel);
        dialog.add(studentComboBox);
        dialog.add(courseLabel);
        dialog.add(courseComboBox);
        dialog.add(new JLabel()); // Empty cell
        dialog.add(enrollButton);

        enrollButton.addActionListener(e -> {
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();

            if (selectedStudent == null || selectedCourse == null) {
                JOptionPane.showMessageDialog(dialog, "Please select a student and a course", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String studentId = selectedStudent.getId();
            String courseCode = selectedCourse.getCode();

            studentManager.enrollStudent(studentId, courseCode);
            JOptionPane.showMessageDialog(dialog, "Student enrolled successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void updateStudentTable() {
        String[] columns = {"Student ID", "Student Name"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Student student : studentManager.getStudents()) {
            model.addRow(new Object[]{student.getId(), student.getName()});
        }

        studentTable.setModel(model);
    }
}
