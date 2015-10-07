package com.scriptfuzz.blog;

import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Simple memory caching of the articles
 * Created by zeek on 08-25-15.
 */
public class ArticleCache {

    public static final int CACHE_MAX_SIZE = 15;
    public static final Logger log = Logger.getLogger(ArticleCache.class.getName());
    private static volatile List<Document> articleCache = new ArrayList<>();

    /**
     * Clears List<Document> cache
     */
    public static synchronized void clearCache(){
        log.info("Clearing cache");
        articleCache = new ArrayList<>();
    }

    /**
     * Add a new document to the memory cache
     * However, it checks to see if its already in the list. If so,
     * it replaces it.
     */
    public static synchronized void addToCache(Document doc) {
        log.fine("Adding to cache: " + doc);
        String title = doc.getString("title");

        boolean exist = articleCache.stream()
                                    .filter(d -> d.getString("title").equals(title))
                                    .findFirst()
                                    .isPresent();

        if (exist) {
            // means the article is already in the list
            // need its location to update it
            for (int i = 0; i < articleCache.size(); i++) {
                if (articleCache.get(i).getString("title").equals(title)) {
                    articleCache.set(i, doc);
                    break;
                }
            }
        } // end if
        else {
            if (articleCache.size() >= CACHE_MAX_SIZE) {
                log.fine("Cache size >= " + CACHE_MAX_SIZE);
                articleCache.remove(articleCache.size() - 1);
                articleCache.add(0, doc);
            } else {
                log.fine("Cache size < " + CACHE_MAX_SIZE);
                articleCache.add(0, doc);
            }
        } //end else
    } // end addToCache

    /**
     * Retrieves the article list
     * @return
     */
    public static synchronized List<Document> getRecentArticles(){
        log.fine("Recent articles in cache: "+articleCache);
        return articleCache;
    }

    /**
     * Populates the in memory list.
     * @param recent List of Documents to insert.
     * @return The number of documents inserted to the memory cache.
     */
    public static synchronized int loadCache(List<Document> recent){
        for(Document d: recent) {
            log.info("Loading: "+d.getString("title"));
            articleCache.add(d);
        }
        return articleCache.size();
    }
}
