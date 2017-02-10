package com.dounine.japi.core.impl;

import com.dounine.japi.core.annotation.IActionRequest;
import com.dounine.japi.core.annotation.impl.ActionRequest;
import com.dounine.japi.core.annotation.impl.ActionRequestImpl;
import com.dounine.japi.core.type.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-9.
 */
public class MVCActionRequest {

    private static List<IActionRequest> actionRequests = new ArrayList<>();
    static {
        actionRequests.add(new ActionRequestImpl(RequestMethod.GET, "org.springframework.web.bind.annotation.GetMapping", true, "value"));
        actionRequests.add(new ActionRequestImpl(RequestMethod.POST, "org.springframework.web.bind.annotation.PostMapping", true, "value"));
        actionRequests.add(new ActionRequestImpl(RequestMethod.PUT, "org.springframework.web.bind.annotation.PutMapping", true, "value"));
        actionRequests.add(new ActionRequestImpl(RequestMethod.DELETE, "org.springframework.web.bind.annotation.DeleteMapping", true, "value"));
        actionRequests.add(new ActionRequestImpl(RequestMethod.PATCH, "org.springframework.web.bind.annotation.PatchMapping", true, "value"));
        ActionRequestImpl allMethod = new ActionRequestImpl(RequestMethod.ALL, "org.springframework.web.bind.annotation.RequestMapping", true, "value", "method");
        List<String[]> methodValues = new ArrayList<>();
        methodValues.add(new String[]{"org.springframework.web.bind.annotation.RequestMethod.GET", RequestMethod.GET.name()});
        methodValues.add(new String[]{"org.springframework.web.bind.annotation.RequestMethod.POST", RequestMethod.POST.name()});
        methodValues.add(new String[]{"org.springframework.web.bind.annotation.RequestMethod.HEAD", RequestMethod.HEAD.name()});
        methodValues.add(new String[]{"org.springframework.web.bind.annotation.RequestMethod.PUT", RequestMethod.PUT.name()});
        methodValues.add(new String[]{"org.springframework.web.bind.annotation.RequestMethod.PATCH", RequestMethod.PATCH.name()});
        methodValues.add(new String[]{"org.springframework.web.bind.annotation.RequestMethod.DELETE", RequestMethod.DELETE.name()});
        methodValues.add(new String[]{"org.springframework.web.bind.annotation.RequestMethod.OPTIONS", RequestMethod.OPTIONS.name()});
        methodValues.add(new String[]{"org.springframework.web.bind.annotation.RequestMethod.TRACE", RequestMethod.TRACE.name()});
        allMethod.setMethodFieldValues(methodValues);
        actionRequests.add(allMethod);
    }

    public static List<IActionRequest> getMVCActionRequest(){
        return actionRequests;
    }
}
