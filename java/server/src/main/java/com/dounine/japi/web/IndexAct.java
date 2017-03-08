package com.dounine.japi.web;

import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by huanghuanlai on 2017/1/15.
 */
@Controller
public class IndexAct {

    @GetMapping({"/", ""})
    public Result index(HttpServletResponse response) {
        return new ResultImpl("welcome to japi server home.");
    }


}
