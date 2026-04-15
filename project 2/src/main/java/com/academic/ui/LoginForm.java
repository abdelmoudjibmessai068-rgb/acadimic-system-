package com.academic.ui;

import com.academic.model.User;
import com.academic.service.AcademicService;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private final AcademicService service;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginForm() {
        this.service = new AcademicService();
        initUI();
    }

    private void initUI() {
        setTitle("Academic System - Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);

        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> performLogin());
        panel.add(btnLogin);

        add(panel);
    }

    private void performLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        try {
            User user = service.login(username, password);
            if (user != null) {
                dispose();
                openDashboard(user);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDashboard(User user) {
        SwingUtilities.invokeLater(() -> {
            JFrame dashboard;
            switch (user.getRole()) {
                case "ADMIN": dashboard = new AdminDashboard(user, service); break;
                case "TEACHER": dashboard = new TeacherDashboard(user, service); break;
                case "STUDENT": dashboard = new StudentDashboard(user, service); break;
                default: return;
            }
            dashboard.setVisible(true);
        });
    }
}
