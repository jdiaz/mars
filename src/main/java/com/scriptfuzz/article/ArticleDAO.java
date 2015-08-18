package com.scriptfuzz.article;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by J. Diaz on 08-12-15.
 */
public class ArticleDAO {


    private final MongoCollection<Document> articlesCollection;

    public ArticleDAO(MongoDatabase blogDatabase) {
            articlesCollection = blogDatabase.getCollection("articles");
    }


}
