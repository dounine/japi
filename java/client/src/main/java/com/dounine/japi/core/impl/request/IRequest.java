package com.dounine.japi.core.impl.request;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/2/24.
 */
public interface IRequest {

    String getName();
    String getType();
    List<RequestImpl> getFields();
    boolean getRequired();
    String getDefaultValue();
    String getDescription();

}
