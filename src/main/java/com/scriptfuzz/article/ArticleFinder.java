package com.scriptfuzz.article;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toList;
/**
 * Created by J. Diaz on 08-04-15.
 */
public class ArticleFinder {
    // Criteria constants
    public static String FIND_ONE_FLAG         = "one";
    public static String FIND_MULTIPLE_FLAG    = "multiple";

    public static String YEAR        = "year";
    public static String DATE        = "date";
    public static String TITLE       = "title";
    public static String AUTHOR      = "author";

    public static Object findByCriteria(Map criteria, String flag)
    {
        // If you need to find one article you may have to match
        // multiple criteria arguments to satisfy the search.
        if(FIND_ONE_FLAG.equalsIgnoreCase(flag)){

            // Find an article matching the criteria map
            findOne(criteria);

        // If you need to need to find multiple articles you
        // will have less criteria to match.
        }else if(FIND_MULTIPLE_FLAG.equalsIgnoreCase(flag)){

            // Find more than one article that matches the
            // the criteria map
            findMultiple(criteria);

        }else {
            // something wrong
        }

        return "";
    }

    public static Object findAll()
    {
        MongoDatabase db = DBConnector.db;
        MongoCollection collection = db.getCollection("articles");

        // find all articles in mongo
        List<Document> allDocs = (List) collection.find().into(new ArrayList<Document>());

        return "";
    }


    //-------------------------------------------------------------//
    //                        Helper Methods                       //
    //-------------------------------------------------------------//

    private static Object findOne(Map criteria)
    {
        // Check if criteria hold more than one parameter
        if(criteria.size() > 1){

            // Get a Set of all keys in the map
            Set<String> keys = criteria.keySet();

            // Loop through all keys. We do not know all
            // the properties we want the article match
            List<String> criteriaValues = keys.stream()
                                              .collect(toList());


        }

        return "";
    }

    private static Object findMultiple(Map criteria)
    {

        return "";
    }



}
