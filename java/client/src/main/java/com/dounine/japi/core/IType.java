package com.dounine.japi.core;

import com.dounine.japi.serial.request.IRequest;
import com.dounine.japi.serial.request.RequestImpl;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IType {

    List<IField> getFields();

    String getName();

}
