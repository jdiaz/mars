package com.scriptfuzz.blog;

import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Processor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by J. Diaz on 08-12-15.
 */
public class ArticleDAO {

    public static final Logger log = Logger.getLogger(ArticleDAO.class.getName());
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

        Document filter = new Document(); // I will populate
        filter.append("_id", false);
        log.fine("Filter: "+filter);
        List<Document> all = articlesCollection.find().into(new ArrayList<>());
        log.info("Returning this from DAO: "+all);
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
        //Todo Need this => db.articles.find({},{_id: false})

        // Find all parameters to filter by and add them to the mongoDB filter
        Set keys = params.keySet();
        keys.stream().forEach( (key)-> {
            filter.append(key.toString(), params.get(key));
        });

        log.fine("Searching with filter: "+filter.toJson());
        List<Document> result = articlesCollection.find(filter).into(new ArrayList<>());
        log.fine("Result: "+result);

        return result;
    }

    /**
     * Insert an article represented as a markdown string
     * @param jsonArticle The markdown represenation of the article
     */
    public Document addNewMarkdownArticle(String jsonArticle){
        // Create a MongoDb document
        Document article = Document.parse(jsonArticle);
        log.info("Received article: "+article.toJson());

        String htmlBody = parseMarkdown(article.getString("content"));

        article.put("content", htmlBody);
        article.put("preview", makePreview(htmlBody));
        log.info("Verifying document before insertion: "+article.toJson());
        articlesCollection.insertOne(article);
        log.fine("Document inserted.");
        return article;
    }

    /**
     * Inserts an article represented as HTML string
     * @param jsonArticle The HTML representation of the article
     * @return The inserted article.
     */
    public Document addNewHTMLArticle(String jsonArticle){
        Document article = Document.parse(jsonArticle);

        log.info("Received article: "+article.toJson());

        article.put("preview", makePreview(article.getString("content")) );
        log.info("Verifying document before insertion: "+article.toJson());
        articlesCollection.insertOne(article);
        log.fine("Document inserted.");
        return article;
    }

    /**
     * Transform the markdown to HTML
     * @param markdown HTML string to convert to markdown
     * @return HTML string representation of the markdown
     */
    public String transform(String markdown){
        return parseMarkdown(markdown);
    }

    /**
     * Generates preview from html code
     * @param html The html to generate the preview from
     * @return First paragraph of the html content
     */
    private static String makePreview(String html){
        String pCloseTag = "</p>";

        int pCloseIndex = html.indexOf(pCloseTag);
        String preview = html.substring(0 , pCloseIndex + pCloseTag.length() + 1);
        log.info("Created: "+preview);
        return preview;
    }
    /**
     * Parses a markdown string into HTML
     * @param markdown The markdown to parse
     * @return
     */
    private static String parseMarkdown(String markdown){

        Configuration.Builder c = Configuration.builder();
        c.setCodeBlockEmitter(new CodeBlockEmitter());
        c.setAllowSpacesInFencedCodeBlockDelimiters(true);
        c.forceExtentedProfile();
        c.setEncoding("UTF-8");

        String res = Processor.process(markdown, c.build());
        log.info("Markdown HTML equivalent: "+res);
        return res;
    }


}
