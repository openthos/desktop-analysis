package com.openthos.launcher.openthoslauncher.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by xu on 2016/8/10.
 */
public class HomeGestureDetector extends GestureDetector {
    public HomeGestureDetector(Context context, OnGestureListener listener) {
        super(context, listener);
        fix();
    }

    public HomeGestureDetector(OnGestureListener listener, Handler handler) {
        super(listener, handler);
        fix();
    }

    public HomeGestureDetector(OnGestureListener listener) {
        super(listener);
        fix();
    }

    public HomeGestureDetector(Context context, OnGestureListener listener, Handler handler, boolean unused) {
        super(context, listener, handler, unused);
        fix();
    }

    public HomeGestureDetector(Context context, OnGestureListener listener, Handler handler) {
        super(context, listener, handler);
        fix();
    }


    void fix() {
        Field field = null;
        try {
            field = GestureDetector.class.getField("LONGPRESS_TIMEOUT");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        //将字段的访问权限设为true：即去除private修饰符的影响
        field.setAccessible(true);
        /*去除final修饰符的影响，将字段设为可修改的*/
        Field modifiersField = null;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        //把字段值设为10
        try {
            field.set(null, 10);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
