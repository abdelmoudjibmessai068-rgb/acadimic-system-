package com.academic.ui;

import com.academic.model.*;
import com.academic.service.AcademicService;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private final User admin;
    private final AcademicService service;
    private JTabbedPane tabbedPane;

    public AdminDashboard(User admin, AcademicService service) {
        this.admin = admin;
        this.service = service;
        initUI();
    }

    private void initUI() {
        setTitle("Admin Dashboard - " + admin.getFirstName());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Users", createUsersPanel());
        tabbedPane.addTab("Groups", createGroupsPanel());
        tabbedPane.addTab("Subjects", createSubjectsPanel());

        add(tabbedPane);
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Name", "Username", "Role"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        refreshUsers(model);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add User");
        btnAdd.addActionListener(e -> showAddUserDialog(model));
        btnPanel.add(btnAdd);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshUsers(DefaultTableModel model) {
        model.setRowCount(0);
        List<User> users = service.getAllUsers();
        for (User u : users) {
            model.addRow(new Object[]{u.getId(), u.getFirstName() + " " + u.getLastName(), u.getUsername(), u.getRole()});
        }
    }

    private void showAddUserDialog(DefaultTableModel model) {
        JTextField fName = new JTextField();
        JTextField lName = new JTextField();
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"ADMIN", "TEACHER", "STUDENT"});
        
        Object[] message = {
            "First Name:", fName,
            "Last Name:", lName,
            "Role:", roleBox
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            User newUser;
            String role = (String) roleBox.getSelectedItem();
            if ("ADMIN".equals(role)) newUser = new Administrator(fName.getText(), lName.getText());
            else if ("TEACHER".equals(role)) newUser = new Teacher(fName.getText(), lName.getText());
            else newUser = new Student(fName.getText(), lName.getText());
            
            service.createUser(newUser);
            refreshUsers(model);
        }
    }

    private JPanel createGroupsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
        JTable table = new JTable(model);
        refreshGroups(model);

        JButton btnAdd = new JButton("Add Group");
        btnAdd.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Group Name:");
            if (name != null) {
                service.createGroup(new Group(name));
                refreshGroups(model);
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnAdd, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshGroups(DefaultTableModel model) {
        model.setRowCount(0);
        service.getAllGroups().forEach(g -> model.addRow(new Object[]{g.getId(), g.getName()}));
    }

    private JPanel createSubjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Credits", "Teacher"}, 0);
        JTable table = new JTable(model);
        refreshSubjects(model);

        JButton btnAdd = new JButton("Add Subject");
        btnAdd.addActionListener(e -> showAddSubjectDialog(model));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnAdd, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshSubjects(DefaultTableModel model) {
        model.setRowCount(0);
        service.getAllSubjects().forEach(s -> {
            String teacherName = "Unassigned";
            if (s.getTeacherId() != null) {
                // Simplified lookup
                teacherName = s.getTeacherId().toString();
            }
            model.addRow(new Object[]{s.getId(), s.getName(), s.getCredits(), teacherName});
        });
    }

    private void showAddSubjectDialog(DefaultTableModel model) {
        JTextField name = new JTextField();
        JTextField credits = new JTextField();
        List<User> teachers = service.getUsersByRole("TEACHER");
        JComboBox<UserWrapper> teacherBox = new JComboBox<>();
        teachers.forEach(t -> teacherBox.addItem(new UserWrapper(t)));

        Object[] message = {
            "Name:", name,
            "Credits:", credits,
            "Teacher:", teacherBox
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Subject", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Subject s = new Subject(name.getText(), Integer.parseInt(credits.getText()));
            UserWrapper selected = (UserWrapper) teacherBox.getSelectedItem();
            if (selected != null) s.setTeacherId(selected.user.getId());
            service.createSubject(s);
            refreshSubjects(model);
        }
    }

    private static class UserWrapper {
        User user;
        UserWrapper(User u) { this.user = u; }
        @Override public String toString() { return user.getFirstName() + " " + user.getLastName(); }
    }
}
