package com.scriptfuzz.blog;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple memory caching of the articles
 * Created by zeek on 08-25-15.
 */
public class ArticleCache {

    private static int CACHE_MAX_SIZE = 5;
    private static volatile List<Document> articleCache = new ArrayList<>();

    public static synchronized void clearCache(){
        // Help garbage collector
        articleCache.stream().forEach(article -> article=null);
        articleCache = new ArrayList<>();
    }

    public static synchronized void addToCache(Document doc){
        if(articleCache.size() >= CACHE_MAX_SIZE)
        {
            articleCache.remove(0);
            articleCache.add(0,doc);
        }else{
            articleCache.add(0,doc);
        }
        articleCache.stream().forEach(a -> System.out.println(a.get("title")));

    }

    public static synchronized List<Document> getRecentArticles(){
        return articleCache;
    }
}
