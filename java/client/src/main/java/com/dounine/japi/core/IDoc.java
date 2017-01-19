package com.dounine.japi.core;

import com.dounine.japi.core.type.DocType;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/19.
 */
public interface IDoc {

    DocType getDocType();

    String getName();

    String getValue();

    String getDes();

}
