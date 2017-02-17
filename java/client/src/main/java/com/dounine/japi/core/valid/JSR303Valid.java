package com.dounine.japi.core.valid;

import com.dounine.japi.core.IParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-10.
 */
public class JSR303Valid implements IValid {
    private static final List<ValidAnno> TYPES = new ArrayList<>();
    static {
        ValidAnno validAnno = new ValidAnno();
        validAnno.setName("org.springframework.validation.annotation.Validated");
        validAnno.getValidAnnoVals().add(new ValidAnnoVal("value","Class<?>[]","{}"));
        TYPES.add(validAnno);
    }

    @Override
    public boolean isValid(String annoStr) {
        String anno = null;
        if(annoStr.startsWith("@")){
            anno = annoStr.substring(1);
        }
        for(ValidAnno validAnno : TYPES){
            if(validAnno.getName().endsWith(anno)){
                return true;
            }
        }
        return false;
    }

    @Override
    public IParameter getParameter(String parameterStr) {
        return null;
    }
}
