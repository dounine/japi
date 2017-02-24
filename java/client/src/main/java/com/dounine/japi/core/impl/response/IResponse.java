package com.dounine.japi.core.impl.response;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/2/24.
 */
public interface IResponse {
    String getName();

    String getType();

    String getDescription();

    String getDefaultValue();

    List<IResponse> getFields();
}
