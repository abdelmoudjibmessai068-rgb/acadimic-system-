package com.academic.model;

import org.bson.types.ObjectId;

public class Student extends User {
    private ObjectId groupId;

    public Student() {
        super();
        setRole("STUDENT");
    }

    public Student(String firstName, String lastName) {
        super(firstName, lastName, "STUDENT");
    }

    public ObjectId getGroupId() { return groupId; }
    public void setGroupId(ObjectId groupId) { this.groupId = groupId; }

    @Override
    public String showDashboard() {
        return "Student Dashboard: " + getFirstName() + " " + getLastName();
    }
}
