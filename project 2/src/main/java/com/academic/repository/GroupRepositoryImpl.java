package com.academic.repository;

import com.academic.model.Group;
import com.academic.util.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class GroupRepositoryImpl implements IRepository<Group> {
    private final MongoCollection<Document> collection;

    public GroupRepositoryImpl() {
        this.collection = MongoConnection.getDatabase().getCollection("groups");
    }

    @Override
    public void save(Group entity) {
        Document doc = new Document("name", entity.getName())
                .append("studentIds", entity.getStudentIds());
        collection.insertOne(doc);
        entity.setId(doc.getObjectId("_id"));
    }

    @Override
    public Group findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc != null ? documentToGroup(doc) : null;
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();
        for (Document doc : collection.find()) {
            groups.add(documentToGroup(doc));
        }
        return groups;
    }

    @Override
    public void update(Group entity) {
        Document doc = new Document("name", entity.getName())
                .append("studentIds", entity.getStudentIds());
        collection.replaceOne(Filters.eq("_id", entity.getId()), doc);
    }

    @Override
    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Group documentToGroup(Document doc) {
        Group group = new Group(doc.getString("name"));
        group.setId(doc.getObjectId("_id"));
        group.setStudentIds(doc.getList("studentIds", ObjectId.class));
        return group;
    }
}
