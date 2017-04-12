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
    public static <T> T getObject(String data, Type type) throws Exception {
        try {
            Gson gson = new Gson();
            T object = gson.fromJson(data, type);
            return object;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static <T> T getObjectExposed(String data, Type type) throws Exception {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.excludeFieldsWithoutExposeAnnotation().create();
            T object = gson.fromJson(data, type);
            return object;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public static <T> T getObject(String data, Class<T> type) throws Exception {
        try {
            Gson gson = new Gson();
            return gson.fromJson(data, type);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static <T> T getObjectExposed(String data, Class<T> type) throws Exception {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.excludeFieldsWithoutExposeAnnotation().create();
            return gson.fromJson(data, type);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public static <T> T getCachedObject(Context context, Class<T> type) throws Exception {
        try {
            Gson gson = new Gson();
            FileInputStream fis = context.openFileInput(type.getSimpleName());
            T object = gson.fromJson(new InputStreamReader(fis), type);
            fis.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

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

    public static void storeObject(Context context, Object object) throws Exception {
        try {
            FileOutputStream fos = context.openFileOutput(object.getClass().getSimpleName(), Context.MODE_PRIVATE);
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

    public static boolean deleteObject(Context context, Class<?> clazz) {
        return context.deleteFile(clazz.getSimpleName());
    }

    public static boolean deleteObject(Context context, Class<?> clazz, String meta) {
        return context.deleteFile(clazz.getSimpleName() + "_meta_" + meta);
    }


    ///////////////////////////////////
    // STORE AND GET BY NAME
    ///////////////////////////////////

    public static void storeObject(Context context, String name, Object object) throws Exception {
        try {
            FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);
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

    public static <T> T getCachedObject(Context context, String name, Type type) throws Exception {
        try {
            Gson gson = new Gson();
            FileInputStream fis = context.openFileInput(name);
            T object = gson.fromJson(new InputStreamReader(fis), type);
            fis.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static <T> T getCachedObject(Context context, String name, Class<T> type) throws Exception {
        return getCachedObject(context, name, ((ParameterizedType) type
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

}
