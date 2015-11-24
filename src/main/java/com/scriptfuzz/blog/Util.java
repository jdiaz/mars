package com.scriptfuzz.blog;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by zeek on 09-13-15.
 */
public class Util {

    public static final Logger log = Logger.getLogger(Util.class.getName());
    /**
     * Athenticate a user
     * @param plain
     * @param encrypted
     * @return
     */
    public static boolean authenticate(String plain, String encrypted){
        log.info("Attempting to authenticate");
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        boolean success = false;
        if(passwordEncryptor.checkPassword(plain, encrypted)){
            // correct
            success = true;
            log.fine("Password match: "+success);
        }else{
            //bad login
            success = false;
            log.fine("Password match: "+success);
        }
        return success;
    }

    /**
     * Reads a property file
     * @param path The path to the property file
     * @return As HashMap of all the properties in the file
     */
    public static HashMap<String, String> readProperties(String path){
        Properties properties = new Properties();
        InputStream in = null;
        HashMap<String, String> propMap = new HashMap<>();
        try{
            in = new FileInputStream(path);
            properties.load(in);
            for(String key: properties.stringPropertyNames()){
                String value = properties.getProperty(key);
                propMap.put(key, value);
            }
        }catch(IOException e) {
            log.severe("Error reading property file: " + e);
        }finally{
            try{
                in.close();
            }catch(IOException e){
              log.severe("Error closing input stream. "+e);
            }
        }
        return propMap;
    }
}
