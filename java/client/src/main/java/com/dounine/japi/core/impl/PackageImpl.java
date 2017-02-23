package com.dounine.japi.core.impl;

import com.dounine.japi.core.IAction;
import com.dounine.japi.core.IPackage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        return null;
    }

    public File getPackageFold() {
        return packageFold;
    }

    public void setPackageFold(File packageFold) {
        this.packageFold = packageFold;
    }

}
