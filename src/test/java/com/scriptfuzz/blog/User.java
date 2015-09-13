package com.scriptfuzz.blog;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * Created by zeek on 09-13-15.
 */
public class User {

    private static final int CONNECTION_POOLS = 10;

    /**
     * Insert new user
     * @param args
     */
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(CONNECTION_POOLS).build();

        final MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), options);
        //final MongoDatabase blogDatabase = mongoClient.getDatabase("blog");
        final MongoDatabase blogDatabase = mongoClient.getDatabase("test");

        MongoCollection colls = blogDatabase.getCollection("users");

        String email = "myEmail";
        String password = "myPassword";

        StrongPasswordEncryptor ecrypter = new StrongPasswordEncryptor();
        String encryptedPassword = ecrypter.encryptPassword(password);

        String type = "admin";

        Document d = new Document();
        d.append("email", email);
        d.append("password", encryptedPassword);
        d.append("type", type);

        colls.insertOne(d);
    }
}
