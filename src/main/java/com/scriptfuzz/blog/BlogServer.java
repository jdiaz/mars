package com.scriptfuzz.blog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import spark.utils.IOUtils;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static spark.Spark.*;
/**
 * Created by J. Diaz on 08-04-15.
 */
public class BlogServer {

    public static final Logger log = Logger.getLogger(BlogServer.class.getName());

    private static final int CONNECTION_POOLS = 100;
    private final ArticleDAO articleDAO;

    //Todo: Use actual username password combo
    // Right now is being ignored
   public BlogServer(String username, String password, String host, int port, String mode){
       log.info("Server running on port: "+port);
       MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(CONNECTION_POOLS).build();

       final MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), options);
       //final MongoDatabase blogDatabase = mongoClient.getDatabase("blog");
       log.info("Connecting to mongo: "+mongoClient);
       final MongoDatabase blogDatabase = mongoClient.getDatabase("test");
       log.info("Got database: "+blogDatabase);
       articleDAO = new ArticleDAO(blogDatabase);

       // Serve the static files
       if("production".equals(mode))
       {
           staticFileLocation("/public");
       }else externalStaticFileLocation("src/main/resources/public/");


       // Enable CORS
       enableCORS();

       // Initialize roots
       bootstrapRoutes();

       // Load memory cache
       Map<String,String> params = new HashMap<>();
       params.put("year", "2015");
       loadCache(params);
   }

    //Todo: Actually use credentials
    public static void main(String[] args){

        String mode = System.getProperty("mode");
        log.info("Running on mode: "+mode);
        if("production".equalsIgnoreCase(mode)) {
            String username = "Thatguy";
            String password = "thisguy";
            new BlogServer(username, password, "localhost", 27017, mode);
        }else{
            String username = "Thatguy";
            String password = "thisguy";
            new BlogServer(username, password, "localhost", 27017, mode);
        }
    }

    /**
     * Loads recent articles from DB to memory
     * Todo Fix the hardcoded date
     * Todo Preferably provide a range
     */
    private void loadCache(Map<String, String> params){
        log.info("Loading article memory cache");
        //Todo The year should not be hardcoded
        //Fix this crap

        List<Document> recentArticles = articleDAO.findArticlesByFilter(params);
        recentArticles.stream().forEach(a -> ArticleCache.addToCache(a));
    }

    private void bootstrapRoutes() {
        //--------------------------------------------------------------//
        //                          API ROUTES                          //
        //--------------------------------------------------------------//
        get("/", (req, res) -> {
            try (InputStream stream = getClass().getResourceAsStream("/public/index.html")) {
                halt(200, IOUtils.toString(stream));
            } catch (IOException e) {
                log.severe("Error serving index: " + e);
            }
            return 0;
        });

        // Filter to serve index.html for all application routes
        before("/articles/*", (req, res) -> {
            try (InputStream stream = getClass().getResourceAsStream("/public/index.html")) {
                halt(200, IOUtils.toString(stream));
            } catch (IOException e) {
                log.severe("Error serving index: " + e);
            }
        });

        /**
         * Upon hitting root of the page load articles
         * from memory cache instead of going to DB
         * Get recent articles
         * Todo Decide what this will return
         */
        get("/api/cache/load/:year", (req, res) -> {
            String y = req.params("year");
            log.info(y);
            Map<String,String> params = new HashMap<>();
            params.put("year", y);
            List<Document> recent = articleDAO.findArticlesByFilter(params);
            log.info("loading from /api/cache/load/"+y +" total of: "+recent.size());
            int count = ArticleCache.loadCache(recent);
            log.info("Loaded " + count + " articles from db to cache");
            return count;
        });

        /**
         * Test out clearing the cache
         * Fix this crap
         */
        get("/api/cache/clear", (req, res) -> {
            ArticleCache.clearCache();
            return "ArticleCache cleared!";
        });

        get("/api/articles/recent", (req, res) -> toJsonStr(ArticleCache.getRecentArticles()));

        /**
         * Returns all articles in db
         */
        get("/api/articles/all", (req, res) -> {
            String jsonStr = toJsonStr(articleDAO.findAllArticles());
            log.info("Response: " + jsonStr);
            res.status(200);
            res.type("application/json");
            res.body(jsonStr);
            return jsonStr;
        });

        /**
         * Returns all articles by year
         */
        get("/api/articles/year/:year", (req, res) -> {
            String year = req.params("year");
            Map params = new HashMap<String, String>();

            params.put("year", year);
            String jsonStr = toJsonStr(articleDAO.findArticlesByFilter(params));
            log.info("Response: " + jsonStr);
            res.status(200);
            res.type("application/json");
            res.body(jsonStr);
            return jsonStr;
        });


        /**
         * Find article by title
         */
        get("/api/articles/title/:title", (req, res) -> {
            String title = req.params("title");
            Map params = new HashMap<String, String>();
            params.put("title", title);

            String jsonStr = toJsonStr(articleDAO.findArticlesByFilter(params));
            log.info("Response: " + jsonStr);
            res.status(200);
            res.type("application/json");
            res.body(jsonStr);
            return jsonStr;
        });

        /**
         * Find article by author
         */
        get("/api/articles/author/:author", (req, res) -> {
            String author = req.params("author");
            Map params = new HashMap<String, String>();
            params.put("author", author);

            String jsonStr = toJsonStr(articleDAO.findArticlesByFilter(params));
            log.info("Response: " + jsonStr);
            res.status(200);
            res.type("application/json");
            res.body(jsonStr);
            return res;
        });

        get("/api/articles/:year/:title", (req, res) -> {
            String year = req.params(":year");
            String title = req.params(":title");

            Map<String, String> params = new HashMap<>();

            params.put("year", year);
            params.put("title", title);
            String jsonStr = toJsonStr(articleDAO.findArticlesByFilter(params));
            log.info("Response: " + jsonStr);
            res.status(200);
            res.type("application/json");
            res.body(jsonStr);
            return jsonStr;
        });

        /**
         * Add a new article expects the article JSON object
         * to contain a property called 'content' with
         * the markdown representation for your article
         * body
         */
        post("/api/articles/markdown/add", (req, res) -> {
            String markdownArticle = req.body();
            log.fine("Markdown received: " + markdownArticle);

            log.fine("Attempting to parse markdown and add to cache");
            try {
                // Add to DB
                Document articleAdded = articleDAO.addNewMarkdownArticle(markdownArticle);
                // Add to
                ArticleCache.addToCache(articleAdded);
                res.status(200);
                res.type("application/json");
                res.body("{\"success\":true}");
            } catch (Exception e) {
                log.severe("Error adding an article: " +e);
                res.status(200);
                res.type("application/json");
                res.body("{\"success\":false}");
            }
            log.info("Response: " + req.body());
            return res.body();
        });

        /**
         * Add a new article expects the article JSON object
         * to contain a property called 'htmlContent' with
         * the HTML representation for your article
         * body
         */
        post("/api/articles/html/add", (req, res) ->{
            String articleStr = req.body();
            log.fine("HTML received: " + articleStr);

            log.fine("Attempting to parse HTML and add to cache");
            try{
                // Add to DB
                Document articleAdded = articleDAO.addNewHTMLArticle(articleStr);
                // Add to
                ArticleCache.addToCache(articleAdded);
                res.status(200);
                res.type("application/json");
                res.body("{\"success\":true}");
            }catch(Exception e){
                log.severe("Error adding an article: " +e);
                res.status(200);
                res.type("application/json");
                res.body("{\"success\":false}");
            }
            log.info("Response: " + req.body());
            return res.body();
        });

        post("/api/articles/transform", (req, res) -> {
            String markdown = req.body();
            String transformed = articleDAO.transform(markdown);
            Document html = new Document();
            html.append("content", transformed);
            return html.toJson();
        });

    }

    /**
     * Enable CORS for this server
     */
    private void enableCORS(){
        before((req, res) -> {
            // Do not log css & js requests
            if(!(req.uri().contains("/js/") || req.uri().contains("/css/")))
            log.info("Got request: host=" +req.host() + " URL="+req.raw().getRequestURL() +" ContentType=" +req.contentType() + " IP="+req.ip() );
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "X-Requested-With, Content-Type, Content-Length, Authorization");
            res.header("Access-Control-Allow-Headers", "GET,PUT,POST,DELETE,OPTIONS");
        });
    }

    /**
     * Extract the JSON string representation of a List<Document>
     * @param in List of documents to convert to JSON string.
     * @return The JSON string representation.
     */
    private static String toJsonStr(List<Document> in){
       Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
       return gson.toJson(in);
    }
}
