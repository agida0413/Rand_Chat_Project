package com.rand.common.service;

import org.springframework.stereotype.Service;

public class PathVarValidationService {

    public static boolean mustOverZero(Integer obj){
        boolean res = true;

        if(obj<1){
            res=false;
        }

        return res;

    }
}
