package com.dounine.japi.act;

/**
 * Created by huanghuanlai on 2017/1/14.
 */
public interface Result<T> {

    /**
     * 状态码
     */
    int getCode();

    /**
     * 错误消息
     */
    String getMsg();

    /**
     * 返回数据
     */
    T getData();
}
