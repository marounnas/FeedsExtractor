package com.shedd.feedsextractor.data.local;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class JsonProvider {

    public static <T> T getCachedObject(Context context, Class<T> type, String meta) throws Exception {
        try {
            Gson gson = new Gson();
            FileInputStream fis = context.openFileInput(type.getSimpleName() + "_meta_" + meta);
            T object = gson.fromJson(new InputStreamReader(fis), type);
            fis.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static void storeObject(Context context, Object object, String meta) throws Exception {
        try {
            FileOutputStream fos = context.openFileOutput(object.getClass().getSimpleName() + "_meta_" + meta, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String data = gson.toJson(object, object.getClass());
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

}
