package com.scriptfuzz.blog;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Simple memory caching of the articles
 * Created by zeek on 08-25-15.
 */
public class ArticleCache {

    public static final int CACHE_MAX_SIZE = 5;
    public static final Logger log = Logger.getLogger(ArticleCache.class.getName());
    private static volatile List<Document> articleCache = new ArrayList<>();

    public static synchronized void clearCache(){
        // Help garbage collector
        log.info("Clearing cache");
        articleCache.stream().forEach(article -> article=null);
        articleCache = new ArrayList<>();
    }

    public static synchronized void addToCache(Document doc){
        log.info("Adding to cache: "+doc);
        if(articleCache.size() >= CACHE_MAX_SIZE)
        {
            log.fine("Cache size < "+CACHE_MAX_SIZE);
            articleCache.remove(0);
            articleCache.add(0,doc);
        }else{

            log.fine("Cache size > "+CACHE_MAX_SIZE);
            articleCache.add(0,doc);
        }
        articleCache.stream().forEach(a -> log.info("Adding article: "+a.get("title") +" to cache"));

    }

    public static synchronized List<Document> getRecentArticles(){
        log.fine("Recent articles in cache: "+articleCache);
        return articleCache;
    }
}
