package com.academic.model;

public class Administrator extends User {
    public Administrator() {
        super();
        setRole("ADMIN");
    }

    public Administrator(String firstName, String lastName) {
        super(firstName, lastName, "ADMIN");
    }

    @Override
    public String showDashboard() {
        return "Admin Dashboard: " + getFirstName() + " " + getLastName();
    }
}
