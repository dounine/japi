package com.dounine.japi.web;

/**
 * Created by lake on 17-2-24.
 */
public class ResultImpl implements Result {
    private int code;
    private String msg;
    private Object data;

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
