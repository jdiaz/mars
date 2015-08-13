package com.scriptfuzz.article;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

/**
 * Created by J. Diaz on 08-12-15.
 */
public class DBConnector {

    private static int CONNECTION_POOLS = 100;

    public static MongoClient client;
    public static MongoDatabase db;

    public static void createMongoClientConnection(String host, int port, String mode) {

        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(CONNECTION_POOLS).build();

        client = new MongoClient(new ServerAddress(host, port), options);

        if("production".equalsIgnoreCase(mode))
         db = client.getDatabase("production");
        else db = client.getDatabase("dev");
    }
}
