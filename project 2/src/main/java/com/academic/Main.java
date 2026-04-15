package com.academic;

import com.academic.model.Administrator;
import com.academic.model.User;
import com.academic.service.AcademicService;
import com.academic.ui.LoginForm;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize Service and check for Admin
        AcademicService service = new AcademicService();
        ensureAdminExists(service);

        // Launch Login Form
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }

    private static void ensureAdminExists(AcademicService service) {
        try {
            List<User> admins = service.getUsersByRole("ADMIN");
            if (admins.isEmpty()) {
                // Default admin: Admin/Admin (First Name/Last Name logic)
                Administrator admin = new Administrator("Admin", "Admin");
                service.createUser(admin);
                System.out.println("Default administrator created: Admin / Admin");
            }
        } catch (Exception e) {
            System.err.println("Database connection failed. Please ensure MongoDB is running on port 27017.");
            JOptionPane.showMessageDialog(null, 
                "Could not connect to MongoDB.\nEnsure it is running locally on port 27017.", 
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
