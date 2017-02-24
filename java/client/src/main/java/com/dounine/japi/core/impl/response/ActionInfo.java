package com.dounine.japi.core.impl.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-23.
 */
public class ActionInfo {

    private String actionName;
    private String version;
    private ActionInfoRequest actionInfoRequest;
    private String requestInfoStr;
    private String responseInfoStr;
    private List<ActionInfoDoc> actionInfoDocs = new ArrayList<>();

    public ActionInfoRequest getActionInfoRequest() {
        return actionInfoRequest;
    }

    public void setActionInfoRequest(ActionInfoRequest actionInfoRequest) {
        this.actionInfoRequest = actionInfoRequest;
    }

    public String getRequestInfoStr() {
        return requestInfoStr;
    }

    public void setRequestInfoStr(String requestInfoStr) {
        this.requestInfoStr = requestInfoStr;
    }

    public String getResponseInfoStr() {
        return responseInfoStr;
    }

    public void setResponseInfoStr(String responseInfoStr) {
        this.responseInfoStr = responseInfoStr;
    }

    public List<ActionInfoDoc> getActionInfoDocs() {
        return actionInfoDocs;
    }

    public void setActionInfoDocs(List<ActionInfoDoc> actionInfoDocs) {
        this.actionInfoDocs = actionInfoDocs;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
