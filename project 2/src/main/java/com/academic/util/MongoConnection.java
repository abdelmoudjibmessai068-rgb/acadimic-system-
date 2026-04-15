package com.academic.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create("mongodb://localhost:27017");
                database = mongoClient.getDatabase("academic_system");
            } catch (Exception e) {
                System.err.println("Could not connect to MongoDB: " + e.getMessage());
                throw e;
            }
        }
        return database;
    }
}
