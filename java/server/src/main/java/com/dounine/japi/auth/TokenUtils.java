package com.dounine.japi.auth;

import java.util.UUID;

/**
 * Created by huanghuanlai on 2017/2/27.
 */
public class TokenUtils {

    public static String createToken(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
