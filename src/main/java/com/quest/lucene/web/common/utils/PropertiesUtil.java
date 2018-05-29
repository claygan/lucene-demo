package com.quest.lucene.web.common.utils;

import java.util.ResourceBundle;

/**
 * Created by Quest on 2018/5/29.
 */
public class PropertiesUtil {
    public static String getMimetypeValue(String key){
        ResourceBundle resource = ResourceBundle.getBundle("mimetype");
        return resource.getString(key);
    }
}
