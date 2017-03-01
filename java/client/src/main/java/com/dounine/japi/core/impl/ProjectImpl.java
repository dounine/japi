package com.dounine.japi.core.impl;

import com.dounine.japi.JapiClient;
import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IConfig;
import com.dounine.japi.core.IPackage;
import com.dounine.japi.core.IProject;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class ProjectImpl implements IProject {

    private static final Pattern API_CONFIG_NAME = JapiPattern.getPattern("\\s*japi[.a-zA-Z0-9_]*\\s*[=]\\s*");

    private Map<String, String> properties = new HashMap<>();
    private static final ProjectImpl PROJECT = new ProjectImpl();

    private ProjectImpl(){}
    public static IProject init(Map<String, String> properties) {
        PROJECT.properties = properties;
        return PROJECT;
    }

    public static IProject init(String propertiesFilePath) {
        return init(new File(propertiesFilePath));
    }

    public static IProject init() {
        return init(new File(ProjectImpl.class.getResource("/japi.properties").getFile()));
    }

    public static IProject init(File propertiesFile) {
        if (!propertiesFile.exists()) {
            throw new JapiException(propertiesFile.getAbsolutePath() + " 配置文件不存在.");
        }
        try {
            List<String> propertiesLines = FileUtils.readLines(propertiesFile, Charset.forName("utf-8"));
            for (String line : propertiesLines) {
                Matcher apiMatcher = API_CONFIG_NAME.matcher(line);
                if (apiMatcher.find()) {
                    String configName = apiMatcher.group();
                    configName = configName.split("=")[0].trim();
                    String configValue = line.substring(apiMatcher.end()).trim();
                    PROJECT.properties.put(configName, configValue);
                }
            }
        } catch (IOException e) {
            throw new JapiException(e.getMessage());
        }
        return PROJECT;
    }

    @Override
    public List<IPackage> getPackages() {
        String masterProjectActionPath = JapiClient.getConfig().getProjectJavaPath() + "/" + JapiClient.getConfig().getActionReletivePath();
        File actionFold = new File(masterProjectActionPath);
        if (!actionFold.exists()) {
            throw new JapiException(masterProjectActionPath + " fold not exists.");
        }
        final IOFileFilter dirFilter = FileFilterUtils.asFileFilter(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        Collection<File> folds = FileUtils.listFilesAndDirs(actionFold, dirFilter, TrueFileFilter.INSTANCE);
        List<IPackage> packages = new ArrayList<>(folds.size());
        for (File fold : folds) {
            if(!fold.getAbsolutePath().equals(actionFold.getAbsolutePath())){
                PackageImpl packageImpl = new PackageImpl();
                packageImpl.setPackageFold(fold);
                packages.add(packageImpl);
            }
        }
        return packages;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
