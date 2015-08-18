package com.scriptfuzz.article;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;


import static spark.Spark.*;
/**
 * Created by J. Diaz on 08-04-15.
 */
public class BlogController {
    public static int CONNECTION_POOLS = 100;

    private final FreemarkerConfiguration cfg;
    private final ArticleDAO articleDAO;

   public BlogController(String username, String password, String host, int port){
       MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(CONNECTION_POOLS).build();

       final MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), options);
       final MongoDatabase blogDatabase = mongoClient.getDatabase("blog");

       articleDAO = new ArticleDAO(blogDatabase);

       cfg = new FreemarkerConfiguration();
       bootstrapRoutes();
   }


    public static void main(String[] args){

        String mode = System.getProperty("mode");
        if("production".equalsIgnoreCase(mode)) {
            String username = "Thatguy";
            String password = "thisguy";
            new BlogController(username, password, "localhost", 3000);
        }else{
            String username = "Thatguy";
            String password = "thisguy";
            new BlogController(username, password, "localhost", 3000);
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
        //get("/article/all", (req, res) -> "";


        get("/article/:year/:title", (req, res) -> {
            final String year = req.queryParams(":year");
            final String title = req.queryParams(":title");

            return "";
        });

        /**
         * Returns all articles by year
         */
        get("/article/:year", (req, res) -> {
            final String year = req.queryParams(":year");

            return "";
        });

        /**
         * Find article by date
         */
        get("/article/:date", (req, res) -> {

            return "";
        });

        /**
         * Find article by title
         */
        get("/article/:title", (req, res) -> {

            final String title = req.queryParams(":title");
            return "";
        });

        /**
         * Find article by author
         */
        get("/article/:author", (req, res) -> {

            final String author = req.queryParams(":author");

            return "";
        });

        post("/article/add", (req, res) -> {
            return "";
        });
    }
}
