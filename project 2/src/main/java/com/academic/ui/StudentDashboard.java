package com.academic.ui;

import com.academic.model.*;
import com.academic.service.AcademicService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame {
    private final User student;
    private final AcademicService service;

    public StudentDashboard(User student, AcademicService service) {
        this.student = student;
        this.service = service;
        initUI();
    }

    private void initUI() {
        setTitle("Student Dashboard - " + student.getFirstName());
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Subject", "Credits", "Grade"}, 0);
        JTable table = new JTable(model);

        List<Grade> grades = service.getGradesForStudent(student.getId());
        for (Grade g : grades) {
            Subject s = service.getSubjectById(g.getSubjectId());
            String subjectName = (s != null) ? s.getName() : "Unknown";
            int credits = (s != null) ? s.getCredits() : 0;
            model.addRow(new Object[]{subjectName, credits, g.getScore()});
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JLabel lblTotal = new JLabel("Welcome, " + student.getFirstName() + " " + student.getLastName());
        lblTotal.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(lblTotal, BorderLayout.NORTH);
    }
}
