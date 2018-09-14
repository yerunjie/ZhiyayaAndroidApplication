package com.lemon.support.request.cookie;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class PersistentCookieJar implements CookieJar {

    private final PersistentCookieStore cookieStore;

    public PersistentCookieJar(Context mContext) {
        cookieStore = new PersistentCookieStore(mContext);
    }
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }
}
