package com.dounine.japi.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huanghuanlai on 2017/1/15.
 */
@RestController
public class IndexAct {

    @GetMapping("/aa")
    public String index(){
        return "/interfaceapidoc/feedbackguide";
    }

}
