package com.dounine.japi.format;

/**
 * Created by ike on 16-10-28.
 */
public class JSPFormat {

    public static String format(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for (int i = 0,len = jsonStr.length(); i < len; i++) {
            char c = jsonStr.charAt(i);
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "</span><br/>\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "</span><br/>\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("</span><br/>\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }

        return jsonForMatStr.toString();

    }

    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("&nbsp;&nbsp;&nbsp;");
        }
        levelStr.replace(0,levelStr.length(),"<span>"+levelStr.toString());
        return levelStr.toString();
    }

}
