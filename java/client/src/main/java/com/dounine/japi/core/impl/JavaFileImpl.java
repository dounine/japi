package com.dounine.japi.core.impl;

import com.dounine.japi.core.IJavaFile;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by huanghuanlai on 2017/1/19.
 */
public class JavaFileImpl implements IJavaFile {

    private static final String CHECK_FILE_SUFFIX = ".java";

    private String javaFilePath;
    private List<String> includePaths = new ArrayList<>();
    private String projectPath;

    @Override
    public File searchTxtJavaFileForProjectsPath(String javaTxt) {
        if (StringUtils.isBlank(javaTxt)) {
            throw new JapiException("javaTxt 关键字不能为空");
        }
        if (StringUtils.isBlank(javaFilePath)) {
            throw new JapiException("javaFilePath 主文件路径不能为空");
        }
        if (null == includePaths || (null != includePaths && includePaths.size() == 0)) {
            throw new JapiException("includePaths 至少包含一个主项目地扯");
        }

        List<String> javaFileLines = null;
        try {
            javaFileLines = FileUtils.readLines(new File(javaFilePath), Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> javaFileImportPackageLines = filterJavaFileImportPackageLines(javaFileLines);
        String searchPath = null;
        for (String line : javaFileImportPackageLines) {//第一层查找
            if (line.endsWith(javaTxt + ";")) {
                searchPath = line;
                break;
            }
        }
        if (null != searchPath) {
            /**
             * 检测文件是否在几个项目当中
             */
            File javaFile = checkFileExists(javaTxt, filterPackageInfo(searchPath), null);
            if (null != javaFile) {
                return javaFile;
            }
        }
        for (String line : javaFileImportPackageLines) {//第二层查找
            if (line.endsWith(".*;")) {
                searchPath = line;
                break;
            }
        }
        if (null != searchPath) {
            /**
             * 检测文件是否在几个项目当中
             */
            File javaFile = checkFileExists(javaTxt, filterPackageInfo(searchPath), null);
            if (null != javaFile) {
                return javaFile;
            }
        }
        //第三层查找，当前包
        File javaFile = checkFileExists(javaTxt, null, javaFilePath);
        if (null != javaFile) {
            return javaFile;
        }
        return null;
    }


    /**
     * 过滤java文件的导入包信息
     *
     * @param javaFileFullLines
     * @return 导入包信息
     */
    private List<String> filterJavaFileImportPackageLines(final List<String> javaFileFullLines) {
        if (null == javaFileFullLines && javaFileFullLines.size() == 0) {
            throw new JapiException("javaFileFullLines 不能为空");
        }
        final List<String> javaFileImportPackageLines = new ArrayList<>();
        String[] matchCharts = {"/**", "public ", "class ", "interface ", "@interface ", "enum ", "abstract ", "@interface "};
        for (String lineString : javaFileFullLines) {
            boolean match = false;//true 找到包结束,意味着导入包结束,严格要求,包中间不能包含注释
            for (String chart : matchCharts) {
                if (lineString.startsWith(chart)) {
                    match = true;
                    break;
                }
            }
            if (match) {
                break;
            } else {
                javaFileImportPackageLines.add(lineString);
            }
        }
        return javaFileImportPackageLines;
    }

    /**
     * 过滤包信息
     *
     * @param packageLineInfo
     * @return 具体包，不包含import与分号
     */
    private static String filterPackageInfo(String packageLineInfo) {
        String[] importAndPack = packageLineInfo.split(" ");
        return StringUtils.substring(importAndPack[1], 0, -1);//去掉分号
    }

    /**
     * 检测文件是否在几个项目当中
     *
     * @param javaName     java名称
     * @param packageName  导包名称
     * @param srcFilePath  源文件路径
     * @return
     */
    private File checkFileExists(String javaName, String packageName, String srcFilePath) {//检测文件是否在几个项目中
        File findFile = null;
        if (StringUtils.isNotBlank(packageName) && !packageName.endsWith("*")) {
            String packageToPath = packageName.replace(".", "/");
            List<String> nList = new ArrayList<>(includePaths);
            nList.add(projectPath);
            for (String projectPath : nList) {
                String splitChar = projectPath.endsWith("/") ? "" : "/";
                String fileAppendPath = new StringBuilder(projectPath).append(splitChar).append(packageToPath).append(CHECK_FILE_SUFFIX).toString();
                File file = new File(fileAppendPath);
                if (file.exists()) {
                    findFile = file;
                    break;
                }
            }
        } else if (StringUtils.isNotBlank(packageName)) {
            String packageToPath = packageName.replace(".", "/");
            packageToPath = StringUtils.substring(packageToPath, 0, -1);//除去*号
            List<String> nList = new ArrayList<>(includePaths);
            nList.add(projectPath);
            for (String projectPath : nList) {
                String splitChar = projectPath.endsWith("/") ? "" : "/";
                String packageFoldPath = new StringBuilder(projectPath).append(splitChar).append(packageToPath).toString();//项目路径+包路径
                File packageFold = new File(packageFoldPath);
                if (packageFold.exists() && packageFold.isDirectory()) {
                    IOFileFilter fileFilter = FileFilterUtils.asFileFilter(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.isFile() && pathname.getName().equals(javaName + CHECK_FILE_SUFFIX);
                        }
                    });
                    Collection<File> packageChildFiles = FileUtils.listFiles(packageFold, fileFilter, TrueFileFilter.INSTANCE);//查找所有文件
                    List<File> files = packageChildFiles.stream().sorted((a, b) -> ((Integer) a.getAbsolutePath().length()).compareTo(b.getAbsolutePath().length())).collect(Collectors.toList());//优先取包层次少的文件
                    findFile = files.get(0);
                }
            }
        } else {//当前包路径
            File srcFile = new File(srcFilePath);
            if (!srcFile.exists()) {
                throw new JapiException("srcFilePath 源文件不存在");
            }
            File srcFold = srcFile.getParentFile();
            IOFileFilter fileFilter = FileFilterUtils.asFileFilter(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().endsWith(javaName + CHECK_FILE_SUFFIX);
                }
            });
            Collection<File> packageFoldChildFiles = FileUtils.listFiles(srcFold, fileFilter, null);
            if (packageFoldChildFiles.size() == 0) {
                throw new JapiException("找不到相关文件:" + javaName + CHECK_FILE_SUFFIX);
            }
            findFile = packageFoldChildFiles.iterator().next();
        }

        return findFile;
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

    public List<String> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(List<String> includePaths) {
        this.includePaths = includePaths;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
}
