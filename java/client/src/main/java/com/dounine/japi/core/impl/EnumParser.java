package com.dounine.japi.core.impl;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.common.JapiPattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-3-11.
 */
public class EnumParser {
    private static final EnumParser ENUM_PARSER = new EnumParser();

    private EnumParser() {
    }

    public static final EnumParser getInstance() {
        return ENUM_PARSER;
    }

    public String getTypes(File javaFile) {
        List<String> sb = new ArrayList<>();
        if (javaFile.exists()) {
            try {
                List<String> lines = FileUtils.readLines(javaFile, Charset.forName("utf-8"));
                Pattern enumHeaderPattern = JapiPattern.getPattern("enum\\s*[a-zA-Z0-9_]+");
                Pattern docBegin = JapiPattern.getPattern("/[*]{2}");
                Pattern enumEdnd = JapiPattern.getPattern("^[A-Z0-9_]+");
                List<List<String>> enumAndDocs = new ArrayList<>();
                boolean headerBegin = false;
                List<String> singleEnum = null;
                for (String line : lines) {
                    if (!headerBegin && enumHeaderPattern.matcher(line).find()&&!line.endsWith(";")) {
                        headerBegin = true;
                    }
                    if (headerBegin) {
                        Matcher matcher = docBegin.matcher(line);
                        Matcher enumMatch = enumEdnd.matcher(line.trim());
                        if (null == singleEnum && matcher.find()) {
                            singleEnum = new ArrayList<>();
                            singleEnum.add(matcher.group());
                        } else if (null != singleEnum && enumMatch.find()) {
                            singleEnum.add(line);
                            enumAndDocs.add(singleEnum);
                            singleEnum = null;
                        } else if (null != singleEnum) {
                            singleEnum.add(line);
                        }
                    }
                }
                for (List<String> enumList : enumAndDocs) {
                    String des = null;
                    for (String line : enumList) {
                        if (null == des && line.trim().startsWith("*") && !line.endsWith("*/")) {
                            des = line.trim().substring(1).trim();
                        }
                        Matcher mm = enumEdnd.matcher(line.trim());
                        if (null != des&&mm.find()) {
                            sb.add(mm.group()+":"+des);
                            des = null;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return StringUtils.join(sb.toArray(),",");
    }

    public String getTypes(String javaFilePath) {
        return getTypes(new File(javaFilePath));
    }
}
