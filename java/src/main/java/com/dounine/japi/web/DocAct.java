package com.dounine.japi.web;

import com.dounine.japi.annotation.MethodVersion;
import com.dounine.japi.format.JSPFormat;
import com.dounine.japi.utils.JspFileDealUtil;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ike on 16-10-28.
 */


@RestController
@RequestMapping("interfaceApiDoc")
public class DocAct {

    @Autowired
    private Render render;


    @GetMapping("tpl/{act}")
    public void index1(@PathVariable String act) {
        render.show(act.replace("-", "/"));
    }

    @GetMapping("findInterface")
    public void findInterfaceApiDoc() {
        render.show("index");
    }

}

