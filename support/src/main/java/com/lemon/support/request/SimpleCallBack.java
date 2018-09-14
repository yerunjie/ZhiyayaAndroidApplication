package com.lemon.support.request;

public interface SimpleCallBack<T> {

    void onSuccess(T body);
    void onFailure(Object body);
    void onError(Exception e);
}
