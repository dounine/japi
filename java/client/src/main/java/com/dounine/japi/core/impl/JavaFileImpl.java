package com.dounine.japi.core.impl;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.core.IJavaFile;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by huanghuanlai on 2017/1/19.
 */
public class JavaFileImpl implements IJavaFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaFileImpl.class);

    private static final String CHECK_FILE_SUFFIX = ".java";
    private static final String PACKAGE_PREFIX = "import ";
    private String javaFilePath;
    private List<String> includePaths = new ArrayList<>();
    private String projectPath;
    private static final String RELATIVE_PATH = "src/main/java/";

    private List<String> includePathsBbsoluteToRelative() {
        List<String> relativePaths = new ArrayList<>();
        for (String abPath : includePaths) {
            int index = abPath.indexOf(RELATIVE_PATH);
            if (index > -1) {
                System.out.println(abPath.substring(index));
            }
        }
        return relativePaths;
    }

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
        if (javaTxt.contains(".")) {//查找关键字包含点的
            File javaTxtFile = new File(getEndSplitPath(projectPath) + javaTxt.replace(".", "/") + CHECK_FILE_SUFFIX);
            List<File> findChildFiles = new ArrayList<>();
            if (!javaTxtFile.exists()) {//主项目不存在,查找其它项目
                for (String childProjectPath : includePaths) {
                    javaTxtFile = new File(getEndSplitPath(childProjectPath) + javaTxt.replace(".", "/") + CHECK_FILE_SUFFIX);
                    if (javaTxtFile.exists()) {
                        findChildFiles.add(javaTxtFile);
                    }
                }
            } else {
                findChildFiles.add(javaTxtFile);
            }
            System.out.println("point:" + findChildFiles.get(0).getAbsolutePath());
            if (findChildFiles.size() > 1) {
                findChildFiles = findChildFiles.stream().sorted((a, b) -> ((Integer) a.getAbsolutePath().length()).compareTo(b.getAbsolutePath().length())).collect(Collectors.toList());//优先取包层次少的文件
                LOGGER.warn("找到多个文件" + JSON.toJSONString(findChildFiles));
            }
        } else {
            List<File> findChildFiles = new ArrayList<>();
            List<String> javaFileLines = null;
            try {
                javaFileLines = FileUtils.readLines(new File(javaFilePath), Charset.forName("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> javaFileImportPackageLines = filterJavaFileImportPackageLines(javaFileLines);
            Optional<String> optionalLine = javaFileImportPackageLines.stream().filter(line -> line.endsWith("." + javaTxt + ";")).findFirst();
            if (optionalLine.isPresent()) {
                String packageStr = StringUtils.substring(optionalLine.get(), PACKAGE_PREFIX.length(), -1).replace(".", "/");
                File javaTxtFile = new File(getEndSplitPath(projectPath) + CHECK_FILE_SUFFIX);
                if (!javaTxtFile.exists()) {//主项目不存在,查找其它项目
                    for (String childProjectPath : includePaths) {
                        javaTxtFile = new File(getEndSplitPath(childProjectPath) + packageStr.replace(".", "/") + CHECK_FILE_SUFFIX);
                        if (javaTxtFile.exists()) {
                            findChildFiles.add(javaTxtFile);
                        }
                    }
                } else {
                    findChildFiles.add(javaTxtFile);
                }
                System.out.println("noPoint:" + javaTxtFile.getAbsolutePath());
            }
            if (findChildFiles.size() > 1) {
                findChildFiles = findChildFiles.stream().sorted((a, b) -> ((Integer) a.getAbsolutePath().length()).compareTo(b.getAbsolutePath().length())).collect(Collectors.toList());//优先取包层次少的文件
                LOGGER.warn("找到多个文件" + JSON.toJSONString(findChildFiles));
            }

            if (findChildFiles.size() == 0) {
                List<String> containEndStrs = javaFileImportPackageLines.stream().filter(line -> line.endsWith(".*;")).collect(Collectors.toList());
                final IOFileFilter javaFileFilter = FileFilterUtils.asFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.isFile() && pathname.getName().equals(javaTxt + CHECK_FILE_SUFFIX);
                    }
                });
                List<File> containFiles = new ArrayList<>();
                for (String containStr : containEndStrs) {
                    String packagePath = StringUtils.substring(containStr, PACKAGE_PREFIX.length(), -3).replace(".", "/");
                    File packageFold = new File(getEndSplitPath(projectPath) + packagePath);
                    if (packageFold.exists()) {//主项目包目录是否存在，存在则进去检测有符合的文件
                        Collection<File> packageChildFiles = FileUtils.listFiles(packageFold, javaFileFilter, TrueFileFilter.INSTANCE);//查找所有文件
                        if (packageChildFiles.size() > 0) {
                            containFiles.addAll(packageChildFiles);
                        }
                    } else {//检测其它项目
                        for (String childProjectPath : includePaths) {
                            packageFold = new File(getEndSplitPath(childProjectPath) + packagePath);
                            if (packageFold.exists()) {//其它项目包目录是否存在，存在则进去检测有符合的文件
                                Collection<File> packageChildFiles = FileUtils.listFiles(packageFold, javaFileFilter, TrueFileFilter.INSTANCE);//查找所有文件
                                if (packageChildFiles.size() > 0) {
                                    containFiles.addAll(packageChildFiles);
                                }
                            }
                        }
                    }
                }
                if(containFiles.size()>1){
                    containFiles = containFiles.stream().sorted((a, b) -> ((Integer) a.getAbsolutePath().length()).compareTo(b.getAbsolutePath().length())).collect(Collectors.toList());//优先取包层次少的文件
                }
                System.out.println(JSON.toJSON(containFiles));
//                Collection<File> packageChildFiles = FileUtils.listFiles(packageFold, fileFilter, TrueFileFilter.INSTANCE);//查找所有文件
            }
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

    private String getEndSplitPath(String path) {
        String splitChar = path.endsWith("/") ? "" : "/";
        return path + splitChar;
    }

    /**
     * 检测文件是否在几个项目当中
     *
     * @param javaName    java名称
     * @param packageName 导包名称
     * @param srcFilePath 源文件路径
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
            String newJavaName = javaName.indexOf(".") > 0 ? javaName.substring(javaName.lastIndexOf(".") + 1) : javaName;
            IOFileFilter fileFilter = FileFilterUtils.asFileFilter(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().endsWith(newJavaName + CHECK_FILE_SUFFIX);
                }
            });
            Collection<File> packageFoldChildFiles = FileUtils.listFiles(srcFold, fileFilter, null);
            if (packageFoldChildFiles.size() == 0) {
                throw new JapiException("找不到相关文件:" + newJavaName + CHECK_FILE_SUFFIX);
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
