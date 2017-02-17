package com.dounine.japi.core.valid;

import com.dounine.japi.core.IParameter;

/**
 * Created by lake on 17-2-10.
 */
public interface IValid {

    boolean isValid(String annoStr);

    IParameter getParameter(String parameterStr);
}
