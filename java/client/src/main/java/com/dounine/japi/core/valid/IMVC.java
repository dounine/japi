package com.dounine.japi.core.valid;

import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.serial.request.IRequest;
import com.dounine.japi.serial.request.RequestImpl;

import java.io.File;
import java.util.List;

/**
 * Created by lake on 17-2-13.
 */
public interface IMVC {

    String getRequestParamName();

//    default String getRequestInfo(String annoStr,String typeStr,String nameStr){
//        return null;
//    }

    default IRequest getRequestField(String annoStr, String typeStr, String nameStr, List<String> docs, File javaFile){
        return null;
    }

//    default String getRequestInfo(String annoStr, String typeStr, String nameStr,List<String> docs,File javaFile){
//        return null;
//    }

//    default String getRequestInfoForField(String annoStr, String typeStr, String nameStr, List<IFieldDoc> docs,List<String> interfaceGroups){
//        return null;
//    }

    default IRequest getRequestFieldForField(String annoStr, String typeStr, String nameStr, List<IFieldDoc> docs, List<String> interfaceGroups){
        return null;
    }
}
