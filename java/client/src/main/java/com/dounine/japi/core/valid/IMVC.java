package com.dounine.japi.core.valid;

import com.dounine.japi.core.IFieldDoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by lake on 17-2-13.
 */
public interface IMVC {

    String getRequestParamName();

//    default String getRequestInfo(String annoStr,String typeStr,String nameStr){
//        return null;
//    }

    default String getRequestInfo(String annoStr, String typeStr, String nameStr,List<String> docs){
        return null;
    }

    default String getRequestInfoForField(String annoStr, String typeStr, String nameStr, List<IFieldDoc> docs,List<String> interfaceGroups){
        return null;
    }
}
