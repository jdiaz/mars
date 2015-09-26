package com.scriptfuzz.blog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.eclipse.jetty.server.Authentication;
import org.jasypt.util.password.StrongPasswordEncryptor;
import spark.Response;
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

    private static final String API_CONTEXT = "/api/";
    private static final int CONNECTION_POOLS = 100;
    private static int port;
    private final ArticleDAO articleDAO;
    private final UserDAO userDAO;

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
       userDAO = new UserDAO(blogDatabase);
       // Serve the static files
       if("production".equals(mode)) staticFileLocation("/public");
       else externalStaticFileLocation("src/main/resources/public/");

       // Enable CORS
       enableCORS();

       // Initialize roots
       bootstrapRoutes();

       // Load memory cache
       Map<String,Object> params = new HashMap<>();
       params.put("year", 2015);
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
    private void loadCache(Map<String, Object> params){
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

        post("/auth/login", (req, res) -> {
            // Get the user login form data
            log.info("Attempting to login");
            Document payload = Document.parse(req.body());
            String email = payload.getString("email");
            String password = payload.getString("password");
            log.info("credentials: email="+email);

            // Find the user with such credentails
            Document user = null;
            if(email != null && password != null) {
                user = userDAO.findUser(email);
            }
            else{
                // If the user is not in db return guest
                final String jsonStr = "{\"email\": \"non@gmail.com\","+
                        "\"name\": \"Any\","+
                        "\"type\": \"guest\"}";
                setResponseMeta(res, "application/json", 200, jsonStr);
                return res.body();
            }

            // Authenticate the user
            log.info("user found: " + user.toJson());
            boolean authorized = Util.authenticate(password , user.getString("password"));
                Document respUser = new Document();
                respUser.append("type", user.getString("type"));
                respUser.append("email", user.getString("email"));

            log.info("Attempted user login access, success: "+authorized);
            if(authorized){

                String jsonStr = respUser.toJson();
                setResponseMeta(res, "application/json", 200, jsonStr);
            }
            else{
                final String jsonStr = "{\"email\": \"non@gmail.com\","+
                        "\"name\": \"Any\","+
                        "\"type\": \"guest\"}";
                setResponseMeta(res, "application/json", 200, jsonStr);
            }
            return res.body();
        });

        // Secure the api routes
        before(API_CONTEXT +"*", (req, res) -> {
            log.info("Attemping to access API routes: "+req.host());
            System.out.println(req.raw().getHeader("mars-gui"));
            boolean authorized = true;
            final String localhost = "localhost:4567";
            final String scriptfuzz = "scriptfuzz.com";
            if( !localhost.equalsIgnoreCase(req.host()) ){
                authorized = false;
                log.severe("Unauthorized API access attempt success: "+authorized + " req.host="+req.host());
            }

            if(!authorized) {
                halt(401);
                res.redirect("/");
                log.info("Authorized API access: "+authorized);
            }
            log.info("Authorized API access: "+authorized);
        });

        /**
         * Upon hitting root of the page load articles
         * from memory cache instead of going to DB
         * Get recent articles
         * Todo Decide what this will return
         */
        get(API_CONTEXT+ "/cache/load/:year", (req, res) -> {
            String y = req.params("year");
            Map<String,Object> params = new HashMap<>();
	    int count = 0;
	    try{
              params.put("year", Integer.parseInt(y));
              List<Document> recent = articleDAO.findArticlesByFilter(params);
              log.info("loading from /api/cache/load/"+y +" total of: "+recent.size());
              count = ArticleCache.loadCache(recent);
            }catch(Exception e){
              log.severe("Error parsing year to Int.");
            }
	    log.info("Loaded " + count + " articles from db to cache");
            return count;
        });

        /**
         * Test out clearing the cache
         * Fix this crap
         */
        get(API_CONTEXT + "cache/clear", (req, res) -> {
            ArticleCache.clearCache();
            return "ArticleCache cleared!";
        });

        get(API_CONTEXT + "articles/recent", (req, res) -> {
            String jsonStr = fromListToJsonStr(ArticleCache.getRecentArticles());
            setResponseMeta(res, "application/json", 200, jsonStr);
            return res.body();
        });

        /**
         * Returns all articles in db
         */
        get(API_CONTEXT + "articles/all", (req, res) -> {
            String jsonStr = fromListToJsonStr(articleDAO.findAllArticles());
            setResponseMeta(res, "application/json", 200, jsonStr);
            return res.body();
        });

        /**
         * Returns all articles by year
         */
        get(API_CONTEXT + "articles/year/:year", (req, res) -> {
            String year = req.params("year");
            Map<String,Object> params = new HashMap<>();
	    String jsonStr = "[]";
	    try{
               params.put("year", Integer.parseInt(year) );
	       jsonStr = fromListToJsonStr(articleDAO.findArticlesByFilter(params));	
	    }
	    catch(Exception e){
		log.severe("Error parsing year to Int type.");	
	    }
            setResponseMeta(res, "application/json", 200, jsonStr);
            return res.body();
        });


        /**
         * Find article by title
         */
        get(API_CONTEXT + "articles/title/:title", (req, res) -> {
            String title = req.params("title");
            Map<String,Object> params = new HashMap<>();
            params.put("title", title);

            String jsonStr = fromListToJsonStr(articleDAO.findArticlesByFilter(params));
            setResponseMeta(res, "application/json", 200, jsonStr);
            return res.body();
        });


        /**
         * Find article by author
         */
        get(API_CONTEXT + "articles/author/:author", (req, res) -> {
            String author = req.params("author");
            Map<String,Object> params = new HashMap<>();
            params.put("author", author);

            String jsonStr = fromListToJsonStr(articleDAO.findArticlesByFilter(params));
            setResponseMeta(res, "application/json", 200, jsonStr);
            return res.body();
        });

        get(API_CONTEXT + "articles/:year/:title", (req, res) -> {
            String year = req.params(":year");
            String title = req.params(":title");

            Map<String,Object> params = new HashMap<>();
            String jsonStr = "[]";
 	    try{
	      params.put("year", Integer.parseInt(year));
              params.put("title", title);
              jsonStr = fromListToJsonStr(articleDAO.findArticlesByFilter(params));
            }
	    catch(Exception e){
              log.severe("Error parsing year to Int.");
            }	
	    setResponseMeta(res, "application/json", 200, jsonStr);
            return res.body();
        });

        /**
         * Add a new article expects the article JSON object
         * to contain a property called 'content' with
         * the markdown representation for your article
         * body
         */
        post(API_CONTEXT + "articles/markdown/add", (req, res) -> {
            String markdownArticle = req.body();
            log.fine("Markdown received: " + markdownArticle);

            log.fine("Attempting to parse markdown and add to cache");
            try {
                // Add to DB
                Document articleAdded = articleDAO.addNewMarkdownArticle(markdownArticle);
                // Add to
                ArticleCache.addToCache(articleAdded);
                String jsonStr = "{\"success\":true}";
                setResponseMeta(res, "application/json", 200, jsonStr);
            } catch (Exception e) {
                log.severe("Error adding an article: " +e);
                String jsonStr = "{\"success\":false}";
                setResponseMeta(res, "application/json", 200, jsonStr);
            }
            return res.body();
        });

        /**
         * Add a new article expects the article JSON object
         * to contain a property called 'htmlContent' with
         * the HTML representation for your article
         * body
         */
        post(API_CONTEXT + "articles/html/add", (req, res) ->{
            String articleStr = req.body();
            log.fine("HTML received: " + articleStr);

            log.fine("Attempting to parse HTML and add to cache");
            try{
                // Add to DB
                Document articleAdded = articleDAO.addNewHTMLArticle(articleStr);
                // Add to
                ArticleCache.addToCache(articleAdded);
                String jsonStr = "{\"success\":true}";
                setResponseMeta(res, "application/json", 200, jsonStr);
            }catch(Exception e){
                log.severe("Error adding an article: " +e);
                String jsonStr = "{\"success\":false}";
                setResponseMeta(res, "application/json", 200, jsonStr);
            }
            return res.body();
        });

        post(API_CONTEXT + "articles/transform", (req, res) -> {
            String markdown = req.body();
            String transformed = articleDAO.transform(markdown);
            Document html = new Document();
            html.append("content", transformed);

            setResponseMeta(res,"application/json",200, html.toJson());
            return res.body();
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
    private static String fromListToJsonStr(List<Document> in){
       Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
       return gson.toJson(in);
    }

    /**
     * Sets HTTP Response meta data
     * @param res The response being sent
     * @param contentType The content type of the result
     * @param code The HTTP status code
     * @param body The body of the response
     */
    private static void setResponseMeta(Response res, String contentType, int code, String body){
        log.fine("Response: " + body);
        res.status(code);
        res.type(contentType);
        res.body(body);
    }
}
