package com.scriptfuzz.blog;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeek on 08-25-15.
 */
public class ArticleCache {

    private static volatile List<Document> articleCache = new ArrayList<>();

    public static void clearCache(){
        // Help garbage collector
        articleCache.stream().forEach(article -> article=null);
        articleCache = new ArrayList<>();
    }

    public static void addToCache(Document doc){
        articleCache.add(doc);
    }
}
