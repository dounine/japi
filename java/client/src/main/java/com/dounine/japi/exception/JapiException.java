package com.dounine.japi.exception;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class JapiException extends RuntimeException {
    public JapiException(){

    }
    public JapiException(String msg){
        super(msg);
    }
}
