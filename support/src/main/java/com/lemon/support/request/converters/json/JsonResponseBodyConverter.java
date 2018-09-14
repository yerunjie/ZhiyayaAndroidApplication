package com.lemon.support.request.converters.json;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public final class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    public JsonResponseBodyConverter() {

    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(value.string());
            return (T) jsonObj;
        } catch(JSONException e) {
            return null;
        }
    }
}
