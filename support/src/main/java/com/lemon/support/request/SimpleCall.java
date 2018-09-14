package com.lemon.support.request;

/**
 * Created by legend on 18/1/3.
 */

public interface SimpleCall<T> {
    void cancel();

    void enqueue(ErrorHandlingCallBack<T> callback);

    SimpleCall<T> clone();
}
