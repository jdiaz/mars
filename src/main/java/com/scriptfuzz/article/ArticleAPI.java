package com.scriptfuzz.article;

import java.util.HashMap;
import static spark.Spark.*;
/**
 * Created by J. Diaz on 08-04-15.
 */
public class ArticleAPI {
    public static void main(String[] args){

        APIConfiguration.initializeFreemarker();

        // Get operation mode (dev or production)
        String mode = System.getProperty("mode");

        // Initialize database connection
        DBConnector.createMongoClientConnection("localhost", 3000, mode);

        //--------------------------------------------------------------//
        //                          API ROUTES                          //
        //--------------------------------------------------------------//
        /**
         * Displays the api
         */
        get("/", (req, res) -> APIConfiguration.getHtmlApi());

        /**
         * Returns all articles in db
         */
        get("/article/all", (req, res) -> ArticleFinder.findAll());


        get("/article/:year/:title", (req, res) -> {
            final String year = req.queryParams(":year");
            final String title = req.queryParams(":title");

            final String flag = ArticleFinder.FIND_ONE_FLAG ;
            final HashMap<String, Object> criteria = new HashMap<>();
                criteria.put("date", year);
                criteria.put("title", title);

            return ArticleFinder.findByCriteria(criteria, flag);
        });

        /**
         * Returns all articles by year
         */
        get("/article/:year", (req, res) -> {
            final String year = req.queryParams(":year");
            //final String criteria = ArticleFinder.YEAR;

            final String flag = ArticleFinder.FIND_MULTIPLE_FLAG;
            final HashMap<String, Object> criteria = new HashMap<>();
                criteria.put("year",year);
            return ArticleFinder.findByCriteria(criteria, year);
        });

        /**
         * Find article by date
         */
        get("/article/:date", (req, res) -> {

            final String date = req.queryParams(":date");
            //final String criteria = ArticleFinder.DATE;
            final HashMap<String, Object> criteria = new HashMap<>();
                 criteria.put("date", date);

            return ArticleFinder.findByCriteria(criteria, date);
        });

        /**
         * Find article by title
         */
        get("/article/:title", (req, res) -> {

            final String title = req.queryParams(":title");
            //final String criteria = ArticleFinder.TITLE;
            final HashMap<String, Object> criteria = new HashMap<>();

            return ArticleFinder.findByCriteria(criteria, title);
        });

        /**
         * Find article by author
         */
        get("/article/:author", (req, res) -> {

            final String author = req.queryParams(":author");
          //  final String criteria = ArticleFinder.AUTHOR;
            final HashMap<String, Object> criteria = new HashMap<>();

            return ArticleFinder.findByCriteria(criteria, author);
        });

        post("/article/add", (req, res) -> {
            return "";
        });
    }
}
