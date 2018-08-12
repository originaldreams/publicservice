package com.originaldreams.publicservicecenter.utils;

import java.util.Random;
import java.util.UUID;

/**
 * Created by songx on 2017/3/26.
 */
public class StringUtils {

    public static boolean isEmpty(String text){
        return text == null || text.trim().equals("");
    }



    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }

}
