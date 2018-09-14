package com.lemon.support.request;

import android.content.Context;
import android.os.Handler;

import com.lemon.support.request.converters.SimpleConverterFactory;
import com.lemon.support.request.cookie.PersistentCookieJar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by legend on 17/9/9.
 */

public class RequestManager {
    private static RequestManager mManager;
    private Context mContext;
    private OkHttpClient mClient;
    private Retrofit retrofit;
    private Handler mHandler;
    private static final String HOST_ = "http://name.renren.com";
    private Map<String, Retrofit> mRetrofitMap = new HashMap<>();
    private PersistentCookieJar mCookieJar;

    private RequestManager(Context context) {
        mContext = context;
        mCookieJar = new PersistentCookieJar(context);
        mClient = new OkHttpClient();
        mHandler = new Handler();
        mClient = mClient.newBuilder().cookieJar(mCookieJar).connectTimeout(20, TimeUnit.SECONDS).build();
        getRetrofit(HOST_);

    }

    private Retrofit getRetrofit(String endPoint) {
        if (mRetrofitMap.containsKey(endPoint)) {
            return mRetrofitMap.get(endPoint);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endPoint)
                .client(mClient)
                .addCallAdapterFactory(new ErrorHandlingCallAdapter.ErrorHandlingCallAdapterFactory())
                .addConverterFactory(SimpleConverterFactory.create())
                .build();
        mRetrofitMap.put(endPoint, retrofit);
        return retrofit;
    }

    public <T> T getService(Class<T> service){
        return getRetrofit(HOST_).create(service);
    }

    public <R> R getService(String endPoint, Class<R> c) {
        return getRetrofit(endPoint).create(c);
    }

    public static RequestManager create(Context context) {
        if (mManager != null) {
            return mManager;
        }
        return new RequestManager(context);
    }

    public PersistentCookieJar getCookieJar() {
        return mCookieJar;
    }

    public OkHttpClient getClient(){
        return mClient;
    }
//    public void doPost(String url, Callback callback) {
//        //url "http://otwtriz08.bkt.clouddn.com/lone-debug.apk"
//
//        Request request = new Request.Builder().url(url).build();
//        mClient.newCall(request).enqueue(callback);
//    }

    public void doPostFormData(String url, Map<String, String> params, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> es : params.entrySet()) {
            builder.add(es.getKey(), es.getValue());
        }
        RequestBody formBody = builder.build();

        Request request = new Request.Builder().url(url).post(formBody).build();
        mClient.newCall(request).enqueue(callback);
    }

    public void doGetWithParam(String url, Callback callback, String name, String value) {
        String newUrl = HOST_ + url + "?" + name + "=" + value;

        Request request = new Request.Builder().url(newUrl).build();
        mClient.newCall(request).enqueue(callback);
    }

    public void doGetWithParams(String url, Map<String, String> params, Callback callback) {
        String getUrl = HOST_ + url + "?";
        int i = params.size();
        for (Map.Entry es : params.entrySet()) {
            if (i > 1) {
                getUrl = getUrl + es.getKey() + "=" + es.getValue() + "&";
                i--;
            } else {
                getUrl = getUrl + es.getKey() + "=" + es.getValue();
            }
        }
        //
        Request request = new Request.Builder().url(getUrl).build();
        mClient.newCall(request).enqueue(callback);
    }

    public <R> void addRequest(final SimpleCall call, final SimpleCallBack<R> callBack,
                               final RequestStateListener listener) {
        if (listener != null) {
            listener.onStart();
        }
        call.enqueue(new ErrorHandlingCallBack<R>() {
            @Override
            public void success(final Response<R> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onSuccess(response.body());
                        }
                        if (listener != null) {
                            listener.onSuccess(response.body());
                            listener.onFinish();
                        }
                    }
                });
            }

            @Override
            public void unauthenticated(final Response<?> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onFailure(response.body());
                        }
                        if (listener != null) {
                            listener.onFailure();
                            listener.onFinish();
                        }
                    }
                });
            }

            @Override
            public void clientError(final Response<?> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onFailure(response.body());
                        }
                        if (listener != null) {
                            listener.onFailure();
                            listener.onFinish();
                        }
                    }
                });
            }

            @Override
            public void serverError(final Response<?> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onFailure(response.body());
                        }
                        if (listener != null) {
                            listener.onFailure();
                            listener.onFinish();
                        }
                    }
                });
            }

            @Override
            public void networkError(final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onError(e);
                        }
                        if (listener != null) {
                            listener.onFailure();
                            listener.onFinish();
                        }
                    }
                });
            }

            @Override
            public void unexpectedError(final Throwable t) {
                t.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onError(new Exception(t));
                        }
                        if (listener != null) {
                            listener.onFailure();
                            listener.onFinish();
                        }
                    }
                });
            }
        });
    }
}
