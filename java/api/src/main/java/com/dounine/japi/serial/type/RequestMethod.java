package com.dounine.japi.serial.type;

/**
 * Created by lake on 17-2-8.
 */
public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE,
    ALL;

    public static RequestMethod match(String methodStr){
        if(methodStr.equals(RequestMethod.GET.name())){
            return RequestMethod.GET;
        }else if(methodStr.equals(RequestMethod.HEAD.name())){
            return RequestMethod.HEAD;
        }else if(methodStr.equals(RequestMethod.POST.name())){
            return RequestMethod.POST;
        }else if(methodStr.equals(RequestMethod.PUT.name())){
            return RequestMethod.PUT;
        }else if(methodStr.equals(RequestMethod.PATCH.name())){
            return RequestMethod.PATCH;
        }else if(methodStr.equals(RequestMethod.DELETE.name())){
            return RequestMethod.DELETE;
        }else if(methodStr.equals(RequestMethod.OPTIONS.name())){
            return RequestMethod.OPTIONS;
        }else if(methodStr.equals(RequestMethod.TRACE.name())){
            return RequestMethod.TRACE;
        }else if(methodStr.equals(RequestMethod.ALL.name())){
            return RequestMethod.ALL;
        }
        return RequestMethod.GET;
    }
}
