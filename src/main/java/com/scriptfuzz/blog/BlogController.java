package com.scriptfuzz.blog;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;


import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
/**
 * Created by J. Diaz on 08-04-15.
 */
public class BlogController {
    public static int CONNECTION_POOLS = 100;

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
       bootstrapRoutes();
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

    private void bootstrapRoutes(){
        //--------------------------------------------------------------//
        //                          API ROUTES                          //
        //--------------------------------------------------------------//
        /**
         * Displays the api
         */
        get("/", (req, res) -> cfg.getHtmlApi());

        /**
         * Returns all articles in db
         */
        get("/article/all", (req, res) -> {

          String jsonStr = articleDAO.findAllArticles();
          res.status(200);
          res.type("application/json");

          return jsonStr;
        });


        get("/article/:year/:title", (req, res) -> {
            final String year = req.params(":year");
            final String title = req.params(":title");

            final Map params = new HashMap<String, String>();

            params.put("year", year);
            params.put("title", title);
            System.out.println("Params: "+params.toString());
            String jsonStr = articleDAO.findArticlesByFilter(params);
            res.status(200);
            res.type("application/json");

            return jsonStr;
        });

        /**
         * Returns all articles by year
         */
        get("/article/:year", (req, res) -> {
            final String year = req.queryParams(":year");
            final Map params = new HashMap<String, String>();
            params.put("year", year);
            System.out.println("Params: "+params.toString());
            String jsonStr = articleDAO.findArticlesByFilter(params);
            res.status(200);
            res.type("application/json");

            return jsonStr;
        });

        /**
         * Find article by title
         */
        get("/article/:title", (req, res) -> {
            final String title = req.queryParams(":title");
            final Map params = new HashMap<String, String>();
            params.put("title", title);

            System.out.println("Params: "+params.toString());
            String jsonStr = articleDAO.findArticlesByFilter(params);
            res.status(200);
            res.type("application/json");

            return jsonStr;
        });

        /**
         * Find article by author
         */
        get("/article/:author", (req, res) -> {
            final String author = req.queryParams(":author");
            final Map params = new HashMap<String, String>();
            params.put("author", author);

            System.out.println("Params: "+params.toString());
            String jsonStr = articleDAO.findArticlesByFilter(params);
            res.status(200);
            res.type("application/json");

            return jsonStr;
        });

    }
}
