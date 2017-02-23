/*
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: Tony Dylan
 * Date: 2014-12-5
 * Description: Preferences工具类，用于本地存储数据
 * Others:
 */
package com.dylan.baseui.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Map;

/**
 * Preferences工具类，用于管理本地存储数据
 */
public class PrefUtils {

    /**
     * 写入string值
     *
     * @param context 上下文实例
     * @param name    sp名称
     * @param key     对应key值
     * @param value   要写入的value值
     */
    public static void putString(Context context, String name, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value).apply();
    }

    /**
     * 读取string值
     *
     * @param context 上下文实例
     * @param name    sp名称
     * @param key     对应key值
     * @return key对应的string值
     */
    public static String getString(Context context, String name, String key) {
        return getString(context, name, key, null);
    }

    /**
     * 读取string值
     *
     * @param context      上下文实例
     * @param name         sp名称
     * @param key          对应key值
     * @param defaultValue 默认value值
     * @return key对应的string值
     */
    public static String getString(Context context, String name, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    /**
     * 写入int值
     *
     * @param context 上下文实例
     * @param name    sp名称
     * @param key     对应key值
     * @param value   要写入的value值
     */
    public static void putInt(Context context, String name, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value).apply();
    }

    /**
     * 读取int值
     *
     * @param context 上下文实例
     * @param name    sp名称
     * @param key     对应key值
     * @return key对应的int值
     */
    public static int getInt(Context context, String name, String key) {
        return getInt(context, name, key, -1);
    }

    /**
     * 读取int值
     *
     * @param context      上下文实例
     * @param name         sp名称
     * @param key          对应key值
     * @param defaultValue 默认value值
     * @return key对应的int值
     */
    public static int getInt(Context context, String name, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    /**
     * 写入long值
     *
     * @param context 上下文实例
     * @param name    sp名称
     * @param key     对应key值
     * @param value   要写入的value值
     */
    public static void putLong(Context context, String name, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value).apply();
    }

    /**
     * 读取long值
     *
     * @param context 上下文实例
     * @param name    sp名称
     * @param key     对应key值
     * @return key对应的long值
     */
    public static long getLong(Context context, String name, String key) {
        return getLong(context, name, key, -1);
    }

    /**
     * 读取long值
     *
     * @param context      上下文实例
     * @param name         sp名称
     * @param key          对应key值
     * @param defaultValue 默认value值
     * @return key对应的long值
     */
    public static long getLong(Context context, String name, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    /**
     * 写入float值
     *
     * @param context 上下文实例
     * @param name    sp名称
     * @param key     对应key值
     * @param value   要写入的value值
     */
    public static void putFloat(Context context, String name, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value).apply();
    }

    /**
     * 读取float值
     *
     * @param context 上下文实例
     * @param name    sp名称
     * @param key     对应key值
     * @return key对应的float值
     */
    public static float getFloat(Context context, String name, String key) {
        return getFloat(context, name, key, -1);
    }

    /**
     * 读取float值
     *
     * @param context      上下文实例
     * @param name         sp名称
     * @param key          对应key值
     * @param defaultValue 默认value值
     * @return key对应的float值
     */
    public static float getFloat(Context context, String name, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * 写入boolean值
     *
     * @param context 上下文实例
     * @param name    sp名称
     * @param key     对应key值
     * @param value   要写入的value值
     */
    public static void putBoolean(Context context, String name, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value).apply();
    }

    /**
     * 读取bolean值
     *
     * @param context 上下文实例
     * @param name    sp名称
     * @param key     对应key值
     * @return key对应的boolean值
     */
    public static boolean getBoolean(Context context, String name, String key) {
        return getBoolean(context, name, key, false);
    }

    /**
     * 读取bolean值
     *
     * @param context      上下文实例
     * @param name         sp名称
     * @param key          对应key值
     * @param defaultValue 默认value值
     * @return key对应的boolean值
     */
    public static boolean getBoolean(Context context, String name, String key,
                                     boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * 保存对象到SharedPreferences，存入的对象必需继承Serializable接口，否则取出来会是null
     *
     * @param context 上下文对象
     * @param key     存入的key
     * @param value   需要存入的对象
     */
    public static void saveObject(Context context, String name, String key, Object value) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(value);
            // 将字节流编码成base64的字符窜
            String data = Base64.encodeToString(baos
                    .toByteArray(), Base64.DEFAULT);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, data);
            editor.apply();
        } catch (IOException e) {
            // TODO Auto-generated
            e.printStackTrace();
        }
    }

    /**
     * 从SharedPreferences中取对象
     *
     * @param context 上下文对象
     * @param name    sp名称
     * @param key     存入的key
     * @return key对应的object
     */
    public static Object readObject(Context context, String name, String key) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        String data = preferences.getString(key, "");
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        try {
            //读取字节
            byte[] base64 = Base64.decode(data.getBytes(), Base64.DEFAULT);

            //封装到字节流
            ByteArrayInputStream bais = new ByteArrayInputStream(base64);

            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                return bis.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断SharedPreferences中是否有某个值
     *
     * @param context 上下文对象
     * @param name    SharedPreferences的名字
     * @param key     需要判断的key
     * @return 是否包含key true--包含 false--不包含
     */
    public static boolean containKey(Context context, String name, String key) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.contains(key);
    }

    /**
     * 清除SharedPreferences中对应的key值
     *
     * @param context 上下文对象
     * @param name    SharedPreferences的名字
     * @param key     存入的key
     */
    public static void remove(Context context, String name, String key) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        preferences.edit().remove(key).apply();
    }

    /**
     * 清除SharedPreferences中所有值
     *
     * @param context 上下文对象
     * @param name    SharedPreferences的名字
     */
    public static void clear(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }

    public static Map<String, ?> getAll(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getAll();
    }
}
