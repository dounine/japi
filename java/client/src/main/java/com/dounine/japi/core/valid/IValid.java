package com.dounine.japi.core.valid;

import com.dounine.japi.core.IParameter;

import java.util.List;

/**
 * Created by lake on 17-2-10.
 */
public interface IValid {

    default boolean isValid(String annoStr) {
        return null!=getValid(annoStr);
    }

    default IMVC getValid(String annoStr) {
        String anno = null;
        if (annoStr.startsWith("@")) {
            if(annoStr.indexOf("(")!=-1){
                anno = annoStr.substring(1,annoStr.indexOf("("));
            }else{
                anno = annoStr.substring(1);
            }
        }else{
            if(annoStr.indexOf("(")!=-1){
                anno = annoStr.substring(0,annoStr.indexOf("("));
            }else{
                anno = annoStr;
            }
        }
        for (IMVC imvc : getTypes()) {
            if (imvc.getRequestParamName().endsWith(anno)) {
                return imvc;
            }
        }
        return null;
    }

    List<IMVC> getTypes();

    IParameter getParameter(String parameterStr);
}
