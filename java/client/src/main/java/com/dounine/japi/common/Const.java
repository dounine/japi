package com.dounine.japi.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by huanghuanlai on 2017/1/20.
 */
public final class Const {
    private Const(){}//@Pa_sdf() asdf asdf

    public static final String[] MATCH_CHARTS = {"public ", "class ", "interface ", "@interface ", "enum ", "abstract ", "@interface "};
    /**
     * 注释单个值
     */
    public static final String[] SINGLE_DOC_VALUE = {"return"};
    /**
     * 注释名称[  * @return]
     */
    public static final Pattern DOC_NAME = Const.getPattern("[*]\\s[@]\\S*");
    /**
     * 注解
     */
    public static final Pattern ANNOTATION_PATTERN = Const.getPattern("^\\s*[@][\\S]*");
    /**
     * 注释名称跟值[  * @param name]
     */
    public static final Pattern DOC_NAME_VALUE = Const.getPattern("[*]\\s[@]\\S*\\s\\S*");
    /**
     * 注释头[  * (这是注释)]
     */
    public static final Pattern DOC_MORE = Const.getPattern("^(\\s*)[*]\\s");
    /**
     * 方法说明注释
     */
    public static final Pattern DOC_METHOD_FUN_DES = Const.getPattern("^(\\s*)[*]\\s[^@]\\S*");
    /**
     * 注释开始
     */
    public static final Pattern DOC_PATTERN_BEGIN = Const.getPattern("^(\\s*)[/][*]{2}$");
    /**
     * 注释结束
     */
    public static final Pattern DOC_PATTERN_END = Const.getPattern("^(\\s*)[*][/]$");
    /**
     * 方法头行
     */
    public static final Pattern[] METHOD_KEYWORD = {Const.getPattern("^(\\s*)(public)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Const.getPattern("^(\\s*)(private)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Const.getPattern("^(\\s*)(void)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Const.getPattern("^(\\s*)(protected)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$")};
    /**
     * 属性行
     */
    public static final Pattern[] FIELD_KEYWORD = {Const.getPattern("^(\\s*)(public)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[;]$"), Const.getPattern("^(\\s*)(private)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[;]$"), Const.getPattern("^(\\s*)(protected)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[;]$"),Const.getPattern("^(\\s*)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[;]$")};
    /**
     * 方法头返回值部分+( public void user(
     */
    public static final Pattern[] METHOD_RETURN_TYPES = {Const.getPattern("^(\\s*)(public)\\s*(\\S*|\\S*\\s\\S*).\\s*[a-zA-z0-9]*[(]"), Const.getPattern("^(\\s*)(private)\\s*(\\S*|\\S*\\s\\S*).\\s*[a-zA-z0-9]*[(]"),Const.getPattern("^(\\s*)(void)\\s*[a-zA-z0-9]*[(]"), Const.getPattern("^(\\s*)(protected)\\s*(\\S*|\\S*\\s\\S*).\\s*[a-zA-z0-9]*[(]")};
    /**
     * 参数行：(@Validated(value = {IActionMethod.class, IParameterField.class}) User user, String bb, Integer[] last){
     */
    public static final Pattern PARAMETER_BODYS = Const.getPattern("[(][\\S\\s]*[)]\\s*[{]$");
    /**
     * 单个参数：user,
     */
    public static final Pattern PARAMETER_SINGLE_NAME = Const.getPattern("[\\s][a-zA-Z0-9]*[,]");

    public static final Pattern PATTERN_SYM_BEGIN = Const.getPattern("[{]\\s*[\"]");
    public static final Pattern PATTERN_SYM_END = Const.getPattern("[\"]\\s*[}]");

    public static final Pattern REQUEST_ANNO_PATTERN = Const.getPattern("^\\S+[(]");

    public static final Pattern TYPE_NAME_PATTERN = Const.getPattern("(\\S)*(\\s)*(\\S)*$");

    public static final Pattern ANNOTATION = Const.getPattern("@[a-zA-Z0-9_]*");


    private static List<Pattern> PATTERNS;
    public static final Pattern getPattern(String compileValue){
        Pattern contain = null;
        if(null!=PATTERNS&&PATTERNS.size()>0){
            for(Pattern pattern : PATTERNS){
                if(pattern.pattern().equals(compileValue)){
                    contain = pattern;
                    break;
                }
            }
        }else{
            PATTERNS = new ArrayList<>();
        }
        if(null==contain){
            contain = Pattern.compile(compileValue);
            PATTERNS.add(Pattern.compile(compileValue));
        }
        return contain;
    }
}

