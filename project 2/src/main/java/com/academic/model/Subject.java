package com.academic.model;

import org.bson.types.ObjectId;

public class Subject {
    private ObjectId id;
    private String name;
    private int credits;
    private ObjectId teacherId;

    public Subject() {}
    public Subject(String name, int credits) {
        this.name = name;
        this.credits = credits;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public ObjectId getTeacherId() { return teacherId; }
    public void setTeacherId(ObjectId teacherId) { this.teacherId = teacherId; }
}
