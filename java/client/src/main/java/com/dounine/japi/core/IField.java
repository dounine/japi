package com.dounine.japi.core;

import com.dounine.japi.serial.request.IRequest;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IField {

    List<String> getAnnotations();

    List<IField> getFields();

    String getName();

    String getType();

    List<IFieldDoc> getDocs();

    boolean isEnumType();

    IRequest enumRequest();
}
