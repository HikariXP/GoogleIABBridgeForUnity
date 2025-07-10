package com.prime31;

import android.util.Log;

/* loaded from: IABPlugin.aar:classes.jar:com/prime31/IABConstants.class */
public class IABConstants {
    private static final String TAG = "Prime31-HD";
    public static boolean DEBUG = false;

    public static void logDebug(String log) {
        if (DEBUG) {
            Log.i(TAG, log);
        }
    }

    public static void logEntering(String klass, String method) {
        if (DEBUG) {
            Log.i(TAG, klass + "." + method + "()");
        }
    }

    public static void logEntering(String klass, String method, Object param) {
        if (DEBUG) {
            Log.i(TAG, klass + "." + method + "( " + param + " )");
        }
    }

    public static void logEntering(String klass, String method, Object[] params) {
        if (DEBUG) {
            String prefix = "";
            StringBuilder b = new StringBuilder();
            for (Object p : params) {
                b.append(prefix);
                b.append(p);
                prefix = ", ";
            }
            Log.i(TAG, klass + "." + method + "( " + b.toString() + " )");
        }
    }
}
