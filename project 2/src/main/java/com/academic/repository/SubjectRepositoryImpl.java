package com.academic.repository;

import com.academic.model.Subject;
import com.academic.util.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class SubjectRepositoryImpl implements IRepository<Subject> {
    private final MongoCollection<Document> collection;

    public SubjectRepositoryImpl() {
        this.collection = MongoConnection.getDatabase().getCollection("subjects");
    }

    @Override
    public void save(Subject entity) {
        Document doc = new Document("name", entity.getName())
                .append("credits", entity.getCredits())
                .append("teacherId", entity.getTeacherId());
        collection.insertOne(doc);
        entity.setId(doc.getObjectId("_id"));
    }

    @Override
    public Subject findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc != null ? documentToSubject(doc) : null;
    }

    @Override
    public List<Subject> findAll() {
        List<Subject> subjects = new ArrayList<>();
        for (Document doc : collection.find()) {
            subjects.add(documentToSubject(doc));
        }
        return subjects;
    }

    @Override
    public void update(Subject entity) {
        Document doc = new Document("name", entity.getName())
                .append("credits", entity.getCredits())
                .append("teacherId", entity.getTeacherId());
        collection.replaceOne(Filters.eq("_id", entity.getId()), doc);
    }

    @Override
    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    public List<Subject> findByTeacherId(ObjectId teacherId) {
        List<Subject> subjects = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("teacherId", teacherId))) {
            subjects.add(documentToSubject(doc));
        }
        return subjects;
    }

    private Subject documentToSubject(Document doc) {
        Subject subject = new Subject(doc.getString("name"), doc.getInteger("credits"));
        subject.setId(doc.getObjectId("_id"));
        subject.setTeacherId(doc.getObjectId("teacherId"));
        return subject;
    }
}
