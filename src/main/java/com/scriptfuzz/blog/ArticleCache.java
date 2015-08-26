package com.scriptfuzz.blog;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple memory caching of the articles
 * Created by zeek on 08-25-15.
 */
public class ArticleCache {

    private static volatile List<Document> articleCache = new ArrayList<>();

    public static synchronized void clearCache(){
        // Help garbage collector
        articleCache.stream().forEach(article -> article=null);
        articleCache = new ArrayList<>();
    }

    public static synchronized void addToCache(Document doc){
        articleCache.add(doc);
    }
}
