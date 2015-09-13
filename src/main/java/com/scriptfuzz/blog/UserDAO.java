package com.scriptfuzz.blog;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by zeek on 09-13-15.
 */
public class UserDAO {

    public static final Logger log = Logger.getLogger(UserDAO.class.getName());
    private final MongoCollection usersCollection;

    /**
     * Constructcs a connection to a database collection
     * @param blogDatabase
     */
    public UserDAO(MongoDatabase blogDatabase){
        usersCollection = blogDatabase.getCollection("users");
    }

    /**
     * Find a user in the collection by email.
     * @param email The email to search the user by.
     * @return The user document
     */
    public Document findUser(String email){
        Document filter = new Document();
        filter.append("email", email);

        List<Document> users = (ArrayList<Document>) usersCollection.find(filter).into(new ArrayList<>());
        return users.get(0);
    }

}
