package com.dounine.japi;

/**
 * Created by ike on 16-9-13.
 */
public enum Sex {
    MAN(0),
    FIMAIL(1);

    private int code;

    Sex(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
