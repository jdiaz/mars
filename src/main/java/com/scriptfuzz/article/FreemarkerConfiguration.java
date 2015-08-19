package com.scriptfuzz.article;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J. Diaz on 08-12-15.
 */
public class FreemarkerConfiguration {

    private Configuration cfg;

    public FreemarkerConfiguration(){
        // Configure freemarker template
         cfg = new Configuration(Configuration.VERSION_2_3_23);
        try {
            cfg.setClassForTemplateLoading(BlogController.class, "/");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    /**
     * Build an HTML represenation of all the routes supported in this API
     * @return HTML representation of this API
     * @throws Exception Error Parsing the freemarker template
     */
    public String getHtmlApi() throws Exception{
        Template apiTemplate = cfg.getTemplate("api.ftl");

        Map<String, Object> root = new HashMap<>();

        List<Map> seq = new ArrayList<>();
        Map<String, String> map = new HashMap<>();

        map.put("uri", "/");
        map.put("description", "Display API");

        seq.add(map);

        map = new HashMap<>();
        map.put("uri", "/article/all");
        map.put("description", "Gets all articles");

        seq.add(map);

        map = new HashMap<>();
        map.put("uri", "/article/:year");
        map.put("description", "Get articles that match given year. e.g. 2014, 2015");

        seq.add(map);

        map = new HashMap<>();
        map.put("uri", "/article/:author");
        map.put("description", "Gets all articles that match given author");

        seq.add(map);

        map = new HashMap<>();
        map.put("uri", "/article/:title");
        map.put("description", "Gets all articles that match given title separated by dashes. e.g Awesome-Blog-Article");

        seq.add(map);

        map = new HashMap<>();
        map.put("uri", "/article/:year/:title");
        map.put("description", "Gets article that matches both year & title. e.g. /article/2015/JDiaz");

        seq.add(map);

        // Load the routes onto freemarker processor
        root.put("routes", seq);

        StringWriter writer = new StringWriter();
        apiTemplate.process(root, writer);

        return writer.toString();
    }
}
