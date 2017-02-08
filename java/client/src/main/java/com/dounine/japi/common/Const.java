package com.dounine.japi.common;

import java.util.regex.Pattern;

/**
 * Created by huanghuanlai on 2017/1/20.
 */
public final class Const {
    private Const(){}

    public static final String[] MATCH_CHARTS = {"public ", "class ", "interface ", "@interface ", "enum ", "abstract ", "@interface "};
    /**
     * 注释单个值
     */
    public static final String[] SINGLE_DOC_VALUE = {"return"};
    /**
     * 注释名称[  * @return]
     */
    public static final Pattern DOC_NAME = Pattern.compile("[*]\\s[@]\\S*");
    /**
     * 注解
     */
    public static final Pattern ANNOTATION_PATTERN = Pattern.compile("^\\s*[@][\\S]*");
    /**
     * 注释名称跟值[  * @param name]
     */
    public static final Pattern DOC_NAME_VALUE = Pattern.compile("[*]\\s[@]\\S*\\s\\S*");
    /**
     * 注释头[  * (这是注释)]
     */
    public static final Pattern DOC_MORE = Pattern.compile("^(\\s*)[*]\\s");
    /**
     * 方法说明注释
     */
    public static final Pattern DOC_METHOD_FUN_DES = Pattern.compile("^(\\s*)[*]\\s[^@]\\S*");
    /**
     * 注释开始
     */
    public static final Pattern DOC_PATTERN_BEGIN = Pattern.compile("^(\\s*)[/][*]{2}$");
    /**
     * 注释结束
     */
    public static final Pattern DOC_PATTERN_END = Pattern.compile("^(\\s*)[*][/]$");
    /**
     * 方法头行
     */
    public static final Pattern[] METHOD_KEYWORD = {Pattern.compile("^(\\s*)(public)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Pattern.compile("^(\\s*)(private)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Pattern.compile("^(\\s*)(void)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Pattern.compile("^(\\s*)(protected)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$")};
    /**
     * 属性行
     */
    public static final Pattern[] FIELD_KEYWORD = {Pattern.compile("^(\\s*)(public)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[;]$"), Pattern.compile("^(\\s*)(private)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[;]$"), Pattern.compile("^(\\s*)(protected)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[;]$"),Pattern.compile("^(\\s*)(\\s*)(\\S*|\\S*\\s\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[;]$")};
    /**
     * 方法头返回值部分+( public void user(
     */
    public static final Pattern[] METHOD_RETURN_TYPES = {Pattern.compile("^(\\s*)(public)\\s*(\\S*|\\S*\\s\\S*).\\s*[a-zA-z0-9]*[(]"), Pattern.compile("^(\\s*)(private)\\s*(\\S*|\\S*\\s\\S*).\\s*[a-zA-z0-9]*[(]"), Pattern.compile("^(\\s*)(void)\\s*[a-zA-z0-9]*[(]"), Pattern.compile("^(\\s*)(protected)\\s*(\\S*|\\S*\\s\\S*).\\s*[a-zA-z0-9]*[(]")};
    /**
     * 参数行：(@Validated(value = {IActionMethod.class, IParameterField.class}) User user, String bb, Integer[] last){
     */
    public static final Pattern PARAMETER_BODYS = Pattern.compile("[(][\\S\\s]*[)]\\s*[{]$");
    /**
     * 单个参数：user,
     */
    public static final Pattern PARAMETER_SINGLE_NAME = Pattern.compile("[\\s][a-zA-Z0-9]*[,]");

    public static final Pattern PATTERN_SYM_BEGIN = Pattern.compile("[{]\\s{0,}[\"]");
    public static final Pattern PATTERN_SYM_END = Pattern.compile("[\"]\\s{0,}[}]");
}
