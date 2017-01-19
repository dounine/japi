package com.dounine.japi.core.impl;

import com.dounine.japi.core.IProject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class ProjectImpl implements IProject {
    /**
     *
     * @param a
     * @param b
     * @param c
     * @param pp
     * @return
     */
    @GetMapping
    public String mm(@Validated StringUtils a, String b, int c, double pp){
        return "";
    }
}
