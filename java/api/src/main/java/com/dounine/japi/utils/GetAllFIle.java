package com.dounine.japi.utils;

import java.io.File;
import java.util.List;

/**
 * Created by ike on 16-11-1.
 */
public class GetAllFIle {
    public static void getAllFile(File file , List<String> listFiles ){
        File [] files = file.listFiles();
        for(File f : files){
            if(!f.isDirectory()){
                listFiles.add(f.getPath());
            }else{
                getAllFile(f,listFiles);
            }

        }
    }
}
