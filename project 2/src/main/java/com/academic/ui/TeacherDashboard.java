package com.academic.ui;

import com.academic.model.*;
import com.academic.service.AcademicService;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeacherDashboard extends JFrame {
    private final User teacher;
    private final AcademicService service;
    private JComboBox<SubjectWrapper> subjectCombo;
    private JTable studentsTable;
    private DefaultTableModel tableModel;

    public TeacherDashboard(User teacher, AcademicService service) {
        this.teacher = teacher;
        this.service = service;
        initUI();
    }

    private void initUI() {
        setTitle("Teacher Dashboard - " + teacher.getFirstName());
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Subject:"));
        subjectCombo = new JComboBox<>();
        List<Subject> subjects = service.getSubjectsByTeacher(teacher.getId());
        subjects.forEach(s -> subjectCombo.addItem(new SubjectWrapper(s)));
        subjectCombo.addActionListener(e -> loadStudents());
        topPanel.add(subjectCombo);

        tableModel = new DefaultTableModel(new String[]{"Student ID", "Student Name", "Grade"}, 0);
        studentsTable = new JTable(tableModel);
        
        JButton btnSave = new JButton("Save Grade");
        btnSave.addActionListener(e -> saveGrade());

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(studentsTable), BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);

        if (subjectCombo.getItemCount() > 0) loadStudents();
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        // For prototype, show all students or filter by group if group was assigned to subject
        // Let's just show all students for now
        List<User> students = service.getUsersByRole("STUDENT");
        SubjectWrapper selected = (SubjectWrapper) subjectCombo.getSelectedItem();
        if (selected == null) return;

        for (User s : students) {
            List<Grade> grades = service.getGradesForStudent(s.getId());
            double score = 0;
            for (Grade g : grades) {
                if (g.getSubjectId().equals(selected.subject.getId())) {
                    score = g.getScore();
                    break;
                }
            }
            tableModel.addRow(new Object[]{s.getId(), s.getFirstName() + " " + s.getLastName(), score});
        }
    }

    private void saveGrade() {
        int row = studentsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student");
            return;
        }

        ObjectId studentId = (ObjectId) tableModel.getValueAt(row, 0);
        SubjectWrapper selected = (SubjectWrapper) subjectCombo.getSelectedItem();
        String scoreStr = JOptionPane.showInputDialog("Enter Grade for " + tableModel.getValueAt(row, 1));
        
        if (scoreStr != null) {
            try {
                double score = Double.parseDouble(scoreStr);
                service.enterGrade(studentId, selected.subject.getId(), teacher.getId(), score);
                loadStudents();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid score");
            }
        }
    }

    private static class SubjectWrapper {
        Subject subject;
        SubjectWrapper(Subject s) { this.subject = s; }
        @Override public String toString() { return subject.getName(); }
    }
}
