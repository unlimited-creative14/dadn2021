package com.hk203.dadn.utils;

import com.google.gson.Gson;
import com.hk203.dadn.models.ErrorResponse;

import okhttp3.ResponseBody;

public class ErrorUtil {
    public static ErrorResponse parseErrorBody(ResponseBody errorBody){
        return (new Gson()).fromJson(errorBody.charStream(),ErrorResponse.class);
    }
}
