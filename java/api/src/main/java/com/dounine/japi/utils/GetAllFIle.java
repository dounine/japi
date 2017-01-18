package com.dounine.japi.utils;

import java.io.File;
import java.util.List;

/**
 * Created by ike on 16-11-1.
 */
public class GetAllFIle {
    public static List<String> getAllFile(File file , List<String> list ){
        File [] files = file.listFiles();
        for(File f : files){
            if(!f.isDirectory()){
                list.add(f.getPath());
            }else{
                getAllFile(f,list);
            }

        }
        return list;
    }
}
