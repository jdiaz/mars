package com.scriptfuzz.blog;

import org.jasypt.util.password.StrongPasswordEncryptor;

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

}
