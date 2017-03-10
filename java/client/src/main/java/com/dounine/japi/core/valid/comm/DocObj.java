package com.dounine.japi.core.valid.comm;

/**
 * Created by lake on 17-3-10.
 */
public class DocObj {
    private String des = "";
    private boolean req = false;
    private String def = "";
    private String con = "";

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isReq() {
        return req;
    }

    public void setReq(boolean req) {
        this.req = req;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }
}
