package com.dounine.japi.core.impl;

import com.dounine.japi.core.IDoc;
import com.dounine.japi.core.IMethod;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class MethodImpl implements IMethod{

    private IDoc[] docs;

    public IDoc[] getDocs() {
        return docs;
    }

    public void setDocs(IDoc[] docs) {
        this.docs = docs;
    }
}
