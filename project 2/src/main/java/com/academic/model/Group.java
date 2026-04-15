package com.academic.model;

import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

public class Group {
    private ObjectId id;
    private String name;
    private List<ObjectId> studentIds = new ArrayList<>();

    public Group() {}
    public Group(String name) { this.name = name; }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<ObjectId> getStudentIds() { return studentIds; }
    public void setStudentIds(List<ObjectId> studentIds) { this.studentIds = studentIds; }
}
