package com.academic.repository;

import com.academic.model.*;
import com.academic.util.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements IUserRepository {
    private final MongoCollection<Document> collection;

    public UserRepositoryImpl() {
        this.collection = MongoConnection.getDatabase().getCollection("users");
    }

    @Override
    public void save(User user) {
        Document doc = userToDocument(user);
        collection.insertOne(doc);
        user.setId(doc.getObjectId("_id"));
    }

    @Override
    public User findByUsername(String username) {
        Document doc = collection.find(Filters.eq("username", username)).first();
        return doc != null ? documentToUser(doc) : null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        for (Document doc : collection.find()) {
            users.add(documentToUser(doc));
        }
        return users;
    }

    @Override
    public List<User> findByRole(String role) {
        List<User> users = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("role", role))) {
            users.add(documentToUser(doc));
        }
        return users;
    }

    @Override
    public void update(User user) {
        collection.replaceOne(Filters.eq("_id", user.getId()), userToDocument(user));
    }

    @Override
    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Document userToDocument(User user) {
        Document doc = new Document("firstName", user.getFirstName())
                .append("lastName", user.getLastName())
                .append("username", user.getUsername())
                .append("password", user.getPassword())
                .append("role", user.getRole());
        
        if (user instanceof Student) {
            doc.append("groupId", ((Student) user).getGroupId());
        }
        return doc;
    }

    private User documentToUser(Document doc) {
        String role = doc.getString("role");
        User user;
        switch (role) {
            case "ADMIN": user = new Administrator(); break;
            case "TEACHER": user = new Teacher(); break;
            case "STUDENT": 
                user = new Student();
                ((Student) user).setGroupId(doc.getObjectId("groupId"));
                break;
            default: throw new IllegalArgumentException("Unknown role: " + role);
        }
        user.setId(doc.getObjectId("_id"));
        user.setFirstName(doc.getString("firstName"));
        user.setLastName(doc.getString("lastName"));
        user.setUsername(doc.getString("username"));
        user.setPassword(doc.getString("password"));
        return user;
    }
}
