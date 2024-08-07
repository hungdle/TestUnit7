import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentManagementSystem {
    private JFrame frame;
    private JComboBox<Student> studentComboBox;
    private JComboBox<Course> courseComboBox;
    private JTable studentTable;
    private JTable gradeTable;
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

        JPanel mainPanel = new JPanel(new GridLayout(1, 3));
        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel studentManagementPanel = createStudentManagementPanel();
        JPanel courseEnrollmentPanel = createCourseEnrollmentPanel();
        JPanel gradeManagementPanel = createGradeManagementPanel();

        mainPanel.add(studentManagementPanel);
        mainPanel.add(courseEnrollmentPanel);
        mainPanel.add(gradeManagementPanel);

        frame.setVisible(true);
    }

    private JPanel createStudentManagementPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton addStudentButton = new JButton("Add Student");
        JButton updateStudentButton = new JButton("Update Student");
        JButton viewStudentDetailsButton = new JButton("View Student Details");

        buttonPanel.add(addStudentButton);
        buttonPanel.add(updateStudentButton);
        buttonPanel.add(viewStudentDetailsButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        studentTable = new JTable();
        JScrollPane studentScrollPane = new JScrollPane(studentTable);
        panel.add(studentScrollPane, BorderLayout.CENTER);

        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddStudentDialog();
            }
        });

        updateStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUpdateStudentDialog();
            }
        });

        viewStudentDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStudentDetails();
            }
        });

        updateStudentTable();
        updateComboBoxes();
        return panel;
    }

    private JPanel createCourseEnrollmentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton enrollButton = new JButton("Enroll Student");

        buttonPanel.add(enrollButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        studentComboBox = new JComboBox<>();
        courseComboBox = new JComboBox<>();

        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Select Student:"));
        formPanel.add(studentComboBox);
        formPanel.add(new JLabel("Select Course:"));
        formPanel.add(courseComboBox);

        panel.add(formPanel, BorderLayout.CENTER);

        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        updateComboBoxes();
        return panel;
    }

    private JPanel createGradeManagementPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton assignGradeButton = new JButton("Assign Grade");

        buttonPanel.add(assignGradeButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        studentComboBox = new JComboBox<>();
        courseComboBox = new JComboBox<>();

        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Select Student:"));
        formPanel.add(studentComboBox);
        formPanel.add(new JLabel("Select Course:"));
        formPanel.add(courseComboBox);

        panel.add(formPanel, BorderLayout.CENTER);

        gradeTable = new JTable();
        JScrollPane gradeScrollPane = new JScrollPane(gradeTable);
        panel.add(gradeScrollPane, BorderLayout.SOUTH);

        assignGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAssignGradeDialog();
            }
        });

        updateComboBoxes();
        return panel;
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

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = studentIdField.getText();
                String studentName = studentNameField.getText();

                if (studentId.isEmpty() || studentName.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "ID and Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Student student = new Student(studentId, studentName);
                studentManager.addStudent(student);
                updateStudentTable();
                updateComboBoxes();
                dialog.dispose();
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openUpdateStudentDialog() {
        JDialog dialog = new JDialog(frame, "Update Student", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new GridLayout(4, 2));

        JLabel selectStudentLabel = new JLabel("Select Student:");
        JComboBox<Student> studentComboBox = new JComboBox<>(studentManager.getStudents().toArray(new Student[0]));
        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField();
        JLabel studentNameLabel = new JLabel("Student Name:");
        JTextField studentNameField = new JTextField();

        JButton updateButton = new JButton("Update");

        dialog.add(selectStudentLabel);
        dialog.add(studentComboBox);
        dialog.add(studentIdLabel);
        dialog.add(studentIdField);
        dialog.add(studentNameLabel);
        dialog.add(studentNameField);
        dialog.add(new JLabel()); // Empty cell
        dialog.add(updateButton);

        studentComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Student selectedStudent = (Student) studentComboBox.getSelectedItem();
                if (selectedStudent != null) {
                    studentIdField.setText(selectedStudent.getId());
                    studentNameField.setText(selectedStudent.getName());
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = studentIdField.getText();
                String studentName = studentNameField.getText();

                if (studentId.isEmpty() || studentName.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "ID and Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Student selectedStudent = (Student) studentComboBox.getSelectedItem();
                if (selectedStudent != null) {
                    studentManager.updateStudent(selectedStudent.getId(), studentId, studentName);
                    updateStudentTable();
                    updateComboBoxes();
                    dialog.dispose();
                }
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openAssignGradeDialog() {
        JDialog dialog = new JDialog(frame, "Assign Grade", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(4, 2));

        JLabel selectStudentLabel = new JLabel("Select Student:");
        JComboBox<Student> studentComboBox = new JComboBox<>(studentManager.getStudents().toArray(new Student[0]));
        JLabel selectCourseLabel = new JLabel("Select Course:");
        JComboBox<Course> courseComboBox = new JComboBox<>(studentManager.getCourses().toArray(new Course[0]));
        JLabel gradeLabel = new JLabel("Grade:");
        JTextField gradeField = new JTextField();

        JButton assignButton = new JButton("Assign");

        dialog.add(selectStudentLabel);
        dialog.add(studentComboBox);
        dialog.add(selectCourseLabel);
        dialog.add(courseComboBox);
        dialog.add(gradeLabel);
        dialog.add(gradeField);
        dialog.add(new JLabel()); // Empty cell
        dialog.add(assignButton);

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Student selectedStudent = (Student) studentComboBox.getSelectedItem();
                Course selectedCourse = (Course) courseComboBox.getSelectedItem();
                String grade = gradeField.getText();

                if (selectedStudent == null || selectedCourse == null || grade.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please select a student, a course, and enter a grade", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                studentManager.assignGrade(selectedStudent.getId(), selectedCourse.getCode(), grade);
                updateGradeTable(selectedStudent.getId());
                dialog.dispose();
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void viewStudentDetails() {
        JDialog dialog = new JDialog(frame, "Student Details", true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());

        List<Student> students = studentManager.getStudents();
        String[] columnNames = {"Student ID", "Student Name"};
        Object[][] data = new Object[students.size()][2];

        for (int i = 0; i < students.size(); i++) {
            data[i][0] = students.get(i).getId();
            data[i][1] = students.get(i).getName();
        }

        JTable studentDetailsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(studentDetailsTable);
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
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
