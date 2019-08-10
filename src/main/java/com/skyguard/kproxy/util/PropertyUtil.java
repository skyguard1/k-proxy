package com.skyguard.kproxy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyUtil.class);

    private static Properties properties;

    private static Properties getProperties() throws Exception{

        if(properties==null){
            properties = new Properties();
            String filePath = System.getProperty("user.dir");
            InputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(new FileInputStream(filePath+"/target/classes/config.properties"));
                properties.load(inputStream);
            }catch (Exception e){
                LOG.error("get file error",e);
            }finally {
                if(inputStream!=null){
                    inputStream.close();
                }
            }
        }

        return properties;
    }



    public static String getValue(String key){

        try {
            String value = getProperties().getProperty(key);
            return value;
        }catch (Exception e){
            LOG.error("get property error",e);
        }

        return null;
    }





}
