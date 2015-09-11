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
        log.fine("Adding to cache: "+doc);
        String title = doc.getString("title");
        List<Document> result = articleCache.stream()
                                            .filter(d -> title.equals(d.getString("title")))
                                            .collect(Collectors.toList());
        if(result.size() > 0){
            // means the article is already in the list
            // need its location to update it
            System.out.println("fired!");
            for(int i=0; i<articleCache.size(); i++){
                if(articleCache.get(i).getString("title").equals(title)){
                    articleCache.add(i, doc);
                }
            }
        }
        else {
            if (articleCache.size() >= CACHE_MAX_SIZE) {
                log.fine("Cache size < " + CACHE_MAX_SIZE);
                articleCache.remove(articleCache.size() - 1);
                articleCache.add(0, doc);
            } else {

                log.fine("Cache size > " + CACHE_MAX_SIZE);
                articleCache.add(0, doc);
            }
            articleCache.stream().forEach(a -> log.fine("Adding article: " + a.get("title") + " to cache"));
        }
    }

    public static synchronized List<Document> getRecentArticles(){
        log.fine("Recent articles in cache: "+articleCache);
        return articleCache;
    }

    public static synchronized int loadCache(List<Document> recent){
        for(Document d: recent) {
            log.info("Loading: "+d.getString("title"));
            articleCache.add(d);
        }
        return articleCache.size();
    }
}
