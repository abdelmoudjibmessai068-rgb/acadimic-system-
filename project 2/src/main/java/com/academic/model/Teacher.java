package com.academic.model;

public class Teacher extends User {
    public Teacher() {
        super();
        setRole("TEACHER");
    }

    public Teacher(String firstName, String lastName) {
        super(firstName, lastName, "TEACHER");
    }

    @Override
    public String showDashboard() {
        return "Teacher Dashboard: " + getFirstName() + " " + getLastName();
    }
}
