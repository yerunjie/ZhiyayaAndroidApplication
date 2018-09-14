package com.lemon.support.request;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A sample showing a custom {@link CallAdapter} which adapts the built-in {@link Call} to a custom
 * version whose callback has more granular methods.
 */
public final class ErrorHandlingCallAdapter {

    private static final String TAG = "ErrorHandlingCallAdapter";

    public static class ErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
        @Override
        public CallAdapter<SimpleCall<?>> get(Type returnType, Annotation[] annotations,
                                              Retrofit retrofit) {
            if (getRawType(returnType) != SimpleCall.class) {
                return null;
            }
            if (!(returnType instanceof ParameterizedType)) {
                throw new IllegalStateException(
                        "MyCall must have generic type (e.g., MyCall<ResponseBody>)");
            }
            final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
            return new CallAdapter<SimpleCall<?>>() {
                @Override
                public Type responseType() {
                    return responseType;
                }

                @Override
                public <R> SimpleCall<R> adapt(Call<R> call) {
                    return new MyCallAdapter<>(call);
                }
            };
        }
    }

    /**
     * Adapts a {@link Call} to {@link SimpleCall}.
     */
    static class MyCallAdapter<T> implements SimpleCall<T> {
        private final Call<T> call;

        MyCallAdapter(Call<T> call) {
            this.call = call;
        }

        @Override
        public void cancel() {
            call.cancel();
        }

        @Override
        public void enqueue(final ErrorHandlingCallBack<T> callback) {
            call.enqueue(new Callback<T>() {

                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    int code = response.code();
                    if (code >= 200 && code < 300) {
                        callback.success(response);
                    } else if (code == 401) {
                        callback.unauthenticated(response);
                    } else if (code >= 400 && code < 500) {
                        callback.clientError(response);
                    } else if (code >= 500 && code < 600) {
                        callback.serverError(response);
                    } else {
                        callback.unexpectedError(new RuntimeException("Unexpected response " + response));
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    if (t instanceof IOException) {
                        callback.networkError((IOException) t);
                    } else {
                        callback.unexpectedError(t);
                    }
                }
            });
        }

        @Override
        public SimpleCall<T> clone() {
            return new MyCallAdapter<>(call.clone());
        }
    }
}
