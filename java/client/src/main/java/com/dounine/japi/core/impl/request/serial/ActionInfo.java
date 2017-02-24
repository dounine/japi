package com.dounine.japi.core.impl.request.serial;

import com.dounine.japi.core.impl.request.IRequest;
import com.dounine.japi.core.impl.response.IResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-23.
 */
public class ActionInfo {

    private String actionName;
    private String version;
    private ActionInfoRequest actionInfoRequest;
    private List<IRequest> requestFields;
    private List<IResponse> responseFields;
    private List<ActionInfoDoc> actionInfoDocs = new ArrayList<>();

    public ActionInfoRequest getActionInfoRequest() {
        return actionInfoRequest;
    }

    public void setActionInfoRequest(ActionInfoRequest actionInfoRequest) {
        this.actionInfoRequest = actionInfoRequest;
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

    public List<IRequest> getRequestFields() {
        return requestFields;
    }

    public List<IResponse> getResponseFields() {
        return responseFields;
    }

    public void setResponseFields(List<IResponse> responseFields) {
        this.responseFields = responseFields;
    }

    public void setRequestFields(List<IRequest> requestFields) {
        this.requestFields = requestFields;
    }
}
