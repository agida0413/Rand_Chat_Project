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

    public static boolean mustNotNull(Object obj){
        boolean res = true;
        if(obj ==null || obj.equals("")){
            res = false;
        }
        if(obj instanceof String){
            String str = ((String) obj).trim(); // 공백 제거
            if (str.isEmpty()) { // 비어 있는지 확인
                return false;
            }
        }
        return res;
    }
}
