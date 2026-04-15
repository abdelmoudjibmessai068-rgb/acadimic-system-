package com.academic.repository;

import com.academic.model.Grade;
import com.academic.util.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class GradeRepositoryImpl implements IGradeRepository {
    private final MongoCollection<Document> collection;

    public GradeRepositoryImpl() {
        this.collection = MongoConnection.getDatabase().getCollection("grades");
    }

    @Override
    public void save(Grade entity) {
        Document doc = gradeToDocument(entity);
        collection.insertOne(doc);
        entity.setId(doc.getObjectId("_id"));
    }

    @Override
    public Grade findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc != null ? documentToGrade(doc) : null;
    }

    @Override
    public List<Grade> findAll() {
        List<Grade> grades = new ArrayList<>();
        for (Document doc : collection.find()) {
            grades.add(documentToGrade(doc));
        }
        return grades;
    }

    @Override
    public void update(Grade entity) {
        collection.replaceOne(Filters.eq("_id", entity.getId()), gradeToDocument(entity));
    }

    @Override
    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    @Override
    public List<Grade> findByStudentId(ObjectId studentId) {
        List<Grade> grades = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("studentId", studentId))) {
            grades.add(documentToGrade(doc));
        }
        return grades;
    }

    @Override
    public List<Grade> findBySubjectId(ObjectId subjectId) {
        List<Grade> grades = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("subjectId", subjectId))) {
            grades.add(documentToGrade(doc));
        }
        return grades;
    }

    public Grade findByStudentAndSubject(ObjectId studentId, ObjectId subjectId) {
        Document doc = collection.find(Filters.and(
                Filters.eq("studentId", studentId),
                Filters.eq("subjectId", subjectId)
        )).first();
        return doc != null ? documentToGrade(doc) : null;
    }

    private Document gradeToDocument(Grade grade) {
        return new Document("studentId", grade.getStudentId())
                .append("subjectId", grade.getSubjectId())
                .append("teacherId", grade.getTeacherId())
                .append("score", grade.getScore());
    }

    private Grade documentToGrade(Document doc) {
        Grade grade = new Grade(
                doc.getObjectId("studentId"),
                doc.getObjectId("subjectId"),
                doc.getObjectId("teacherId"),
                doc.getDouble("score")
        );
        grade.setId(doc.getObjectId("_id"));
        return grade;
    }
}
