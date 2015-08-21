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

    /**
     * Finds all articles in the collection
     * Returns them a a json string representation
     * @return JSON string representation of all articles
     */
    public String findAllArticles(){
        List<Document> all = articlesCollection.find().into(new ArrayList<>());
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(all);
        System.out.println("Returning this from DAO: "+json);
        return json;
    }

    /**
     * Finds an article based on a parameter map
     * @param params The parameters to which match the article to
     * @return JSON string representation of the article matching the parameters
     */
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

    /**
     * Insert an article represented as a markdown string
     * @param markdownArticle The markdown represenation of the article
     */
    public void addNewMarkdownArticle(String markdownArticle){
        // Create a MongoDb document
        Document article = new Document();

        //Todo: Add the necesary logic to build the article document
        String htmlArticle = parseMarkdown(markdownArticle);

        articlesCollection.insertOne(article);
    }

    /**
     * Parses a markdown string into HTML
     * @param markdown The markdown to parse
     * @return
     */
    private String parseMarkdown(String markdown){
        //Todo: Add the necesary logic to parse the markdown into HTML
     return "";
    }


}
