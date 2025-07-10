package com.prime31;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: IABPlugin.aar:classes.jar:com/prime31/GoogleIABPluginBase.class */
public class GoogleIABPluginBase {
    private static GoogleIABPlugin _instance;
    protected static final String TAG = "Prime31";
    protected static final String MANAGER_NAME = "GoogleIABManager";
    private Class<?> _unityPlayerClass;
    private Field _unityPlayerActivityField;
    private Method _unitySendMessageMethod;
    public Activity _activity;
    protected LinearLayout _layout;

    public static GoogleIABPlugin instance() {
        if (_instance == null) {
            _instance = new GoogleIABPlugin();
        }
        return _instance;
    }

    public GoogleIABPluginBase() {
        try {
            this._unityPlayerClass = Class.forName("com.unity3d.player.UnityPlayer");
            this._unityPlayerActivityField = this._unityPlayerClass.getField("currentActivity");
            this._unitySendMessageMethod = this._unityPlayerClass.getMethod("UnitySendMessage", String.class, String.class, String.class);
        } catch (ClassNotFoundException var2) {
            Log.i(TAG, "could not find UnityPlayer class: " + var2.getMessage());
        } catch (NoSuchFieldException var3) {
            Log.i(TAG, "could not find currentActivity field: " + var3.getMessage());
        } catch (Exception var4) {
            Log.i(TAG, "unkown exception occurred locating getActivity(): " + var4.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Activity getActivity() {
        if (this._unityPlayerActivityField != null) {
            try {
                Activity activity = (Activity) this._unityPlayerActivityField.get(this._unityPlayerClass);
                if (activity == null) {
                    Log.e(TAG, "Something has gone terribly wrong. The Unity Activity does not exist. This could be due to a low memory situation");
                }
                return activity;
            } catch (Exception var2) {
                Log.i(TAG, "error getting currentActivity: " + var2.getMessage());
            }
        }
        return this._activity;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void UnitySendMessage(String m, String p) {
        if (p == null) {
            p = "";
        }
        if (this._unitySendMessageMethod == null) {
            Toast.makeText(getActivity(), "UnitySendMessage:\n" + m + "\n" + p, 1).show();
            Log.i(TAG, "UnitySendMessage: GoogleIABManager, " + m + ", " + p);
            return;
        }
        try {
            this._unitySendMessageMethod.invoke(null, MANAGER_NAME, m, p);
        } catch (IllegalAccessException var5) {
            Log.i(TAG, "could not find UnitySendMessage method: " + var5.getMessage());
        } catch (IllegalArgumentException var4) {
            Log.i(TAG, "could not find UnitySendMessage method: " + var4.getMessage());
        } catch (InvocationTargetException var6) {
            Log.i(TAG, "could not find UnitySendMessage method: " + var6.getMessage());
        }
    }

    protected void runSafelyOnUiThread(Runnable r) {
        runSafelyOnUiThread(r, null);
    }

    protected void runSafelyOnUiThread(final Runnable r, final String methodName) {
        getActivity().runOnUiThread(new Runnable() { // from class: com.prime31.GoogleIABPluginBase.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    r.run();
                } catch (Exception var2) {
                    if (methodName != null) {
                        GoogleIABPluginBase.this.UnitySendMessage(methodName, var2.getMessage());
                    }
                    Log.e(GoogleIABPluginBase.TAG, "Exception running command on UI thread: " + var2.getMessage());
                }
            }
        });
    }

    protected void persist(String key, String value) {
        IABConstants.logEntering(getClass().getSimpleName(), "persist", new Object[]{key, value});
        try {
            SharedPreferences prefs = getActivity().getSharedPreferences("P31Preferences", 0);
            prefs.edit().putString(key, value).commit();
        } catch (Exception var4) {
            Log.i(TAG, "error in persist: " + var4.getMessage());
        }
    }

    protected String unpersist(String key, boolean deleteKeyAfterFetching) {
        IABConstants.logEntering(getClass().getSimpleName(), "unpersist", new Object[]{key, true});
        String val = "";
        try {
            SharedPreferences prefs = getActivity().getSharedPreferences("P31Preferences", 0);
            val = prefs.getString(key, null);
            if (deleteKeyAfterFetching) {
                prefs.edit().remove(key).commit();
            }
            return val;
        } catch (Exception var5) {
            Log.i(TAG, "error in unpersist: " + var5.getMessage());
            return val;
        }
    }

    public static Bundle bundleFromJson(JSONObject s) {
        Bundle bundle = new Bundle();
        Iterator it = s.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            JSONArray arr = s.optJSONArray(key);
            Double num = Double.valueOf(s.optDouble(key));
            String str = s.optString(key);
            if (arr != null && arr.length() <= 0) {
                bundle.putStringArray(key, new String[0]);
            } else if (arr != null && !Double.isNaN(arr.optDouble(0))) {
                double[] newarr = new double[arr.length()];
                for (int i = 0; i < arr.length(); i++) {
                    newarr[i] = arr.optDouble(i);
                }
                bundle.putDoubleArray(key, newarr);
            } else if (arr != null && arr.optString(0) != null) {
                String[] newarr2 = new String[arr.length()];
                for (int i2 = 0; i2 < arr.length(); i2++) {
                    newarr2[i2] = arr.optString(i2);
                }
                bundle.putStringArray(key, newarr2);
            } else if (!num.isNaN()) {
                bundle.putDouble(key, num.doubleValue());
            } else if (str != null) {
                bundle.putString(key, str);
            } else {
                System.err.println("unable to transform json to bundle " + key);
            }
        }
        return bundle;
    }

    protected String jsonFromBundle(Bundle bundle) {
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    json.put(key, JSONObject.wrap(bundle.get(key)));
                } else {
                    json.put(key, bundle.get(key));
                }
                return json.toString();
            } catch (JSONException var7) {
                var7.printStackTrace();
            }
        }
        return "{}";
    }
}
