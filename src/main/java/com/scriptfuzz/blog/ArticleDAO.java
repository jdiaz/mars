package com.scriptfuzz.blog;

import com.github.rjeschke.txtmark.Processor;
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
    public List<Document> findAllArticles(){

        Document test2 = new Document(); // I will populate
        test2.append("_id", false);
        List<Document> all = articlesCollection.find().into(new ArrayList<>());
        System.out.println("Returning this from DAO: "+all);
        return all;
    }// So if my new query must look like this: db.articles.find({},{"_id":false})
     // I need to provide the query in the same fashion articles being the articlesCollection
     // However, the api shows the find() only to take 1 filter. That is to say one Document
     // How to achieve the above?

    /**
     * Finds an article based on a parameter map
     * @param params The parameters to which match the article to
     * @return JSON string representation of the article matching the parameters
     */
    public List<Document> findArticlesByFilter(Map<String, String> params){

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

        return result;
    }

    /**
     * Insert an article represented as a markdown string
     * @param jsonArticle The markdown represenation of the article
     */
    public void addNewMarkdownArticle(String jsonArticle){
        // Create a MongoDb document
        Document article = Document.parse(jsonArticle);
        // Todo: use a real logger :p
        System.out.println("Lets very it parsed correctly: "+article.toJson());

        String htmlBody = parseMarkdown(article.getString("content"));

        article.put("content",htmlBody);

        System.out.println("Verifying document before insertion: "+article.toJson());
        articlesCollection.insertOne(article);

    }

    /**
     * Parses a markdown string into HTML
     * @param markdown The markdown to parse
     * @return
     */
    private String parseMarkdown(String markdown){
        //Todo: Add the necesary logic to parse the markdown into HTML
        return Processor.process(markdown);
    }


}
