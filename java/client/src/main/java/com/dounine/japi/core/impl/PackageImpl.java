package com.dounine.japi.core.impl;

import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IAction;
import com.dounine.japi.core.IPackage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class PackageImpl implements IPackage {

    private File packageFold;

    @Override
    public List<IAction> getActions() {
        final IOFileFilter dirFilter = FileFilterUtils.asFileFilter(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".java") && !pathname.getName().equals("package-info.java");
            }
        });
        Collection<File> actionFiles = FileUtils.listFiles(packageFold, dirFilter, null);
        List<IAction> actions = new ArrayList<>();
        for (File actionFile : actionFiles) {
            ActionImpl action = new ActionImpl();
            action.setActionFile(actionFile);
            actions.add(action);
        }
        return actions;
    }

    @Override
    public String getName() {
        try {
            final IOFileFilter dirFilter = FileFilterUtils.asFileFilter(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().equals("package-info.java");
                }
            });
            Collection<File> actionFiles = FileUtils.listFiles(packageFold, dirFilter, null);
            if(actionFiles.size()==1){
                List<String> javaLines = FileUtils.readLines(actionFiles.iterator().next(), Charset.forName("utf-8"));
                Pattern docBeginPattern = JapiPattern.getPattern("[/][*][*]");
                Pattern classBeginPattern = JapiPattern.getPattern("[a-zA-Z0-9_]+\\s*[{]$");
                List<String> docsAndAnnos = new ArrayList<>();
                boolean docBegin = false;
                boolean classBegin = false;
                for (String line : javaLines) {
                    if (false == docBegin && docBeginPattern.matcher(line).find()) {
                        docBegin = true;
                    }
                    if (docBegin && !classBegin) {
                        docsAndAnnos.add(line);
                    }
                    if(classBeginPattern.matcher(line).find()){
                        classBegin = true;
                        break;
                    }
                }
                if(docsAndAnnos.size()>0){
                    Pattern docEndPattern = JapiPattern.getPattern("[*][/]$");
                    List<String> docs = new ArrayList<>();
                    for(String line : docsAndAnnos){
                        docs.add(line);
                        if(docEndPattern.matcher(line).find()){
                            break;
                        }
                    }
                    String name = "";
                    for(String line : docs){
                        if(line.length()>3){
                            name = line.substring(3).trim();
                            break;
                        }
                    }
                    return name;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File getPackageFold() {
        return packageFold;
    }

    public void setPackageFold(File packageFold) {
        this.packageFold = packageFold;
    }

}
