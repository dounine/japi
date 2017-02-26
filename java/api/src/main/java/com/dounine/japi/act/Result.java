package com.dounine.japi.act;

/**
 * Created by huanghuanlai on 2017/1/14.
 */
public interface Result<T> {
    
    int getCode();

    String getMsg();

    T getData();
}
