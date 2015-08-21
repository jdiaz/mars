package com.scriptfuzz.blog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;

/**
 * Created by J. Diaz on 08-12-15.
 */
public class ArticleDAO {

    private final MongoCollection<Document> articlesCollection;

    public ArticleDAO(MongoDatabase blogDatabase) {
            articlesCollection = blogDatabase.getCollection("articles");
    }

    public String findAllArticles(){
        List<Document> all = articlesCollection.find().into(new ArrayList<>());
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(all);
        System.out.println("Returning this from DAO: "+json);
        return json;
    }

    public String findArticlesByFilter(Map<String, String> params){

        // Create a MongoDB filter.
        Document filter = new Document();

        // Find all parameters to filter by and add them to the mongoDB filter
        Set keys = params.keySet();
        keys.stream().forEach( (key)-> {
            // Todo: Might need to convert numbers to native type
            filter.append(key.toString(), params.get(key));
        });
        System.out.println("Final mongoDB filter: "+filter.toJson());
        List<Document> result = articlesCollection.find(filter).into(new ArrayList<>());

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(result);
        return json;
    }


}
