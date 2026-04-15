package com.academic.model;

import org.bson.types.ObjectId;

public class Grade {
    private ObjectId id;
    private ObjectId studentId;
    private ObjectId subjectId;
    private ObjectId teacherId;
    private double score;

    public Grade() {}
    public Grade(ObjectId studentId, ObjectId subjectId, ObjectId teacherId, double score) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.score = score;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public ObjectId getStudentId() { return studentId; }
    public void setStudentId(ObjectId studentId) { this.studentId = studentId; }

    public ObjectId getSubjectId() { return subjectId; }
    public void setSubjectId(ObjectId subjectId) { this.subjectId = subjectId; }

    public ObjectId getTeacherId() { return teacherId; }
    public void setTeacherId(ObjectId teacherId) { this.teacherId = teacherId; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}
