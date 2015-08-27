package com.scriptfuzz.blog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
/**
 * Created by J. Diaz on 08-04-15.
 */
public class BlogController {
    private static int CONNECTION_POOLS = 100;

    private final FreemarkerConfiguration cfg;
    private final ArticleDAO articleDAO;

    //Todo: Use actual username password combo
    // Right now is being ignored
   public BlogController(String username, String password, String host, int port){
       MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(CONNECTION_POOLS).build();

       final MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), options);
       //final MongoDatabase blogDatabase = mongoClient.getDatabase("blog");
       final MongoDatabase blogDatabase = mongoClient.getDatabase("test");
       articleDAO = new ArticleDAO(blogDatabase);

       cfg = new FreemarkerConfiguration();
       // Serve the static files
       externalStaticFileLocation("src/main/resources");
       bootstrapRoutes();
       loadCache();
   }

    //Todo: Actually use credentials
    public static void main(String[] args){

        String mode = System.getProperty("mode");
        if("production".equalsIgnoreCase(mode)) {
            String username = "Thatguy";
            String password = "thisguy";
            new BlogController(username, password, "localhost", 3000);
        }else{
            String username = "Thatguy";
            String password = "thisguy";
            new BlogController(username, password, "localhost", 27017);
        }


    }

    private void loadCache(){
        Map<String,String> params = new HashMap<>();
        //Todo The year should not be hardcoded
        params.put("date", "2015");
        List<Document> recentArticles = articleDAO.findArticlesByFilter(params);
        recentArticles.stream().forEach(a -> ArticleCache.addToCache(a));
    }

    private void bootstrapRoutes(){
        //--------------------------------------------------------------//
        //                          API ROUTES                          //
        //--------------------------------------------------------------//
        /**
         * Displays the api
         */
        get("/all", (req, res) -> cfg.getHtmlApi());

        /**
         * Returns all articles in db
         */
        get("/article/all", (req, res) -> {

          String jsonStr = toJsonStr(articleDAO.findAllArticles());
          res.status(200);
          res.type("application/json");
          res.body(jsonStr);
          return jsonStr;
        });


        /**
         * Returns all articles by year
         */
        get("/article/year/:year", (req, res) -> {
            String year = req.params("year");
            Map params = new HashMap<String, String>();

            params.put("year", year);
            System.out.println("Params: "+params.toString());
            String jsonStr = toJsonStr(articleDAO.findArticlesByFilter(params));
            res.status(200);
            res.type("application/json");
            res.body(jsonStr);
            return jsonStr;
        });

        /**
         * Find article by title
         */
        get("/article/title/:title", (req, res) -> {
            String title = req.params("title");
            Map params = new HashMap<String, String>();
            params.put("title", title);

            System.out.println("Params: "+params.toString());
            String jsonStr = toJsonStr(articleDAO.findArticlesByFilter(params));
            System.out.println("/article/title => "+jsonStr);
            res.status(200);
            res.type("application/json");
            res.body(jsonStr);
            return jsonStr;
        });

        /**
         * Find article by author
         */
        get("/article/author/:author", (req, res) -> {
            String author = req.params("author");
            Map params = new HashMap<String, String>();
            params.put("author", author);

            System.out.println("Params: "+params.toString());
            String jsonStr = toJsonStr(articleDAO.findArticlesByFilter(params));
            res.status(200);
            res.type("application/json");
            res.body(jsonStr);
            return res;
        });

        get("/article/:year/:title", (req, res) -> {
            String year = req.params(":year");
            String title = req.params(":title");

            Map params = new HashMap<String, String>();

            params.put("year", year);
            params.put("title", title);
            System.out.println("Params: "+params.toString());
            String jsonStr = toJsonStr(articleDAO.findArticlesByFilter(params));
            res.status(200);
            res.type("application/json");
            res.body(jsonStr);
            return res;
        });

        post("/article/add", (req, res) -> {
           String markdownArticle = req.body();
           System.out.println("Markdown recieved: "+markdownArticle);

           try{
               articleDAO.addNewMarkdownArticle(markdownArticle);
               res.status(200);
               res.type("application/json");
               res.body("{\"success\":true}");
           }catch(Exception e){
               res.status(200);
               res.type("application/json");
               res.body("{\"success\":false}");
           }

           return res.body();
        });


    }

    private static String toJsonStr(List<Document> in){
      Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
       return gson.toJson(in);
    }
}
